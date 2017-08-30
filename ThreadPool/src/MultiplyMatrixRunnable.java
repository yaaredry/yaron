import java.util.concurrent.BlockingQueue;

/**
 * @author yaron
 */
public class MultiplyMatrixRunnable implements Runnable {
	/**
	 * This class extract 2 matrices and multiply those in a runnable form, then adds the product to the shared collection
	 */
	
	private BlockingQueue<SquareMatrix> _listOfMatrices;
	/**
	 * 
	 * @param listOfMatrices -A shared collection of square matrices
	 */
	public MultiplyMatrixRunnable(BlockingQueue<SquareMatrix> listOfMatrices){
		this._listOfMatrices=listOfMatrices;
	}
	/**
	 * Multiply 2 matrices from the collection while removing them, adds the result to the tail.
	 */

	@Override
	public void run() {
		
		SquareMatrix sm1 = _listOfMatrices.poll();
		SquareMatrix sm2 = _listOfMatrices.poll();
		SquareMatrix res = sm1.multiplyMatrices(sm2);
		_listOfMatrices.add(res);

	}

}
