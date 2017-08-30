import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yaron
 */

public class MyThreadPool {

	/**
	 * This class implements the design pattern of thread pooling
	 */
	private LinkedBlockingQueue<Runnable> _tasks;
	private Thread[] _threadsArray;
	/**
	 * Constructor
	 * @param numOfThreads - The number of threads in the thread pool
	 */
	
	public MyThreadPool(int numOfThreads){
	
		_tasks = new LinkedBlockingQueue<>();
		_threadsArray= new Thread[numOfThreads];
		
		for (int i = 0 ; i<numOfThreads; i++){
			_threadsArray[i]= new MyThreadPoolThread(_tasks);
		}
			
	}
	/**
	 * @param r - The runnable object to add to the queue 
	 */
	public void addRunnableToQueue(Runnable r){
		_tasks.add(r);
		
	}
	
	/**
	 * Run all threads in the pool.
	 */
	public void startThreadPoolActivity(){
		for (int i=0 ; i<this._threadsArray.length; i++)
			_threadsArray[i].run();
	}
	/**
	 * Waits for all threads in the pool to die.
	 */
	public void joinThreadPoolActivity(){
		for (int i=0 ; i<this._threadsArray.length; i++)
			try {
				_threadsArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	
	
	
	public static void main(String[] args) {
		
		boolean play=true;
		
		
		while(play){
			
			Scanner reader = new Scanner(System.in);  
			
			System.out.println("Enter a number of threads [between 2-20]: ");
			int numOfThreads = reader.nextInt(); 
			System.out.println("Enter a number of Matrices [at least 2]: ");
			int quantityOfSquareMatrices = reader.nextInt(); 
			System.out.println("Enter a number of Matrices dimension [at least 1000]: ");
			int matrixDimensions = reader.nextInt(); 
			

			MyThreadPool myThreadPool = new MyThreadPool(numOfThreads);
			LinkedBlockingQueue<SquareMatrix> listOfMatrices= new LinkedBlockingQueue<>();
			
			
			for (int i=0; i< quantityOfSquareMatrices; i++){
				myThreadPool.addRunnableToQueue(new GenerateRandomMatrixRunnable(matrixDimensions,listOfMatrices));
			}
			
			for (int i=0; i < quantityOfSquareMatrices-1; i++){
					myThreadPool.addRunnableToQueue(new MultiplyMatrixRunnable(listOfMatrices));
			}
			
			myThreadPool.startThreadPoolActivity();
			myThreadPool.joinThreadPoolActivity();
			
			
			//System.out.println("result");
			for (SquareMatrix mat: listOfMatrices)
				mat.printMatrix();	
			
			System.out.println("Go again?");
			System.out.println("type in any key or 'q' to exit");
			String goAgain = reader.next(); // Scans the next token 
			if (goAgain.equals("q")){
				reader.close();
				play=false;
				System.out.println("exiting...");
				}
			}
		}
	}
