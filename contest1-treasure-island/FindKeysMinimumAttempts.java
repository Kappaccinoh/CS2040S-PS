import java.util.Arrays;

public class FindKeysMinimumAttempts implements IFindKeys {
    @Override
    public int[] findKeys(int N, int k, ITreasureExtractor treasureExtractor) {
        int num_keys_left = k;
        int[] key_array = new int[N];
        for (int i = 0; i < N; i++) {
            key_array[i] = 1;
        }

        // go back to the guessing number game where the number can only go up
        int lower_bound = 0;
        int upper_bound = N;
        int highest = (upper_bound + lower_bound) / 2;
        while (num_keys_left > 0) {
            while (upper_bound - lower_bound > 0) {

                System.out.println("Upper " + upper_bound);
                System.out.println("Lower " + lower_bound);
                System.out.println(Arrays.toString(key_array));

                highest = (upper_bound + lower_bound) / 2;
                for (int i = 0; i < upper_bound; i++) {
                    if (i <= highest) {
                        key_array[i] = 1;
                    } else {
                        key_array[i] = 0;
                    }

                }
                if (treasureExtractor.tryUnlockChest(key_array)) {
                    upper_bound = highest; // decrease the highest number (1-100 -> 50 passes, so try 1-50 -> 25)
                    highest = (upper_bound + lower_bound / 2);
                } else {
                    lower_bound = highest; // increase the highest number (1-100 -> 50 fails, so try 50 - 100 -> 75)
                    highest = (upper_bound + lower_bound / 2);
                }
            }
            System.out.println("NEW ARRAY " + Arrays.toString(key_array));
            System.out.println(highest);

            // gotten highest number
            for (int i = 0; i < highest; i++) {
                key_array[i] = 0;
            }
            key_array[highest - 1] = 1;
            // reset
            lower_bound = 0;
            upper_bound = highest - 1;
            num_keys_left--;

        }
        return key_array;
    }






















//    @Override
//    public int[] findKeys(int N, int k, ITreasureExtractor treasureExtractor) {
//        // try true and false method - log2 comes from the true and false, not the partitioning of the array
//        int num_keys_left = k;
//        int[] key_array = new int[N];
//        // populate the array first assuming all keys work
//        for (int i = 0; i < N; i++) {
//            key_array[i] = 1;
//        }
//        int low = 0;
//        int high = N;
//        int mid;
//
//        while (num_keys_left > 0) {
//            while (high - low > 1) {
//                mid = (high + low) / 2;
//                System.out.println("low " + low);
//                System.out.println("high " + high);
//                System.out.println("arr " + Arrays.toString(key_array));
//                if (treasureExtractor.tryUnlockChest(key_array)) {
//                    System.out.println("true");
//                    for (int i = mid + 1; i < high; i++) {
//                        key_array[i] = 0;
//                    }
//                    high = mid;
//                } else {
//                    System.out.println("false");
//                    for (int i = low; i < high; i++) {
//                        key_array[i] = 1;
//                    }
//                    low = mid;
//                }
//            }
//            // find the lowest key
//            low = 0;
//            high -= 1;
//            num_keys_left--;
//            System.out.println("ARRAY BELOW");
//            System.out.println(Arrays.toString(key_array));
//            System.out.println("HIGH BELOW");
//            System.out.println(high);
//        }
//        return key_array;
////        int[] ans = {0,1,0,0,0,1};
////        System.out.println(treasureExtractor.tryUnlockChest(ans));
////        return ans;
//    }
}
