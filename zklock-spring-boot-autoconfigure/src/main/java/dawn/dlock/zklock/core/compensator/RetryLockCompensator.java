package dawn.dlock.zklock.core.compensator;

import dawn.dlock.zklock.core.LockHelper;
import dawn.dlock.zklock.core.lock.LockInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 失败重试补偿器
 *
 * @author HEBO
 */
@Slf4j
public class RetryLockCompensator implements LockFailedCompensator {

	@Autowired
	private LockHelper helper;

	@Override
	public Boolean compensate(LockInfo info) throws Throwable {
		for (int i = 0; i < info.getZkLock().retry(); i++) {
			if (helper.lock(info)) {
				return true;
			}
			TimeUnit.SECONDS.sleep(1);
		}
		return false;
	}

}
