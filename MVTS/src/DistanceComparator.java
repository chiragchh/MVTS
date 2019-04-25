

import java.util.Comparator;

public class DistanceComparator implements Comparator<DistanceStructure> {

	@Override
	
	public int compare(DistanceStructure o1, DistanceStructure o2) {
		// TODO Auto-generated method stub
		if (o1.distance > o2.distance) {
			return 1;
		} else if (o1.distance == o2.distance) {

			return 0;
		} else {
			return -1;
		}
	}

}
