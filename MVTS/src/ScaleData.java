public class ScaleData {
	
	public static double [][]  scaleData(double [][] arr, int rows)
	{
		
		double [][] scaledArr;
		
		if(rows > Main.rowsPerSample)
			scaledArr = scaleDown(arr, rows);
		
		else if(rows < Main.rowsPerSample)
			scaledArr = scaleUp(arr, rows);
		else
			scaledArr = dontScale(arr);
		
		return scaledArr;
	}

	public static double [][] dontScale(double [][] arr)
	{
		double [][] temp = new double [Main.rowsPerSample][arr[0].length];
		for(int i=0; i<Main.rowsPerSample; i++)
		{
			for(int j=0; j<arr[0].length; j++)
			{
				temp[i][j] = arr[i][j];
			}
		}
		return temp;
	}

	public static double [][] scaleDown(double [][] arr, int rows)
	{
		
		double scaling_factor=((double)(rows-1)/(Main.rowsPerSample-1));
		double [][] temp = new double [Main.rowsPerSample][arr[0].length];
		
		
		for(int x=0; x<Main.rowsPerSample-1; x++) //chosenIndex[x]=x*SampleScaleRows;
		{
			for(int y=0; y<Main.dimensions; y++)
			{
				temp[x][y] = arr[(int)(x*scaling_factor)][y];
			}
		}
		for(int y=0; y<Main.dimensions; y++)
		{
			temp[Main.rowsPerSample-1][y] = arr[rows-1][y];
		}
		
		return temp;
		
	}

	public static double [][] scaleUp(double [][] arr, int rows)
	{
		double [][] temp = new double [Main.rowsPerSample][arr[0].length];
		
		int[] chosenIndex=new int[rows];
		double scaling_factor=((double)(Main.rowsPerSample-1)/(rows-1));
		
		
		for(int x=0;x<rows-1;x++)
		{
			chosenIndex[x]=  (int) (x*scaling_factor);
		
		}
		chosenIndex[rows-1]=Main.rowsPerSample-1;

		
		for(int x=0; x<rows; x++)
		{
			for(int y=0;y<Main.dimensions;y++)
			{
				temp[chosenIndex[x]][y] = arr[x][y];
			}
			
			if( x>0 && (chosenIndex[x]-chosenIndex[x-1])>1)
			{
				double[] interPolatingValues = interPolate (temp, chosenIndex[x-1], chosenIndex[x]);
				for(int m =chosenIndex[x-1]+1; m<chosenIndex[x]; m++ )
				{
					for(int y = 0; y<temp[0].length; y++)
						temp[m][y] = temp[m-1][y] + interPolatingValues[y];
				}
			}		
		}
		
		return temp;
	}

	public static double[] interPolate(double [][] temp ,int low, int high)
	{
		double [] interPolatingValues =new double[temp[0].length];
		
		int diff = high - low;
		
		for(int i = 0; i<temp[0].length; i++)
		{
			interPolatingValues[i] = (temp[high][i] - temp[low][i])/diff;
		}
		return interPolatingValues;
	}
}
