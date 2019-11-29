package dawn.dlock.zklock.core.strategy;

import dawn.dlock.zklock.core.lock.LockInfo;
import dawn.dlock.zklock.core.strategy.LockFailedStrategy;
import org.springframework.stereotype.Component;

/**
 * 默认加锁失败策略
 *
 * @author HEBO
 * @created 2019-11-29 11:03
 */
@Component
public class DefaultFailedStrategy implements LockFailedStrategy {

	@Override
	public Boolean doFailed(LockInfo lockInfo) throws Throwable {
		return false;
	}

}
