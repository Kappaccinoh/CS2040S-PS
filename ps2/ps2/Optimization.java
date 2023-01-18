/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83}
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */

    // General idea is to determine if the array is ascending (peak) or descending (valley),
    // then apply the correct algorithm. If peak then use an algorithm similar to binary search,
    // if descending, then compare just the ends of the array which is just 1 comparison. Therefore,
    // the runtime of this algorithm is O(log(n)).
    public static int searchMax(int[] dataArray) {

        if (dataArray.length == 1) {
            return dataArray[0];
        } else if (dataArray.length == 2) {
            return Math.max(dataArray[0], dataArray[1]);
        } else if (dataArray.length == 0){
            return 0;
        }

        // Determine if array is ascending or descending
        boolean ascending = true;
        if (dataArray[0] > dataArray[1]) {
            ascending = false;
        }

        // Ascending
        if (ascending) {
            int low = 0;
            int high = dataArray.length - 1;
            int mid = (high + low) / 2;
            int curr = dataArray[mid];

            while (high - low > 0) {
//                System.out.println(curr);
                if ((curr > dataArray[mid - 1] && (mid + 1 == dataArray.length))
                        || (curr > dataArray[mid + 1] && (mid - 1 == -1))
                        || (curr > dataArray[mid + 1] && curr > dataArray[mid - 1])) {
                    return curr;
                } else if (curr > dataArray[mid + 1]) {
                    high = mid;
                    mid = (high + low) / 2;
                    curr = dataArray[mid];
                } else {
                    low = mid + 1;
                    mid = (high + low) / 2;
                    curr = dataArray[mid];
                }
            }

            return curr;

        } else {
            int max = dataArray.length - 1;
            return Math.max(dataArray[0], dataArray[max]);
        }
    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
