package dawn.dlock.zklock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * zk配置
 * <p>
 * ##### 分布式锁zk配置 #####
 * zk.registry.address=zk:2181
 * zk.lock.namespace=locks
 * zk.connect.timeout=30000
 * zk.session.timeout=30000
 *
 * @author HEBO
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "zk")
public class ZkConfig {

	private String registryAddress;

	private String lockNamespace = "locks";

	private Long connectTimeout = 30000L;

	private Long sessionTimeout = 30000L;

}
