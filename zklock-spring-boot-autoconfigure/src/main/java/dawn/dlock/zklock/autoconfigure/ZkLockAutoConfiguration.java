package dawn.dlock.zklock.autoconfigure;

import dawn.dlock.zklock.aspect.ZkLockAspect;
import dawn.dlock.zklock.config.LockConfig;
import dawn.dlock.zklock.core.FailedCompensatorSelector;
import dawn.dlock.zklock.core.LockHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
@EnableConfigurationProperties(LockConfig.class)
@ConditionalOnExpression("${zklock.enabled:true}")
@Import({FailedCompensatorSelector.class, ZkLockAspect.class})
public class ZkLockAutoConfiguration {

	@Bean(initMethod = "init")
	@ConditionalOnMissingBean(LockHelper.class)
	public LockHelper lockHelper(LockConfig lockConfig) {
		LockHelper helper = new LockHelper();
		helper.setZkConfig(lockConfig.getZk());
		return helper;
	}

}
