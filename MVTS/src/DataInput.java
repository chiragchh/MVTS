import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Jama.Matrix;


public class DataInput {

	public static ArrayList<DataSet> getTrainData(String fileName){
		
		ArrayList<DataSet> trainData = new ArrayList<DataSet>();
		
		try{
        	FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int i=0,j=0,k=0;

            for(i =0; i<Main.noOfDigits; i++)
            {
            	trainData.add(new DataSet(Main.rowsPerSample, Main.noOfSamplesPerDigit, Main.dimensions));
            	
            	
            	for(j=0; j<Main.noOfSamplesPerDigit; j++)
            	{
            		double [][] temp = new double[Main.maxRowsPerSample][Main.dimensions];
            		
            		k = 0;
            		while (!((strLine = br.readLine()).startsWith(",")))   
                	{
                	   String[] tokens = strLine.split(",");
      
                	   for(int x=0;x<tokens.length;x++)
                	   {
                		   temp[k][x] = Double.parseDouble(tokens[x]);
                	   }
                	   k++;
                	}
            		
            		double[][] scaledTemp = ScaleData.scaleData(temp, k);
            		trainData.get(i).samples[j] = new Matrix(scaledTemp);
            	}
            }
            br.close();
		}
		catch (IOException e) {
            e.printStackTrace();
        }
		return trainData;
	}
	
	public static DataSet getTestData(String fileName){
		
		DataSet testData = new DataSet(Main.rowsPerSample, Main.noOfTestSamples, Main.dimensions);
		try{
			 
        	FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int j=0,k=0;
         
            for(j=0; j<Main.noOfTestSamples; j++)
        	{
        		double [][] temp = new double[Main.maxRowsPerSample][Main.dimensions];
        		
        		k = 0;
        		while (!((strLine = br.readLine()).startsWith(",")))   
            	{
            	   String[] tokens = strLine.split(",");
  
            	   for(int x=0;x<tokens.length;x++)
            	   {
            		   temp[k][x] = Double.parseDouble(tokens[x]);
            	   }
            	   k++;
            	}
        		double[][] scaledTemp = ScaleData.scaleData(temp, k);
        		testData.samples[j] = new Matrix(scaledTemp);
        	}
            br.close();
		}
  
		catch (IOException e) {
            e.printStackTrace();
        }
		return testData;
	}
}
