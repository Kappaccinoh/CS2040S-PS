///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

import java.util.Arrays;

/**
 * class ShiftRegister
 * @author
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    int[] register_array;
    int size;
    int tap;

    ShiftRegister(int size, int tap) {
        if (tap > size - 1) {
            try {
                throw new Exception("Tap is out of bounds");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.size = size;
        this.tap = tap;
        this.register_array = new int[size];
    }

    /**
     * setSeed
     * @param seed
     */
    @Override
    public void setSeed(int[] seed) {
        this.register_array = new int[seed.length];
        for (int i = seed.length - 1; i > -1; i--) {
            if (seed[seed.length - 1 - i] != 0 && seed[seed.length - 1 - i] != 1) {
                try {
                    throw new Exception("Seed contains non 0's or 1's digit");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            this.register_array[i] = seed[seed.length - 1 - i];
        }
        System.out.println("Successfully setSeed");
    }

    /**
     * shift
     * @return
     */
    @Override
    public int shift() {
        int msb = this.register_array[0];
        int tap_pos = this.size - this.tap - 1;
        int tap_b = this.register_array[tap_pos];
        int arr_length = this.size;

        int lsb = msb ^ tap_b;

        // shifting register array down by one element
        for (int j = 0; j < arr_length - 1; j++) {
            this.register_array[j] = register_array[j + 1];
        }

        // adding least significant digit based on XOR logic
        this.register_array[arr_length - 1] = lsb;

//        System.out.println("array:");
//        System.out.println(Arrays.toString(this.register_array));
//        System.out.println("lsb");
//        System.out.println(lsb);

        return lsb;
    }

    /**
     * generate
     * @param k
     * @return
     */
    @Override
    public int generate(int k) {
        int[] int_array = new int[k];

        for (int i = 0; i < k; i++) {
            int_array[i] = shift();
        }

//        System.out.println(toDecimal(int_array));
        return toDecimal(int_array);
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // [0,0,0,0] -> "0000"
        String binaryString = "";
        for (int i : array) {
            binaryString = binaryString.concat(Integer.toString(i));
        }
        return Integer.parseInt(binaryString, 2);
    }
}
