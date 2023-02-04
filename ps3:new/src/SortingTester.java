import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.security.Key;
import java.util.Random;

public class SortingTester {
    public static boolean checkSort(ISort sorter, int size) {
        if (size == 1) {
            return true;
        }

        Random rand = new Random();
        KeyValuePair[] arr = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            int num = rand.nextInt(10);
            arr[i] = new KeyValuePair(num, 20);
        }
        sorter.sort(arr);

        int prev = arr[0].getKey();

        for (int i = 1; i < size; i++) {
            int curr = arr[i].getKey();
            if (prev > curr) {
                return false;
            }
            prev = curr;
        }
        return true;
    }

    public static boolean isStable(ISort sorter, int size) {
        if (size == 1) {
            return true;
        }
        KeyValuePair[] arr = new KeyValuePair[size];

        arr[0] = new KeyValuePair(5,1);
        arr[1] = new KeyValuePair(5, 2);

        Random rand = new Random();

        for (int i = 2; i < size; i++) {
            int num = rand.nextInt(10);

            while (num == 5) {
                num = rand.nextInt(10);
            }
            arr[i] = new KeyValuePair(num, 3);
        }

        sorter.sort(arr);

        boolean appeared = false;

        for (int i = 0; i < size; i++) {
            if (appeared && arr[i].getValue() == 2) { // preserves stability as 1 then 2
                return true;
            }
            if (arr[i].getValue() == 1) {
                appeared = true;
            }
        }
        return false;
    }

    // Creates an array of KeyValuePairs of length 'size' and key 'num' - values are unique
    public static KeyValuePair[] createArrayOrdered(int num, int size) {
        KeyValuePair[] arr = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new KeyValuePair(num, i);
        }

        return arr;
    }

    // Creates an array of KeyValuePairs of length 'size' with randomised keys, all values are unique
    public static KeyValuePair[] createArrayRandom(int size) {
        KeyValuePair[] arr = new KeyValuePair[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            int num = rand.nextInt(10);
            arr[i] = new KeyValuePair(num, i);
        }

        return arr;
    }

    public static KeyValuePair[] createRunning(int size) {
        KeyValuePair[] arr = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new KeyValuePair(i, i);
        }

        return arr;
    }

    public static KeyValuePair[] createRunningReversed(int size) {
        KeyValuePair[] arr = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new KeyValuePair(size - i, i);
        }

        return arr;
    }

    public static KeyValuePair[] createOneOff(int size) {
        KeyValuePair[] arr = new KeyValuePair[size];

        arr[0] = new KeyValuePair(0, 0);

        for (int i = 1; i < size - 2; i++) {
            arr[i] = new KeyValuePair(i + 1, i);
        }

        arr[size - 2] = new KeyValuePair(1, size - 2);
        arr[size - 1] = new KeyValuePair(size - 1, size - 1);

        return arr;
    }

    public static void runSequence(ISort sort, int size) {
        KeyValuePair[] arr = new KeyValuePair[size];
        KeyValuePair[] arrRandom = createArrayRandom(size);
        KeyValuePair[] arrRunning = createRunning(size);
        KeyValuePair[] arrOrdered = createArrayOrdered(1, size);
        KeyValuePair[] arrOneOff = createOneOff(size);

        System.out.println("checkSort() " + checkSort(sort, size));
        System.out.println("isStable() " + isStable(sort, size));

        System.out.println("Random: " + sort.sort(arrRandom));
        System.out.println("Running: " + sort.sort(arrRunning));
        System.out.println("Ordered: " + sort.sort(arrOrdered));
        System.out.println("OneOff: " + sort.sort(arrOneOff));
    }


    public static void main(String[] args) {
        /* Not Stable - QS, SS
         * Stable - Insert, Bubble, Merge
         *
         * Better on Sorted Arrays - Insert, Bubble
         * Bubblesort will iterate it with n^2 confirmed, but Insertionsort will do so with 2n -> n
         * No Diff in Best/Ave/Worst - Merge
         */

        //-----------------------------------------------
        ISort sortingObject = new SorterB();
        runSequence(sortingObject, 2);
    }
}
