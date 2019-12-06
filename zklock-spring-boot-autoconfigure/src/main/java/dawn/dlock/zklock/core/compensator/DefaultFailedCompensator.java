package dawn.dlock.zklock.core.compensator;

import dawn.dlock.zklock.core.lock.LockInfo;

/**
 * 默认加锁失败补偿器
 *
 * @author HEBO
 */
public class DefaultFailedCompensator implements LockFailedCompensator {

	@Override
	public Boolean compensate(LockInfo lockInfo) throws Throwable {
		return false;
	}

}
