import java.util.ArrayList;

import Jama.Matrix;


public class Main {

	static int rowsPerSample = 30;
	static int maxRowsPerSample = 200;
	static int noOfSamplesPerDigit = 660;
	static int noOfDigits = 10;
	static int dimensions = 13;
	static int noOfTestSamples = 2200;

	static int r = 5;
	static int s = 10;
	static String trainFile = "Train_Arabic_Digit.csv";
	static String testFile = "Test_Arabic_Digit.csv";
	
	public static void main (String args[])
	{

		System.out.println("Initializing..");System.out.println("");
		long totalTime = System.currentTimeMillis();

		
		System.out.println("-------------Getting Data and Scaling------------------");
		System.out.println("Training Data");
		ArrayList<DataSet> trainData = DataInput.getTrainData(trainFile);
		System.out.println("Testing Data");
		DataSet testData = DataInput.getTestData(testFile);
			
		System.out.println("");
		System.out.println("------------------Applying 2D SVD----------------------");
		Matrix avgMat = PreProc.getAvgMatrix(trainData, testData);
		System.out.println("Average matrix calculated");

		Matrix rowCoVar = PreProc.getCoVar(trainData, testData, avgMat, 0);//0 is for row
		System.out.println("Row-Row Co-Variance Matrix Calculated");
		
		Matrix colCoVar = PreProc.getCoVar(trainData, testData, avgMat, 1);//1 is for col
		System.out.println("Column-Column Co-Variance Matrix Calculated");
			
			
		Matrix F = Eigen.getEigen(rowCoVar);
		System.out.println("Eigen Vector Matrix for Row-Row Co-Variance Calculated");

		Matrix G = Eigen.getEigen(colCoVar);
		System.out.println("Eigen Vector Matrix for Column-Column Co-Variance Calculated");
			

		Matrix Ut = Eigen.getPartition(F, r);
		Ut = Ut.transpose();
		System.out.println("Got Ur");
			
		Matrix V = Eigen.getPartition(G, s);
		System.out.println("Got Vs");
			
		PreProc.getReducedTrainData(Ut, V, trainData);
		System.out.println("Reduced Dimensions of training data");
			
		PreProc.getReducedTestData(Ut, V, testData);
		System.out.println("Reduced Dimensions of testing data");
		
		ArrayList<Matrix> T = new ArrayList<Matrix>();
		
		for (DataSet dataSet : trainData) {
			for (Matrix M : dataSet.samples) {
				T.add(M);
			}
		}
		int counter = 0;
		//ArrayList<Matrix> newT= new ArrayList<Matrix>(T.subList(0, 1000));
				Shapelet.ShapeletCachedSelection(T, 2, 4, 50);
		
		/*for (Matrix matrix : testData.samples) {
			counter++;
			System.out.println(counter);
			matrix.print(matrix.getColumnDimension(), 2);
		}*/
		
		
		
		/*System.out.println("");
		System.out.println("---------------Starting Classification------------------");
		long classificationTime = System.currentTimeMillis();
		int [] resultClasses = OneNearestNeighbour.classify(trainData, testData);
		int count = 0;
		
		for(int i=0; i<resultClasses.length; i++)
			if(resultClasses[i] == i/220)
				count++;
		
		classificationTime = System.currentTimeMillis() - classificationTime;
		totalTime = System.currentTimeMillis() - totalTime;
		System.out.println("Classifition done");
		
		System.out.println("");
		System.out.println("----------------------Results---------------------------");
		System.out.println("Total running time of the program : " + (double)totalTime/1000 + " seconds");
		System.out.println("Correctly classified test samples : " + count + " out of " + noOfTestSamples);
		System.out.println("Accuracy : " + (double)(Math.round(((double)count*100/22)))/100 + "%");	
		System.out.println("Total time taken for classification : " + (double)classificationTime/1000 + " seconds");		
		System.out.println("");
		System.out.println("Exiting..");*/
	}
}
