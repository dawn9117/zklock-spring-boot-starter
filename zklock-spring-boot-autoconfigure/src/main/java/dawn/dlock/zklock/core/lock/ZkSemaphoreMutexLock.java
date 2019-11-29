package dawn.dlock.zklock.core.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

import java.util.concurrent.TimeUnit;

/**
 * 默认使用该锁
 * zk 实现分布式锁  http://colobu.com/2014/12/12/zookeeper-recipes-by-example-2/
 * 不可重入共享锁,同一时刻一个线程只能获取一次,第二次以上没有被释放会被阻塞
 */
@Slf4j
public class ZkSemaphoreMutexLock implements DistributeLock {

	private InterProcessSemaphoreMutex lock;

	public ZkSemaphoreMutexLock(CuratorFramework client, String keyName) {
		if (client == null || StringUtils.isEmpty(keyName)) {
			log.error("[ZkSemaphoreMutexLock] client 或者 keyName 不能为空 client={},keyName={}", client, keyName);
			throw new RuntimeException("client 或者 keyName 不能为空");
		}
		keyName = StringUtils.prependIfMissing(keyName, "/", "/");
		lock = new InterProcessSemaphoreMutex(client, keyName);
	}

	public void lock() throws Exception {
		lock.acquire();
	}

	public boolean tryLock() throws Exception {
		return lock.acquire(0, TimeUnit.MICROSECONDS);
	}

	public boolean tryLock(long time, TimeUnit unit) throws Exception {
		return lock.acquire(time, unit);
	}

	public void unlock() throws Exception {
		lock.release();
	}



}