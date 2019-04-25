import java.util.ArrayList; 
import Jama.Matrix;


public class PreProc {

	public static Matrix getAvgMatrix (ArrayList<DataSet> trainData, DataSet testData)
	{
		int row = testData.samples[0].getRowDimension();
		int col = testData.samples[0].getColumnDimension(); 
		Matrix avgMatrix = new Matrix(row, col);
		double noOfSamples = 0;
		
		//For training data
		DataSet digitData;
		for(int i=0; i< trainData.size(); i++ )	//Take a digit
		{
			digitData = trainData.get(i);
			for(int j=0; j<digitData.samples.length; j++)//Take a sample of that digit
			{
				noOfSamples++;
				Matrix temp = digitData.samples[j];
				avgMatrix = avgMatrix.plusEquals(temp);
			}
		}
		
		//For testing data
		for(int i=0; i< testData.samples.length; i++ )//Take a sample
		{
			noOfSamples++;
			Matrix temp = testData.samples[i];
			avgMatrix = avgMatrix.plusEquals(temp);
		}
		
		//Get Average
		avgMatrix = avgMatrix.timesEquals(1/noOfSamples);
		
		return avgMatrix;
	}
	
	public static Matrix getCoVar (ArrayList<DataSet> trainData, DataSet testData, Matrix avgMatrix, int des)
	{
		
		Matrix coVar;
		double noOfSamples = 0;
		if(des == 0)//rowCoVar
		{
			int row = testData.samples[0].getRowDimension();
			coVar = new Matrix (row, row);
		}
		else
		{
			int col = testData.samples[0].getColumnDimension();
			coVar = new Matrix (col, col);
		}


		//For training data
		DataSet digitData;
		for(int i=0; i< trainData.size(); i++ )	//Take a digit
		{
			digitData = trainData.get(i);
			for(int j=0; j<digitData.samples.length; j++)//Take a sample of that digit
			{
				noOfSamples++;
				//Get SD
				Matrix temp = digitData.samples[j].copy();
				temp = temp.minusEquals(avgMatrix);
				//Get SD transpose
				Matrix tempTranspose = temp.copy();
				tempTranspose = tempTranspose.transpose();
				
				//Get Multiplication
				Matrix newTemp;
				if(des == 0)//RowCoVar
					newTemp = temp.times(tempTranspose);
				else
					newTemp = tempTranspose.times(temp);
				
				//Add multiplication result
				coVar = coVar.plusEquals(newTemp);	
			}
		}
		
		//For test data
		for(int i=0; i< testData.samples.length; i++ )//Take a sample
		{
			noOfSamples++;
			//Get SD
			Matrix temp = testData.samples[i].copy();
			temp = temp.minusEquals(avgMatrix);
			//Get SD transpose
			Matrix tempTranspose = temp.copy();
			tempTranspose = tempTranspose.transpose();
			
			//Get Multiplication
			Matrix newTemp;
			if(des == 0)//RowCoVar
				newTemp = temp.times(tempTranspose);
			else
				newTemp = tempTranspose.times(temp);
			
			
			//Add multiplication result
			coVar = coVar.plusEquals(newTemp);	
		}

		coVar = coVar.timesEquals(1/noOfSamples);
		return coVar;
	}

	
	public static void print (double[][] mat)
	{
		System.out.println("");
		for(int i = 0; i<mat.length; i++)
		{
			for(int j = 0; j<mat[0].length; j++)
				System.out.print(mat[i][j] + " ");
			System.out.println("");
		}
		System.out.println("");
	}
	
	
	public static void getReducedTestData (Matrix Ut, Matrix V, DataSet testData)
	{
		for(int i=0; i<testData.samples.length; i++)
		{	
			Matrix T = testData.samples[i];
			Matrix UtCopy = Ut.copy();
			testData.samples[i] = UtCopy.times(T).times(V);
		}
		return;
	}
	
	public static void getReducedTrainData(Matrix Ut, Matrix V, ArrayList<DataSet> trainData)
	{
		for(int i = 0; i<trainData.size(); i++)
		{
			DataSet temp = trainData.get(i);
			getReducedTestData (Ut, V, temp);
		}
		return;
	}
}
