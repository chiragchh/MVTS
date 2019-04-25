

import java.util.Comparator;

public class QualityComparator implements Comparator<QualityShapelet>{

	
	public int compare(QualityShapelet o1, QualityShapelet o2) {
		// TODO Auto-generated method stub
		if(o1.Quality>o2.Quality){
			return 1;
		}
		else if(o1.Quality==o2.Quality){
			return 0;
		}
		else{
			return -1;
		}
	}

}
