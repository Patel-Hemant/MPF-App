package Constants;

public class DistanceCalculator {
    private static final double TO_METERS = 6_371_008.7714D; // equatorial radius
    public static final double TO_RADIANS = Math.PI / 180D;

    public static double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371; // Radius of the earth in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // distance in km

        return d;
    }

    public static double getDistanceFromLatLonInKm2(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }


    public static double getDistanceFromLatLonInKm3(double lat1, double lon1, double lat2, double lon2) {
        double key = haversinSortKey(lat1, lon1, lat2, lon2);
        return haversinMeters(key);
    }

    public static double haversinMeters(double sortKey) {
        return TO_METERS * 2 * Math.asin(Math.min(1, Math.sqrt(sortKey * 0.5)));
    }

    public static double haversinSortKey(double lat1, double lon1, double lat2, double lon2) {
        double x1 = lat1 * TO_RADIANS;
        double x2 = lat2 * TO_RADIANS;
        double h1 = 1 - Math.cos(x1 - x2);
        double h2 = 1 - Math.cos(Math.toRadians(lon1 - lon2));
        double h = h1 + Math.cos(x1) * Math.cos(x2) * h2;
        // clobber crazy precision so subsequent rounding does not create ties.
        return Double.longBitsToDouble(Double.doubleToRawLongBits(h) & 0xFFFFFFFFFFFFFFF8L);
    }

    private static double degToRad(double deg) {
        return deg * (Math.PI / 180);
    }


}
