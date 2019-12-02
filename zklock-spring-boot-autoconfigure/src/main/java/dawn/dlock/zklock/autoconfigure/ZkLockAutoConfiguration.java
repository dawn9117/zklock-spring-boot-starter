package dawn.dlock.zklock.autoconfigure;

import dawn.dlock.zklock.aspect.ZkLockAspect;
import dawn.dlock.zklock.config.ZkConfig;
import dawn.dlock.zklock.core.FailedStrategySelector;
import dawn.dlock.zklock.core.LockHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * zk自动配置类
 *
 * @author hebo
 */
@Configuration
@Import({FailedStrategySelector.class, ZkLockAspect.class})
@EnableConfigurationProperties(ZkConfig.class)
public class ZkLockAutoConfiguration {

	@Bean(initMethod = "init")
	@ConditionalOnMissingBean(LockHelper.class)
	public LockHelper lockHelper() {
		return new LockHelper();
	}
}
