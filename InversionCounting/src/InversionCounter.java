import java.util.Arrays;
import java.util.HashMap;

class InversionCounter {

    public static long countSwaps(int[] arr) {
        return merge(arr, 0, arr.length - 1);
    }

    public static long merge(int[] arr, int l, int r) {
        int half;
        long inv_cnt = 0;
        if (r > l) {
            half = (r + l) / 2;
            inv_cnt = inv_cnt + merge(arr, l, half);
            inv_cnt = inv_cnt + merge(arr, half + 1, r);

            inv_cnt = inv_cnt + mergeAndCount(arr, l, half, half + 1, r);
        }
        return inv_cnt;
    }

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.
     */
    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        if (arr == null) {
            return 0;
        }
        if (arr.length == 1) {
            return 0;
        }

        if (arr.length == 2) {
            if (arr[0] < arr[1]) {
                return 0;
            } else {
                return 1;
            }
        }

        int[] left = Arrays.copyOfRange(arr, left1, right1 + 1);

        int[] right = Arrays.copyOfRange(arr, left2, right2 + 1);

        int i = 0;
        int j = 0;
        int k = left1;
        long swapCount = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
                swapCount += right1 + 1 - left1 - i;
            }
        }

        while (j < right.length) {
            arr[k++] = right[j++];
        }

        while (i < left.length) {
            arr[k++] = left[i++];
        }

        return swapCount;
    }
}