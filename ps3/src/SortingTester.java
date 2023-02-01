import java.security.Key;
import java.util.Random;

public class SortingTester {
    public static boolean checkSort(ISort sorter, int size) {
        Random rand = new Random();
        KeyValuePair[] arr = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            int num = rand.nextInt(10);
            arr[i] = new KeyValuePair(num, 20);
        }
        sorter.sort(arr);
        int prev = arr[0].getKey();
        for (int i = 0; i < size; i++) {
            int curr = arr[i].getKey();
            if (prev > curr) {
                return false;
            }
            prev = curr;
        }
        return true;
    }

    public static boolean isStable(ISort sorter, int size) {
        KeyValuePair[] arr = new KeyValuePair[size];

        for (int i = 0; i < size; i++) {
            arr[i] = new KeyValuePair(1, i);
        }

        sorter.sort(arr);

        for (int i = 0; i < size; i++) {
            if (arr[i].getValue() != i) {
                return false;
            }
        }
        return true;
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





    public static void main(String[] args) {
        // A: Merge
        // B: Quick
        // C: Insert/Bubble
        // D: Selection?
        // E: Dr Evil
        // F: Insert/Bubble

        // Selection has to be D

        // QuickSort, SelectionSort - not stable algos

        // Insertion/Bubble - best for one or two misplaced elements -> compare performance
        // of arrays completely randomised vs completely ordered, ordered should perform
        // significantly better

        // Idea: Insertion vs Bubblesort for an almost fully sorted array -> 1,2,3,4,6,8,7
        // Bubblesort will iterate it with n^2 confirmed, but Insertionsort will do so with 2n -> n

//        KeyValuePair[] testArray = new KeyValuePair[8];
//        testArray[0] = new KeyValuePair(1, 100);
//        testArray[1] = new KeyValuePair(9, 100);
//        testArray[2] = new KeyValuePair(5, 100);
//        testArray[3] = new KeyValuePair(13, 100);
//        testArray[4] = new KeyValuePair(3, 100);
//        testArray[5] = new KeyValuePair(11, 100);
//        testArray[6] = new KeyValuePair(7, 100);
//        testArray[7] = new KeyValuePair(15, 100);


        // MergeSort - Sorter A (sort true, stable true):
        // Random(20) - 10304, 9656, 9548, 9656
        // Ordered(20) - 9440, 10736, 9872, 11060
        // No diff between Random and Ordered, likely not Insertion Sort

        // Try Mergesort, no diff in bestcase, averagecase, worst case -> all O(nlogn)
        // worstcase(8) - 3680, 3616
        // averagecase(8) - 3680
        // bestcase(8) - 3584

        // MergeSort Scaling -> seems to echo O(nlogn)
        // Random(10) - 4980
        // Random(100) - 73 368
        // Random(1000) - 1 010 592
        // Random(10000) - 12 208 160







        // QuickSort - Sorter B (sort true, stable false): 2500 - 2600 (QuickSort or SelectionSort?)
        // Ordered(50) - 4600, 4900, 4700, 4500
        // Random(50) - 13450, 13832, 12100, 16521

        // QuickSort - O(nlogn), SelectionSort - O(n^2) -> compare performance as n scales
        // Ordered(10) - 1340, 1280, 1340, 1280
        // Ordered(50) - 4500, 5100, 4700, 4900
        // Ordered(100) - 9800, 8800, 9600, 9400
        // Ordered(500) - 43400, 41400, 42400, 40400
        // Ordered(1000) - 82400, 92400, 90400, 84400

        // Random(10) - 3281, 3524, 3220
        // Random(50) - 13 450, 13 832, 12 100, 16 521
        // Random(100) - 21 433, 24 925, 27 126
        // Random(500) - 122 170, 170 186, 114 375
        // Random(1000) - 305 194, 238 737, 287 040






        // Bubble - Sorter C (sort true, stable true): - 1500 - 1600
        // Ordered(1, 50) - 3540, 3687, 3883, 3834
        // Running(50) - 3736, 4030, 3932, 3589
        // Random(50) - 120552, 154950, 131185, 113888

        // OneOff(10) - 5136, 5392, 5736
        // OneOff(100) - 640932, 670038
        // OneOff(1000) - 60817722, 67796736
        // clearly n^2






        // Selection - Sorter D (sort true, stable true/false): - 1700 - 1800 (by elimination this should be selection)
        // Selection - O(n^2) in all cases

        // Ordered(1, 50) - 90125, 86450, 102375, 96250
        // Running(50) - 90125, 87675, 88900, 93800
        // Random(50) - 87675, 86450, 98700, 91350

        // Ordered(1,100) - 376900, 357100, 367000
        // Running(100) - 396700, 391750, 396700
        // Random(100) - 376900, 367000, 347200

        // Ordered(1,1000) - 39960700, 38961700, 39960700
        // Running(1000) - 35465200, 40959700, 40460200
        // Random(1000) - 37962700, 37463200, 40959700





        // DR EVIL - Sorter E (sort true, stable false/true?) - -> might be selectionsort - O(n^2)?
        // sometimes stable sometimes unstable...
        // sortingcost makes no sense - more expensive to sort ordered arrays? performs better with random arrays

        // Ordered(1, 50) - 12000, 11600, 12100
        // Random(50) - 29662, 33528, 32752

        // Sorted vs Unsorted Arrays
        // Running(10) - 14 054, 11 914, 14 570
        // Running(100) - 218 854, 182 584, 233 323
        // Running(1000) - 3 311 223, 2 964 800, 2 517 200

        // RunningReversed(10) - 15 848, 14 284, 15 847
        // RunningReversed(100) - 163 792, 212 904, 192 637
        // RunningReversed(1000) - 2 576 103, 2 795 869, 2 601 694

        // Ordered(1, 10) - 3000, 3240, 3280,
        // Ordered(1, 100) - 24 800, 24 200, 24 600
        // Ordered(1, 1000) - 219 000, 211 000, 231 000

        // Random(10) - 4540, 4892, 4452, 4408
        // Random(100) - 55 484, 61 280, 57 175
        // Random(1000) -589 300, 604 316, 497 100





        // Insertion/Bubble - Sorter F (sort true, stable true): - 500 - 600
        // Ordered(1, 50) - 1770, 1819, 2015, 1917
        // Running(50) - 1819, 2015, 1770, 1819
        // Random(50) - 20729, 22509, 18549, 17790, 26165

        // OneOff(10) - 844, 860, 854
        // OneOff(100) - 6180, 6500, 6340
        // OneOff(1000) - 64172, 68700, 66168
        // OneOff(10000) - 660168, 680164, 673520
        // Seems linear, 10x increase in n results in roughly a 10x increase in cost

        //-------------------------------------------------------------------------------------

        KeyValuePair[] testArray = createOneOff(1000);
        ISort sortingObject = new SorterC();
        System.out.println("checkSort() " + checkSort(sortingObject, 1000));
        System.out.println("isStable() " + isStable(sortingObject, 1000));
        System.out.println(sortingObject.sort(testArray));

    }
}
