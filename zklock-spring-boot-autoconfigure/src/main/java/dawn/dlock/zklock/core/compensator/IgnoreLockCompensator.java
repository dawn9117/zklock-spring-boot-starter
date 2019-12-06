package dawn.dlock.zklock.core.compensator;

import dawn.dlock.zklock.core.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 失败重试补偿器
 *
 * @author HEBO
 */
@Slf4j
public class IgnoreLockCompensator implements LockFailedCompensator {

	@Override
	public Boolean compensate(LockInfo info) throws Throwable {
		return true;
	}

}
