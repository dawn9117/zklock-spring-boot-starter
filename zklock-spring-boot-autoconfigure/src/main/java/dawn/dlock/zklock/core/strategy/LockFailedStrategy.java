package dawn.dlock.zklock.core.strategy;

import dawn.dlock.zklock.core.lock.LockInfo;

/**
 * 加锁失败策略
 *
 * @author HEBO
 * @created 2019-11-29 10:51
 */
public interface LockFailedStrategy {

	Boolean doFailed(LockInfo lockInfo) throws Throwable;

}
