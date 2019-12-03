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
import org.springframework.core.annotation.Order;

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

	private LockInfo buildLockInfo(ProceedingJoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		ZkLock zkLock = method.getAnnotation(ZkLock.class);
		String path = zkLock.path();
		if (StringUtils.isBlank(path)) {
			StringJoiner joiner = new StringJoiner(delimiter);
			joiner.add(method.getDeclaringClass().getCanonicalName());
			joiner.add(method.getName());
			path = StringUtils.prependIfMissing(joiner.toString(), delimiter, delimiter);
		}

		LockInfo info = new LockInfo();
		info.setIsLocked(false);
		info.setLockName(path);
		info.setZkLock(zkLock);
		return info;
	}

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


}
