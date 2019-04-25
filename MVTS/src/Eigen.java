import Jama.EigenvalueDecomposition;
import Jama.Matrix;


public class Eigen {

	public static Matrix getEigen (Matrix mat)
	{
		EigenvalueDecomposition eg = mat.eig();
		
		double [] eigenValues = eg.getRealEigenvalues();
		double[][] eigenVectors = eg.getV().getArray();
		
		//Sort the eigen Values
		for(int i=0; i<eigenValues.length; i++)
			if(eigenValues[i] < 0)
				eigenValues[i] *= -1;

		int [] sortingHelp = new int [eigenValues.length];
		for(int i=0; i<sortingHelp.length; i++)
			sortingHelp[i] = i;
		
		mergeSort(eigenValues, sortingHelp, 0, eigenValues.length);
		
		//Sort Eigen Vectors
		double [][] newEigenVectors = new double [eigenVectors.length][eigenVectors[0].length];
		for(int i=0; i<eigenValues.length; i++)
		{
			int col = sortingHelp[i];
			for(int j=0; j<eigenVectors.length; j++)
			{
				newEigenVectors[j][i] = eigenVectors[j][col];
			}
		}

		Matrix M = new Matrix(newEigenVectors);
		return M;
	}
	
	
	
	public static void mergeSort(double [] eigenValues, int [] sortingHelp, int low, int high)
	{
		if(low+1 >= high)
			return;
		int mid = (low+high)/2;
		mergeSort(eigenValues, sortingHelp, low, mid);
		mergeSort(eigenValues, sortingHelp, mid, high);
		merge(eigenValues, sortingHelp, low, mid, high);
		return;
	}
	
	public static void merge(double [] eigenValues, int [] sortingHelp, int low, int mid, int high)
	{

		double [] temp = new double [high];
		int tempMid = mid, tempLow = low;
		int [] tempHelp = new int [high];
		int counter = 0;
	
		while(tempLow < mid && tempMid < high)
		{
			if(eigenValues[tempLow] >= eigenValues[tempMid])
			{
				tempHelp[counter] = sortingHelp[tempLow];
				temp[counter] = eigenValues[tempLow];
				counter++;tempLow++;
			}
			else
			{
				tempHelp[counter] = sortingHelp[tempMid];
				temp[counter] = eigenValues[tempMid];
				counter++;tempMid++;
			}
		}
		
		if(tempMid == high)
		{
			while(tempLow < mid)
			{
				tempHelp[counter] = sortingHelp[tempLow];
				temp[counter] = eigenValues[tempLow];
				counter++;tempLow++;
			}
		}
		
		if(tempLow == mid)
		{
			while(tempMid < high)
			{
				tempHelp[counter] = sortingHelp[tempMid];
				temp[counter] = eigenValues[tempMid];
				counter++;tempMid++;
			}
		}
		
		for(int i=0; i<counter; i++)
		{
			eigenValues[low+i] = temp[i];
			sortingHelp[low+i] = tempHelp[i];
		}
		return;
	}
	
	
	public static Matrix getPartition (Matrix orig, int reqCols)
	{
		double [][] newMat = new double [orig.getRowDimension()][reqCols];
		double [][] origMat = orig.getArrayCopy();
		
		for(int i=0; i<orig.getRowDimension(); i++)
		{
			for(int j = 0; j<reqCols; j++)
			{
				newMat[i][j] = origMat[i][j];
			}
		}
		
		Matrix M = new Matrix(newMat);
		return M;
	}
}
