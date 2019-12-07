package dawn.dlock.zklock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * zookeeper lock配置类
 *
 * @author HEBO
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "zklock")
public class LockConfig {

	/**
	 * zklock插件总开关(默认开启)
	 */
	private Boolean enabled = Boolean.TRUE;

	/**
	 * zk配置
	 */
	@NestedConfigurationProperty
	private ZkConfig zk;


}
