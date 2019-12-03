package dawn.dlock.zklock.exception;

/**
 * 异常
 *
 * @author HEBO
 */
public class ZkLockException extends Exception {

	public ZkLockException() {
	}

	public ZkLockException(String message) {
		super(message);
	}

	public ZkLockException(Throwable cause) {
		super(cause);
	}

	public ZkLockException(String message, Throwable cause) {
		super(message, cause);
	}
}
