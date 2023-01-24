import java.util.Arrays;

class WiFi {
    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        if (houses.length == 0) {
            return 0;
        }
        Arrays.sort(houses);
        double max = houses[houses.length - 1] - houses[0];
        double min = 0;
        double mid = (max + min) / 2.0;
        while (max - min > 0.5) {
            if (coverable(houses, numOfAccessPoints, mid)) {
                max = mid;
                mid = (max + min) / 2.0;
            } else {
                min = mid;
                mid = (max + min) / 2.0;
            }
        }
        return roundToHalf(mid);
    }

    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        if (houses.length == 0 || numOfAccessPoints >= houses.length) {
            return true;
        } else if (distance == 0 || numOfAccessPoints == 0) {
            return false;
        } else {

        }

        int[] arr = houses;
        double full = distance * 2.0;
        int num = numOfAccessPoints;
//        Arrays.sort(arr);
        int diff = 0;
        for (int index = arr.length - 1; index > 0; index--) {
            if (num < 1) {
                return false;
            }
            diff += arr[index] - arr[index - 1];
            if (diff > full) {
                diff = 0;
                num--;
            }
        }
        if (num < 1) {
            return false;
        }

        return true;
    }
}
