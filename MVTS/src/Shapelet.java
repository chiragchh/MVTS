

import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import Jama.Matrix;

public class Shapelet {
	
	static HashMap<Double, Subsequence> kbestShapes = new  HashMap<Double, Subsequence>();
	static ArrayList<QualityShapelet> kbestShapelets = new ArrayList<QualityShapelet>();
	static ArrayList<ArrayList<QualityShapelet>> individualkbest = new ArrayList<ArrayList<QualityShapelet>>();
	
	static ArrayList<Matrix> T = new ArrayList<Matrix>();
	static int count=0;
	public static void ShapeletCachedSelection(ArrayList<Matrix> T, int min, int max,int k) {
		ArrayList<Subsequence> kbestSubsequencesofTi = null;
		ArrayList<Matrix> Ds = new ArrayList<Matrix>();
		for (int i = 0; i < T.size(); i++) {
			individualkbest.add(new ArrayList<QualityShapelet>());
		}
			
		System.out.println("------starting-----");
		// omp parallel for threadNum(10)	
		
		
		ExecutorService executorService = Executors.newFixedThreadPool(10); // number of threads
		
		for (int i = 0; i < T.size(); i++) 
		{
			
			final int counter = i;
			executorService.submit(new Runnable() 
			{
				@Override
				public void run() 
				{
			           //do your long stuff and can use count variable
			    
				
				
				
						System.out.println("------starting----- " + (counter+1) );
						HashMap<Integer, ArrayList<Subsequence>> subsequenceofL = generateCanditate(T.get(counter),min,max, counter);
						ArrayList<QualityShapelet> shapelets = new ArrayList<QualityShapelet>();
						
						ArrayList<Subsequence> WL = new ArrayList<Subsequence>();
						for (Integer l : subsequenceofL.keySet()) {
							WL.addAll(subsequenceofL.get(l));
						}
					
						// add elements to WL, including duplicates
						Set<Subsequence> hs = new HashSet<Subsequence>();
						hs.addAll(WL);
						WL.clear();
						WL.addAll(hs);
						//System.out.println("printing shapelets");
						for (int j = 0 ; j < WL.size();j++){
							Subsequence subsequence = WL.get(j);
							Matrix DSshapelet = new Matrix(1, T.size());
							Matrix m = subsequence.Sequence;
								
							DSshapelet = findDistance(WL.get(j), T).copy();count++;
							//DSshapelet.print(DSshapelet.getColumnDimension(), 2);
							Double Quality = assessCandidate(subsequence, DSshapelet);
							
							QualityShapelet q = new QualityShapelet(subsequence, Quality);
							shapelets.add(q);
							
						}
					
						ArrayList<QualityShapelet> sortedshapelets = new ArrayList<QualityShapelet>();
						shapelets.sort(new QualityComparator());
						sortedshapelets = shapelets;
						if(counter<=100)
						printShapeletsinFile(sortedshapelets,counter,"OVERLAPPING");
						sortedshapelets = removeSelfSimilar(sortedshapelets);
						if(counter<=100)
						sortedshapelets.sort(new QualityComparator());
						printShapeletsinFile(sortedshapelets,counter,"NON-OVERLAPPING");
						int n ;
						if(shapelets.size()<k){
							n= shapelets.size();
						}
						else{n=k;}
						ArrayList<QualityShapelet> q = new ArrayList<QualityShapelet>(shapelets.subList(0, n));
						individualkbest.get(counter).addAll(q);
				
				}

				
			});
			
			
			//executorService.shutdown();
			
		}
		
		executorService.shutdown();
		try {
			executorService.awaitTermination(15,TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Hello");
		
	
		
		for (ArrayList<QualityShapelet> kbest : individualkbest) {
			kbestShapelets.addAll(kbest);
		}
		kbestShapelets.sort(new QualityComparator());
		printShapeletsinFile(kbestShapelets, 0, "final");
		ArrayList<QualityShapelet> q = new ArrayList<QualityShapelet>(kbestShapelets.subList(0, k));
		kbestShapelets = q;
		System.out.println("best shapelets");
		for (int i = 0; i < kbestShapelets.size(); i++) {
			Matrix m = kbestShapelets.get(i).s.Sequence;
			System.out.println("from series " + kbestShapelets.get(i).s.seriesNo);
			m.print(m.getColumnDimension(), 2);
		}
		return;
	}
	private static Matrix findDistance(Subsequence subsequence, ArrayList<Matrix> T) {
		Matrix DSshapelet = new Matrix(1, T.size());
		for (int i = 0; i < T.size(); i++) {
			//System.out.println("finding distance for " + i);
			DSshapelet.set(0, i,findDistanceSingle(subsequence,T.get(i)));
		}
		
		// TODO Auto-generated method stub
		return DSshapelet;
	}
	private static double findDistanceSingle(Subsequence subsequence, Matrix matrix) {
		// TODO Auto-generated method stub
		ArrayList<Subsequence> WL = new ArrayList<Subsequence>();
		int l = subsequence.Sequence.getRowDimension()-1;
		double min = 100000.00;
		Matrix sub = subsequence.Sequence;
		for (int i = 0; i < matrix.getRowDimension()-l; i++) {
			Matrix ss = matrix.getMatrix(i, i + l, 0, matrix.getColumnDimension()-1).copy();
			//System.out.println("cadidates for comparison");
			//ss.print(ss.getColumnDimension(), 2);
			Subsequence s = new Subsequence(ss, i, i+l,0);
			s.intialindex = i;
			s.finalindex= i+l;
			WL.add(s);
		}
		Matrix s1 = subsequence.Sequence.copy();
		//System.out.println("+++++ before normalization ++++");
		//s1.print(s1.getColumnDimension(), 2);
		s1 = normalizeMatrix(s1);
		//System.out.println("+++++ after normalization ++++");
		//s1.print(s1.getColumnDimension(), 2);
		for (int i = 0; i < WL.size(); i++) {
			Matrix s2 = WL.get(i).Sequence.copy();
			s2= normalizeMatrix(s2);
			Matrix s5 = s1.minus(s2).copy();
			Matrix s3 = s5.copy().transpose();
			Matrix s4 = s5.times(s3);
			//s4.print(s4.getColumnDimension(), 2);
			
			double distance=s4.trace();
			if(distance < min){
				min =distance;
			}
			}
		return min;
		
	}
	private static Matrix normalizeMatrix(Matrix m) {
		// TODO Auto-generated method stub
	
		//m.print(m.getColumnDimension(), 2);
		
		Matrix normalized = m.copy();
		for (int i = 0; i < normalized.getColumnDimension(); i++) {
			double max =0.00;
			for (int j = 0; j < normalized.getRowDimension(); j++) {
				if(Math.abs(m.get(j, i))>max){
					max = Math.abs(m.get(j, i));
				}
			}
			for (int j = 0; j < normalized.getRowDimension(); j++) {
				
					normalized.set(j, i, m.get(j, i)/max);
			}
			//System.out.println("max is " + max);
		}
		
	return normalized;
		
	}
	private static HashMap<Integer, ArrayList<Subsequence>> generateCanditate(Matrix matrix, int min, int max,int seriesNo) {
		// TODO Auto-generated method stub
		HashMap<Integer, ArrayList<Subsequence>> subsequenceofL = new HashMap<Integer,ArrayList<Subsequence>>();
		ArrayList<Subsequence> WL = new ArrayList<Subsequence>();
		for (int l = min; l <= max; l++) {
			ArrayList<Subsequence> WLi = new ArrayList<Subsequence>();
			for (int i = 0; i < matrix.getRowDimension()-l+1; i++) {
				//System.out.println("int i " + i + " int end " +(i+l-1));
				Matrix subsequence = matrix.getMatrix(i, i + l-1, 0, matrix.getColumnDimension()-1).copy();
				Subsequence s = new Subsequence(subsequence, i, i+l,seriesNo);
				s.intialindex = i;
				s.finalindex= i+l;
				WLi.add(s);
			}
			
			WL.addAll(WLi);
			subsequenceofL.put(l, WL);
			
		}
		
		
		return subsequenceofL;
		
			
	}
	private static ArrayList<QualityShapelet> removeSelfSimilar(ArrayList<QualityShapelet> sortedshapelets) {
		// TODO Auto-generated method stub
		HashMap<Double,Subsequence> shapelets = new HashMap<Double,Subsequence>();
		
		for (int i = 0; i < sortedshapelets.size(); i++) {
			for (int j = i+1; j < sortedshapelets.size(); j++) {
				QualityShapelet s1 = sortedshapelets.get(i);
				QualityShapelet s2 = sortedshapelets.get(j);
				if((s2.s.intialindex<=s1.s.finalindex && s1.s.intialindex<=s2.s.intialindex) || (s1.s.intialindex<=s2.s.finalindex && s2.s.finalindex<=s1.s.finalindex)){
					sortedshapelets.remove(s2);
					j--;
				}
			}
		}		
	return sortedshapelets;
	}

	private static void printShapeletsinFile(ArrayList<QualityShapelet> sortedshapelets, int seriesNo,String name) {
		// TODO Auto-generated method stub
		boolean flag=true;
		if(name.equals("final")){
			flag=false;
		}
		
		String filename = name+"Shapelets_"+seriesNo;
		File file1 = new File(filename + ".txt");
		FileWriter writer1=null;
		try {
			writer1 = new FileWriter(file1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (int i = 0; i < sortedshapelets.size(); i++) {
			
			try {
				QualityShapelet Q = sortedshapelets.get(i);
				Matrix ShapeletMatrix = Q.s.Sequence;
				
				writer1.write("\n********** SHAPELET NO #"+i+"**********\n");
				writer1.write("\nQuality of Shapelet : " + Q.Quality+"\n");
				if(flag)
				writer1.write("\nThe SeriesNo : " + Q.s.seriesNo+"\n\n");
				for (int j = 0; j < ShapeletMatrix.getRowDimension(); j++) {
					for (int j2 = 0; j2 < ShapeletMatrix.getColumnDimension(); j2++) {
						Double d = ShapeletMatrix.get(j, j2);
						DecimalFormat df = new DecimalFormat("#.##");
						df.setRoundingMode(RoundingMode.CEILING);	
						writer1.write(df.format(d)+" ");
					}
					writer1.write("\n");
				}
			} catch (Exception e4) {
				System.out.println("not able to write in " + name);
			};
			
			
		}

		try {
			writer1.flush();writer1.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static double assessCandidate(Subsequence subsequence, Matrix dSROW) {
		// TODO Auto-generated method stub
		double[] m = dSROW.getColumnPackedCopy();
		DistanceStructure[] M = new DistanceStructure[m.length];
		
		int sampleclass = Main.noOfSamplesPerDigit;
		for (int i = 0; i < m.length; i++) {
			DistanceStructure d = new DistanceStructure(m[i],i/sampleclass);
			M[i]=d;
		}
		Arrays.sort(M,new DistanceComparator());
		double HDs = entropy(M);
		double[] ds = m.clone();
		double[] as = new double[ds.length];
		double[] bs = new double[ds.length];
		Arrays.sort(m);
		
		//System.out.println("printing for entorpy " + HDs);
		//dSROW.print(dSROW.getColumnDimension(), 2);
		double IGs =0.00;
		// omp parallel for
		for (int i = 0; i < ds.length-1; i++) {
			if(ds[i]==ds[i+1]){
				continue;
			}
			int AS =i;
			int BS=i+1;
			int DS = M.length;
			double IG = HDs-( ((double)AS/(double)DS) * entropy(Arrays.copyOfRange(M, 0, AS+1)) + ((double)(DS-BS)/(double)DS) * entropy(Arrays.copyOfRange(M, (int)BS, (int)DS)) );
			if(IG>IGs){
				IGs = IG;
			}
		}
		return IGs;
	}

	private static double entropy(DistanceStructure[] m) {
		// TODO Auto-generated method stub
		double HDs = 0.00;
		double[] classcount = new double[10000];
		double[] prob = new double[m.length];
		for (int i = 0; i < m.length; i++) {
				classcount[m[i].classno]++;	
		}
		//System.out.println("class counts");
		for (int i = 0; i < classcount.length; i++) {
			//System.out.println(classcount[i]);
			if(classcount[i]!=0)
		HDs+=(classcount[i]/m.length)*Math.log((classcount[i]/m.length));	
			
		}
		HDs=-HDs;
		return HDs;
	}
	
	

	
}
