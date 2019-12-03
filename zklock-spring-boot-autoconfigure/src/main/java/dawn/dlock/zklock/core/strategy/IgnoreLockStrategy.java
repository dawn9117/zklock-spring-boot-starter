package dawn.dlock.zklock.core.strategy;

import dawn.dlock.zklock.core.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 失败重试策略
 *
 * @author HEBO
 */
@Slf4j
public class IgnoreLockStrategy implements LockFailedStrategy {

	@Override
	public Boolean doFailed(LockInfo info) throws Throwable {
		return true;
	}

}
