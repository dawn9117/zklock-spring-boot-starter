package dawn.dlock.zklock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * zk配置
 * <p>
 * ##### 分布式锁zk配置 #####
 * zk.registry.address=zk:2181
 * zk.lock.namespace=/locks
 * zk.connect.timeout=30000
 * zk.session.timeout=15000
 *
 * @author HEBO
 * @created 2019-11-28 10:14
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ZkConfig.ZK_PREFIX)
public class ZkConfig {

	public static final String ZK_PREFIX = "zk";

	private String registryAddress;

	private String lockNamespace;

	private Long connectTimeout;

	private Long sessionTimeout;

}
