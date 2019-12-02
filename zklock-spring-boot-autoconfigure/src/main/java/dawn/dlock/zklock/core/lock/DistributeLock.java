package dawn.dlock.zklock.core.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author he.bo
 * @created 2019-11-28 14:27
 */
public interface DistributeLock {

	/**
	 * 获取锁<br>
	 * 若获不到锁,当前线程阻塞,直到获取锁为止<br>
	 * <pre>
	 * 实例如下：
	 * try{
	 *    lock.lock();
	 *    //执行业务代码
	 * }catch(){
	 *    //异常处理
	 * }finally{
	 *    lock.unlock();
	 * }
	 *
	 * </pre>
	 */
	void lock() throws Exception;

	/**
	 * 尝试获取锁<br>
	 * 若获取到锁,返回true,否则返回false<br>
	 * <pre>
	 * 实例如下：
	 * try{
	 *    if(lock.tryLock()){
	 *        //执行成功获取锁业务代码
	 *    }else{
	 *        //执行获取锁失败业务代码
	 *    }
	 * }catch(){
	 *    //异常处理
	 * }finally{
	 *    lock.unlock();
	 * }
	 *
	 * </pre>
	 */
	boolean tryLock() throws Exception;


	/**
	 * 在限定时间内获取锁<br>
	 * 若获取到锁,返回true,否则返回false<br>
	 * <pre>
	 * 实例如下：
	 * try{
	 *    if(lock.tryLock(1000,TimeUnit.MILLISECONDS)){
	 *        //执行成功获取锁业务代码
	 *    }else{
	 *        //执行获取锁失败业务代码
	 *    }
	 * }catch(){
	 *    //异常处理
	 * }finally{
	 *    lock.unlock();
	 * }
	 *
	 * </pre>
	 *
	 * @param time 等待获取锁最大的时间
	 * @param unit 等待获取锁最大的时间单位
	 * @return
	 */
	boolean tryLock(long time, TimeUnit unit) throws Exception;

	/**
	 * 释放锁,与lock(),tryLock()成对使用
	 */
	void unlock() throws Exception;

}
