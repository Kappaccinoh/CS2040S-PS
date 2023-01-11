import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

/**
 * ShiftRegisterAdditionalTests
 * @author dcsslg
 * Description: Testing used for problem 2d
 */
public class ShiftRegisterAdditionalTests {
    static ILFShiftRegister getRegister(int size, int tap) {
        return new ShiftRegister(size, tap);
    }
    public static void main(String[] args) {
        // PART 1
        // Different Taps produce different cycles - in this case Tap 2 performed the best with seed 0, 1, 0, 1, 1

        /*
        ILFShiftRegister r = getRegister(5,2);
        int[] seed = { 0, 1, 0, 1, 1};
        r.setSeed(seed);

        // Takes about 18 cycles
        for (int i = 0; i < 50; i++) {
            System.out.println(r.shift());
        }
         */

        /*
        ILFShiftRegister r = getRegister(5,3);
        int[] seed = { 0, 1, 0, 1, 1};
        r.setSeed(seed);

        // Takes 7 cycles
        for (int i = 0; i < 50; i++) {
            System.out.println(r.shift());
        }
         */

        /*
        ILFShiftRegister r = getRegister(5,4);
        int[] seed = { 0, 1, 0, 1, 1};
        r.setSeed(seed);

        // Produced all 0s
        for (int i = 0; i < 50; i++) {
            System.out.println(r.shift());
        }
         */

        // PART 2
        // Convert text string to binary, which will be used as seed
        String input = "TheCowJumpedOverTheMoon";
        String output = "";

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            String charAsASCIIString = Integer.toString((int) c);
            output = output + charAsASCIIString;
        }

        // output is a binary string -> format to array int and pass through as seed


        // PART 3
        // No not true, it depends on the tap and the seed configuration

    }

}
