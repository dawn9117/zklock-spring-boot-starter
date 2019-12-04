package dawn.dlock.zklock.core.strategy;

import dawn.dlock.zklock.core.lock.LockInfo;

/**
 * 加锁失败策略
 *
 * @author HEBO
 */
public interface LockFailedStrategy {

	/**
	 * 加锁失败会调用该方法:
	 * 该方法如果返回true, 则表示继续执行方法, 否则就抛出异常
	 *
	 * @param lockInfo 锁信息
	 * @return
	 * @throws Throwable
	 */
	Boolean doFailed(LockInfo lockInfo) throws Throwable;

}
