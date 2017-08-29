import java.util.concurrent.BlockingQueue;

/**
 * @author yaron
 */
public class GenerateRandomMatrixRunnable implements Runnable {
/**
 * This class generates random square matrix in a runnable form	
 */

	private int _dimensions;
	private BlockingQueue<SquareMatrix> _listOfMatrices;
	
	/**
	 * Constructor
	 * @param dimensions - the square matrix dimensions
	 * @param listOfMatrices - A shared collection of square matrices
	 */
	public GenerateRandomMatrixRunnable(int dimensions,BlockingQueue<SquareMatrix> listOfMatrices) {
		this._dimensions=dimensions;
		this._listOfMatrices=listOfMatrices;
	
	}

	/**
	 * Generates random square matrix and adds it to the shared collection
	 */
	@Override
	public void run() {
		SquareMatrix newSquareMatrix = new SquareMatrix(this._dimensions);
		newSquareMatrix.initializeWithRandomNumbers();
		_listOfMatrices.add(newSquareMatrix);

	}

}
