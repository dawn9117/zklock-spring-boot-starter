package dawn.dlock.zklock.core;

import dawn.dlock.zklock.core.strategy.DefaultFailedStrategy;
import dawn.dlock.zklock.core.strategy.IgnoreLockStrategy;
import dawn.dlock.zklock.core.strategy.RetryLockStrategy;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 失败策略注册器
 *
 * @author HEBO
 * @created 2019-12-02 11:01
 */
public class FailedStrategySelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return new String[]{DefaultFailedStrategy.class.getName(), IgnoreLockStrategy.class.getName(), RetryLockStrategy.class.getName()};
	}
}
