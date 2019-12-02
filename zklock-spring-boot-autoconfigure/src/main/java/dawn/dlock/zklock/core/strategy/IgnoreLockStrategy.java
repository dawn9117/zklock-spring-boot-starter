package dawn.dlock.zklock.core.strategy;

import dawn.dlock.zklock.core.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 失败重试策略
 *
 * @author HEBO
 * @created 2019-11-29 11:03
 */
@Slf4j
public class IgnoreLockStrategy implements LockFailedStrategy {

	@Override
	public Boolean doFailed(LockInfo info) throws Throwable {
		return true;
	}

}
