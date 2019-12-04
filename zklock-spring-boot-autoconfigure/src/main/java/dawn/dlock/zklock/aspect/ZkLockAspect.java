package dawn.dlock.zklock.aspect;

import dawn.dlock.zklock.anntation.ZkLock;
import dawn.dlock.zklock.core.LockHelper;
import dawn.dlock.zklock.core.lock.LockInfo;
import dawn.dlock.zklock.core.strategy.LockFailedStrategy;
import dawn.dlock.zklock.exception.ZkLockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author HEBO
 */
@Slf4j
@Order(0)
@Aspect
public class ZkLockAspect {

	@Autowired
	private LockHelper lockHelper;

	@Autowired
	private List<LockFailedStrategy> failedStrategies;

	private static final String delimiter = "/";

	/**
	 * Spel 解析器
	 */
	private ExpressionParser parser = new SpelExpressionParser();

	/**
	 * 本地变量名工具
	 */
	private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

	@Pointcut("@annotation(dawn.dlock.zklock.anntation.ZkLock)")
	public void lockPointcut() {
	}

	@Around("lockPointcut()")
	public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
		LockInfo info = buildLockInfo(joinPoint);
		try {
			// 加锁
			Boolean isLocked = lockHelper.lock(info);
			// 加锁异常, 执行失败策略
			if (BooleanUtils.isNotTrue(isLocked)) {
				isLocked = doFailed(info);
			}
			// 依然未加锁成功, 直接抛出异常
			if (BooleanUtils.isNotTrue(isLocked)) {
				throw new ZkLockException(String.format("[Zklock] lock failed, lockName:%s", info.getLockName()));
			}
			// 加锁成功
			log.info("[ZkLock] locked lockName:{}", info.getLockName());
			return joinPoint.proceed();
		} finally {
			if (info.getLock() != null) {
				info.getLock().unlock();
				log.info("[ZkLock] released lock lockName:{}", info.getLockName());
			}
		}
	}

	/**
	 * 解析锁相关信息
	 *
	 * @param point 连接点
	 * @return 锁信息
	 * @throws ZkLockException
	 */
	private LockInfo buildLockInfo(ProceedingJoinPoint point) throws ZkLockException {
		// 获取连接的方法对象
		Method method = ((MethodSignature) point.getSignature()).getMethod();

		// 必须通过Spring的AnnotationUtils获取ZkLock, 否则@AlisFor无效
		ZkLock zkLock = AnnotationUtils.findAnnotation(method, ZkLock.class);

		// 获取锁path
		String path = zkLock.path();
		if (StringUtils.isBlank(path) || StringUtils.startsWith(path, "#")) {
			// 未自定义path, 前缀为 /lockNamespace/className/methodName
			StringJoiner joiner = new StringJoiner(delimiter);
			joiner.add(method.getDeclaringClass().getCanonicalName());
			joiner.add(method.getName());

			// 指定path为SPEL, 锁路径规则为: /lockNamespace/className/methodName/SPEL
			if (StringUtils.startsWith(path, "#")) {
				String value = parseEl(zkLock.path(), method, point.getArgs());
				Assert.hasText(value, String.format("[ZkLock] parsed %s, but value is empty, method:%s#%s",
						zkLock.path(), method.getDeclaringClass().getName(), method.getName()));

				joiner.add(value);
			}
			path = joiner.toString();
		}

		// 在path首位添加/
		path = StringUtils.prependIfMissing(path, delimiter, delimiter);

		// 构建锁信息
		LockInfo info = new LockInfo();
		info.setIsLocked(false);
		info.setLockName(path);
		info.setZkLock(zkLock);
		return info;
	}

	/**
	 * 执行失败策略
	 *
	 * @param info 锁信息
	 * @return 执行结果
	 * @throws Throwable
	 */
	private Boolean doFailed(LockInfo info) throws Throwable {
		try {
			Class<? extends LockFailedStrategy> failedStrategy = info.getZkLock().failedStrategy();
			for (LockFailedStrategy strategy : failedStrategies) {
				if (strategy.getClass().equals(failedStrategy)) {
					return strategy.doFailed(info);
				}
			}
			log.warn("Zklock nonsupport LockFailedStrategy: {}, please check or change your config", info.getZkLock().failedStrategy().getCanonicalName());
		} catch (Exception e) {
			log.warn("Zklock invoke LockFailedStrategy: {} exception", info.getZkLock().failedStrategy().getCanonicalName(), e);
		}
		return false;
	}

	/**
	 * @param el     表达式
	 * @param method 方法
	 * @param args   方法参数
	 * @return
	 * @description 解析spring EL表达式
	 */
	private String parseEl(String el, Method method, Object[] args) throws ZkLockException {
		Assert.notEmpty(args, String.format("[ZkLock] parse %s failed, method:%s#%s", el, method.getDeclaringClass().getName(), method.getName()));
		String[] params = discoverer.getParameterNames(method);
		EvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < params.length; i++) {
			context.setVariable(params[i], args[i]);
		}
		return parser.parseExpression(el).getValue(context, String.class);
	}

}
