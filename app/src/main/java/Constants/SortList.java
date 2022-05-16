package Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Models.MissingPersonData;

public class SortList {

    public static void sortByDistance(List<MissingPersonData> list) {
        Collections.sort(list, new Comparator<MissingPersonData>() {
            @Override
            public int compare(MissingPersonData a, MissingPersonData b) {
                double d1 = a.getLocationData().getDistance();
                double d2 = b.getLocationData().getDistance();
                if (d1 > d2) return 1;
                else if (d1 < d2) return -1;
                else return 0;
            }
        });
    }

    public static void sortByName(List<MissingPersonData> list) {
        Collections.sort(list, new Comparator<MissingPersonData>() {
            @Override
            public int compare(MissingPersonData a, MissingPersonData b) {
                return a.getName().compareTo(b.getName());
            }
        });
    }

}
