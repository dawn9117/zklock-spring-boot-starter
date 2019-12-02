package dawn.dlock.zklock.core;

import com.alibaba.fastjson.JSON;
import dawn.dlock.zklock.config.ZkConfig;
import dawn.dlock.zklock.core.lock.DistributeLock;
import dawn.dlock.zklock.core.lock.LockInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Constructor;

/**
 * 分布式锁帮助类
 */
@Slf4j
@Getter
public class LockHelper {

	/**
	 * ZK配置类
	 */
	@Resource
	private ZkConfig zkConfig;

	/**
	 * zk 客户端
	 **/
	private CuratorFramework client;

	@PostConstruct
	public void init() {
		log.info("[ZkDistributeLockFactoryImpl] init zkConfig:{}", JSON.toJSONString(zkConfig));
		if (StringUtils.isEmpty(zkConfig.getRegistryAddress())) {
			throw new RuntimeException("[ZkDistributeLockFactoryImpl] 服务器地址不能为空");
		}
		client = CuratorFrameworkFactory.builder().connectString(zkConfig.getRegistryAddress())
				.connectionTimeoutMs(zkConfig.getConnectTimeout().intValue())
				.sessionTimeoutMs(zkConfig.getSessionTimeout().intValue())
				.namespace(zkConfig.getLockNamespace())
				.retryPolicy(new RetryNTimes(10, 1000)).build();
		client.start();
		log.info("[ZkDistributeLockFactoryImpl] 初始化zk客户端成功");
	}

	/**
	 * 获取分布式锁(默认zk不可重入共享锁)
	 *
	 * @param lockName  应用context
	 * @param lockClass 锁类型
	 * @return zk锁
	 */
	public DistributeLock build(String lockName, Class<? extends DistributeLock> lockClass) {
		try {
			Constructor<? extends DistributeLock> constructor = lockClass.getConstructor(CuratorFramework.class, String.class);
			constructor.setAccessible(true);
			return constructor.newInstance(client, lockName);
		} catch (Exception e) {
			log.error("[LockHelper] build lock exception", e);
			return null;
		}
	}

	public Boolean lock(LockInfo lockInfo) {
		try {
			if (BooleanUtils.isTrue(lockInfo.getIsLocked())) {
				return Boolean.TRUE;
			}
			if (lockInfo.getLock() == null) {
				DistributeLock lock = build(lockInfo.getLockName(), lockInfo.getZkLock().type());
				lockInfo.setLock(lock);
			}
			return lockInfo.getLock() != null && lockInfo.getLock().tryLock(lockInfo.getZkLock().timeout(), lockInfo.getZkLock().timeunit());
		} catch (Exception e) {
			log.warn("[LockHelper] lock failed", e);
			return Boolean.FALSE;
		}
	}

}
