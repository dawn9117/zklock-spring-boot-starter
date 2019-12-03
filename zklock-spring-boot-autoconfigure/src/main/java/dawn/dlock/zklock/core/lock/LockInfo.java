package dawn.dlock.zklock.core.lock;

import dawn.dlock.zklock.anntation.ZkLock;
import lombok.Data;

import java.io.Serializable;

/**
 * 加锁的方法信息
 *
 * @author HEBO
 */
@Data
public class LockInfo implements Serializable {

	/**
	 * 锁标识: 是否已经加锁成功
	 */
	private Boolean isLocked;

	/**
	 * 锁对象
	 */
	private DistributeLock lock;

	/**
	 * 锁名称
	 */
	private String lockName;

	/**
	 * 方法注解
	 */
	private ZkLock zkLock;

}
