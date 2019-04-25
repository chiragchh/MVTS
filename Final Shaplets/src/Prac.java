import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.omp4j.*;

import Jama.Matrix;

public class Prac {

	public static void main(String[] args) {
		File finalfile = new File("finalShapelets_0.txt");
		FileReader fileReader;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(finalfile);
			bufferedReader = new BufferedReader(fileReader);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			System.out.println(" final shapelets");
			String line;
			int row = 0;ArrayList<Double> d = new ArrayList<>();int rows=0;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				
				if (line.contains("**") || line.contains("SeriesNo")){
					d=new ArrayList<Double>();rows=0;continue;
				}
					
				else if (line.contains("Quality")) {
					System.out.println(line);
					StringTokenizer st = new StringTokenizer(line, ": ");
					while (st.hasMoreTokens()) {
						String word = (String) st.nextElement();
						if (word.contains(".")) {
							double Quality = Double.parseDouble(word);
							break;
						}
					}
					continue;
				}
				else if (line.contains(".") && !line.contains(":")) {
				
					StringTokenizer st = new StringTokenizer(line, " ");
					
					while (st.hasMoreTokens()) {
						String word = (String) st.nextElement();
						d.add(Double.parseDouble(word));
					}
				}
				
			}
			
		} catch (Exception e) {
		}

	}

}
