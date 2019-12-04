package dawn.dlock.zklock.anntation;

import dawn.dlock.zklock.core.lock.DistributeLock;
import dawn.dlock.zklock.core.lock.ZkSemaphoreLock;
import dawn.dlock.zklock.core.lock.ZkSemaphoreMutexLock;
import dawn.dlock.zklock.core.strategy.DefaultFailedStrategy;
import dawn.dlock.zklock.core.strategy.LockFailedStrategy;
import dawn.dlock.zklock.core.strategy.RetryLockStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * zklock加锁注解
 *
 * @author HEBO
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ZkLock {

	/**
	 * zk锁路径
	 *
	 * @return 锁路径
	 */
	@AliasFor("value")
	String path() default StringUtils.EMPTY;

	/**
	 * zk锁路径
	 *
	 * @return 锁路径
	 */
	@AliasFor("path")
	String value() default StringUtils.EMPTY;

	/**
	 * 锁类型:
	 * 默认:不可重入共享锁
	 *
	 * @return 分布式锁
	 * @see ZkSemaphoreMutexLock
	 * @see ZkSemaphoreLock
	 */
	Class<? extends DistributeLock> type() default ZkSemaphoreMutexLock.class;

	/**
	 * 加锁失败策略
	 * 默认: DefaultLockFailedStrategy
	 *
	 * @return 失败策略
	 * @see DefaultFailedStrategy
	 * @see RetryLockStrategy
	 * @see ZkLock#retry()
	 */
	Class<? extends LockFailedStrategy> failedStrategy() default DefaultFailedStrategy.class;

	/**
	 * 获取锁重试次数:
	 * 默认3次
	 *
	 * @return 重试次数
	 * @see RetryLockStrategy
	 */
	int retry() default 3;

	/**
	 * 获取锁超时时间:
	 * 默认30秒
	 *
	 * @return 获取锁超时时间
	 */
	long timeout() default 30000;

	/**
	 * 获取锁超时时间单位
	 * 默认秒
	 *
	 * @return 获取锁超时时间单位
	 */
	TimeUnit timeunit() default TimeUnit.MILLISECONDS;


}
