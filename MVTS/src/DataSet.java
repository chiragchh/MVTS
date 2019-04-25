import Jama.Matrix;

public class DataSet {

	int rowsPerSample, noOfSamplesPerDigit, dimensions;//IMP! make maxRowsPerSample as final number of common Rows
	Matrix [] samples;
	DataSet (int rowsPerSample, int noOfSamplesPerDigit, int dimensions)
	{
		this.rowsPerSample = rowsPerSample;
		this.noOfSamplesPerDigit = noOfSamplesPerDigit;
		this.dimensions = dimensions;
		samples = new Matrix [noOfSamplesPerDigit];
	}
}
