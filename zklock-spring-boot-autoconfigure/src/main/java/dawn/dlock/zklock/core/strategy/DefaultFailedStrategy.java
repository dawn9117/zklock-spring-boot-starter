package dawn.dlock.zklock.core.strategy;

import dawn.dlock.zklock.core.lock.LockInfo;

/**
 * 默认加锁失败策略
 *
 * @author HEBO
 */
public class DefaultFailedStrategy implements LockFailedStrategy {

	@Override
	public Boolean doFailed(LockInfo lockInfo) throws Throwable {
		return false;
	}

}
