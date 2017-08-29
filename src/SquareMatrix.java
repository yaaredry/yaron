import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * @author yaron
 */

public class SquareMatrix {
	/**
	 * Lower and Higher bounds for values within the matrices
	 */
	
	final static int UPPER_BOUND = 10;
	final static int LOWER_BOUND = 0;
	
	private int dimensions;
	private long[][] squareMatrix;
	/**
	 * Constructor
	 * @param dimensions - Square matrix dimensions
	 */
	public SquareMatrix(int dimensions){
		this.dimensions=dimensions;
		this.squareMatrix= new long[dimensions][dimensions];
	}
	
	/**
	 * Constructor
	 * @param squareMatrixToCreate - Square matrix to create the class with.
	 */
	public SquareMatrix(long[][] squareMatrixToCreate){
		this.dimensions=squareMatrixToCreate.length;
		this.squareMatrix= squareMatrixToCreate;
	}
	/**
	 * Initialize the square matrix instance with random numbers between higher and lower bounds
	 */
	public void initializeWithRandomNumbers(){
		Random random= new Random();
		for (int i = 0; i < this.dimensions; i++)
			for (int j = 0; j < this.dimensions; j++)
				this.squareMatrix[i][j] = random.nextInt(UPPER_BOUND - LOWER_BOUND +1)+ LOWER_BOUND;
		
	}
	/**
	 * Print the matrix
	 */
	public void printMatrix(){
		int n= getDimensions();
		try{
		 PrintWriter writer = new PrintWriter("outputMatrix.txt", "UTF-8");
		 for (int i = 0; i < n ; i++) {
			 for (int j = 0; j < n; j++) 
				 writer.printf("%20d", this.squareMatrix[i][j]); //"%12d" to allow printing in a nice format for all integers
			 writer.println();
		}
		 writer.println();
		 writer.close();
		 System.out.println("Matrix was printed to file outputMatrix.txt\n");
		 
		}catch(IOException e){
			System.out.println("IO exception");
		}
	}
	/**
	 * 
	 * @param otherMat - matrix to multiply with
	 * @return  The multiplication product of the matrix * otherMat
	 */
	public SquareMatrix multiplyMatrices(SquareMatrix otherMat){
		int n=otherMat.getDimensions();
		long[][] resultMat = new long[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				for (int k = 0; k < n; k++)
					resultMat[i][j] += this.getElement(i, k)* otherMat.getElement(k,j);
		
		return new SquareMatrix(resultMat);
	}
	
	public long getElement(int row, int column){
		return this.squareMatrix[row][column];
	}
	
	public int getDimensions(){
		return this.dimensions;
	}

}
