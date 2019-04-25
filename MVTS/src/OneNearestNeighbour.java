import java.util.ArrayList;

import Jama.Matrix;


public class OneNearestNeighbour {

	static double MAX = 999999999999999999999999999999999999999999999999.0;
	
	public static int [] classify(ArrayList<DataSet> trainData, DataSet testData)
	{
		int [] resultClasses = new int [testData.samples.length];
		
		for(int i=0; i<testData.samples.length; i++)//Take a sample
		{
			double[] dist = new double [trainData.size()];
			
			for(int j=0; j<trainData.size(); j++)//Pick a digit to get min Dist
			{
				DataSet digit = trainData.get(j);
				double [] distPerSample = new double [digit.samples.length];
				for(int k=0; k<distPerSample.length; k++)
					distPerSample[k] = MAX;
				
				for(int k=0; k<digit.samples.length; k++)
				{
					distPerSample[k] = getDistance(testData.samples[i], digit.samples[k]);
				}

				dist[j] = distPerSample[minIndex(distPerSample)];
			}
			resultClasses[i] = minIndex(dist);
		}
		return resultClasses;
	}

	public static double getDistance (Matrix A, Matrix B)
	{
		Matrix Ac = A.copy();
		Matrix Bc = B.copy();
		
		Ac = Ac.minus(Bc);
		double [][] mat = Ac.getArray();
		
		double dist = 0;
		for(int i=0; i<Ac.getColumnDimension(); i++)
		{
			double sum = 0;
			for(int j=0; j<Ac.getRowDimension(); j++)
			{
				sum += (mat[j][i]) * (mat[j][i]);
			}
			dist += Math.sqrt(sum);
		}

		return dist;
	}

	public static int minIndex (double [] arr)
	{
		int minIndex = 0;
		for(int i=0; i<arr.length; i++)
		{
			if(arr[i] < arr[minIndex])
				minIndex = i;
		}
		return minIndex;
	}
}
