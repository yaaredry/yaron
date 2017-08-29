import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yaron
 */

public class MyThreadPoolThread extends Thread {
/**
 * This Thread Class is dedicated to thread pool class
 */
	private LinkedBlockingQueue<Runnable> _queue;
	
	/**
	 * 
	 * @param queue - Tasks queue which contains all runnable objects
	 */
	public MyThreadPoolThread(LinkedBlockingQueue<Runnable> queue) {
		this._queue=queue;
		
	}
	
	public void run(){
		
		try{
		while (!(_queue.isEmpty())){
			Runnable task;
			while ((task=_queue.poll()) != null){
				task.run();
			}
		}
		Thread.sleep(1);
		//prevent cpu exhaustion
		}
		catch (RuntimeException | InterruptedException e){
			throw new MyThreadPoolException(e);
		}
	}

}
