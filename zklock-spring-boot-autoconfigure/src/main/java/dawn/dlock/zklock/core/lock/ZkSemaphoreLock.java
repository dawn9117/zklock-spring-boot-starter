package dawn.dlock.zklock.core.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;

import java.util.concurrent.TimeUnit;

/**
 * zk 实现分布式锁  http://colobu.com/2014/12/12/zookeeper-recipes-by-example-2/
 * <p>
 * 一个计数的信号量类似JDK的Semaphore。 JDK中Semaphore维护的一组许可(permits)，而Curator中称之为租约(Lease)。
 * 有两种方式可以决定semaphore的最大租约数。第一种方式是有用户给定的path决定。第二种方式使用SharedCountReader类。
 * 如果不使用SharedCountReader, 所有的实例必须使用相同的numberOfLeases值.
 * <p>
 * 每次调用acquire会返回一个租约对象。 客户端必须在finally中close这些租约对象，否则这些租约会丢失掉。
 * 但是，如果客户端session由于某种原因比如crash丢掉， 那么这些客户端持有的租约会自动close，
 * 这样其它客户端可以继续使用这些租约。
 */
@Slf4j
public class ZkSemaphoreLock implements DistributeLock {

	private InterProcessSemaphoreV2 lock;

	private Lease lease;

	public ZkSemaphoreLock(CuratorFramework client, String keyName) {
		if (client == null || StringUtils.isEmpty(keyName)) {
			log.error("[ZkSemaphoreLock] client 或者 keyName 不能为空 client={},keyName={}", client, keyName);
			throw new RuntimeException("[ZkSemaphoreLock] client 或者 keyName 不能为空");
		}
		keyName = StringUtils.prependIfMissing(keyName, "/", "/");
		lock = new InterProcessSemaphoreV2(client, keyName, 1);
	}

	public void lock() throws Exception {
		lease = lock.acquire();
	}

	public boolean tryLock() throws Exception {
		lease = lock.acquire(1, TimeUnit.MICROSECONDS);
		return true;
	}

	public boolean tryLock(long time, TimeUnit unit) throws Exception {
		lease = lock.acquire(time, unit);
		return true;
	}

	public void unlock() {
		lock.returnLease(lease);
	}

}