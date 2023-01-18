/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        if (jobSizes.length == 0) {
            return false;
        }
        int p_count = 1;
        int sum = 0;
        for (int i : jobSizes) {
            if (i > queryLoad) {
                return false;
            } else {
                if (sum + i > queryLoad) {
                    p_count++;
                    sum = i;
                } else {
                    sum += i;
                }
            }
        }
        if (sum > queryLoad || p_count > p) {
            return false;
        }

        return true;
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        if (jobSizes.length == 0 || p < 1) {
            return -1;
        }

        int sum = 0;
        int largest = 0;
        for (int i : jobSizes) {
            if (i > largest) {
                largest = i;
            }
            sum += i;
        }
        // lower bound is the largest elem in the arr
        // upper bound is the sum of the elements in the arr
        int lower = largest;
        int higher = sum;
        int mid = (lower + higher) / 2;

        while (higher - lower > 0) {
            System.out.println(mid);
            if (isFeasibleLoad(jobSizes, mid, p)) {
                higher = mid;
                mid = (higher + lower) / 2;
            } else {
                lower = mid + 1;
                mid = (higher + lower) / 2;
            }
        }
        return mid;
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
