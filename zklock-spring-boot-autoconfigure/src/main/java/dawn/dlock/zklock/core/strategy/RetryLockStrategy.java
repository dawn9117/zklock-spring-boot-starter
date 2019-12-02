package dawn.dlock.zklock.core.strategy;

import dawn.dlock.zklock.core.LockHelper;
import dawn.dlock.zklock.core.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 失败重试策略
 *
 * @author HEBO
 * @created 2019-11-29 11:03
 */
@Slf4j
public class RetryLockStrategy implements LockFailedStrategy {

	@Autowired
	private LockHelper helper;

	@Override
	public Boolean doFailed(LockInfo info) throws Throwable {
		for (int i = 0; i < info.getZkLock().retry(); i++) {
			if (helper.lock(info)) {
				return true;
			}
		}
		return false;
	}

}
