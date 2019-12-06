package dawn.dlock.zklock.core;

import dawn.dlock.zklock.core.compensator.DefaultFailedCompensator;
import dawn.dlock.zklock.core.compensator.IgnoreLockCompensator;
import dawn.dlock.zklock.core.compensator.RetryLockCompensator;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 失败补偿器注册
 *
 * @author HEBO
 */
public class FailedCompensatorSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return new String[]{DefaultFailedCompensator.class.getName(), IgnoreLockCompensator.class.getName(), RetryLockCompensator.class.getName()};
	}
}
