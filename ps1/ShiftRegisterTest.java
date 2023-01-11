import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

/**
 * ShiftRegisterTest
 * @author dcsslg
 * Description: set of tests for a shift register implementation
 */
public class ShiftRegisterTest {
    /**
     * Returns a shift register to test.
     * @param size
     * @param tap
     * @return a new shift register
     */
    ILFShiftRegister getRegister(int size, int tap) {
        return new ShiftRegister(size, tap);
    }

    /**
     * Tests shift with simple example.
     */
    @Test
    public void testShift1() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 1, 1, 0, 0, 0, 1, 1, 1, 1, 0 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.shift());
        }
    }

    /**
     * Tests generate with simple example.
     */
    @Test
    public void testGenerate1() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 6, 1, 7, 2, 2, 1, 6, 6, 2, 3 };

        for (int i = 0; i < 10; i++) {
            assertEquals("GenerateTest", expected[i], r.generate(3));
        }
    }

    /**
     * Tests register of length 1.
     */
    @Test
    public void testOneLength() {
        ILFShiftRegister r = getRegister(1, 0);
        int[] seed = { 1 };
        r.setSeed(seed);
        int[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.generate(3));
        }
    }

    /**
     * Tests with erroneous seed.
     */

    // Problem 2b Answer: The code should throw an error that the seed is larger than the size of the register's
    // allocated array size. The test should expect an error to be thrown, perhaps a custom error message written
    // in the program to flash and say that the initilised register's size does not match the size of the seed.
    @Test
    public void testError() {
        ILFShiftRegister r = getRegister(4, 1);
        int[] seed = { 1, 0, 0, 0, 1, 1, 0 };
        r.setSeed(seed);
        r.shift();
        r.generate(4);
    }

    /**
     * Tests shift functionality with another example
     */
    @Test
    public void testShift2() {
        ILFShiftRegister r = getRegister(8, 4);
        int[] seed = { 1, 1, 0, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 0, 1, 1, 0, 0, 0, 0, 0 };
        for (int i = 0; i < 8; i++) {
            assertEquals(expected[i], r.shift());
        }
    }

    /**
     * Tests generate functionality with another example
     */
    @Test
    public void testGenerate2() {
        ILFShiftRegister r = getRegister(8, 6);
        int[] seed = { 1, 1, 0, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 25, 17, 10, 15, 26, 1, 24, 9 };

        for (int i = 0; i < 8; i++) {
            assertEquals("GenerateTest", expected[i], r.generate(5));
        }
    }

    /**
     * Tests if tap is within bounds of the register
     */
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Test
    public void testTapBounds() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Tap is out of bounds");
        ILFShiftRegister r = getRegister(8, 8);
    }

    /**
     * Tests if seed contains non 0's and 1's
     */
    @Rule
    public ExpectedException expectedEx1 = ExpectedException.none();
    @Test
    public void testValidityOfSeed() throws Exception {
        expectedEx1.expect(RuntimeException.class);
        expectedEx1.expectMessage("Seed contains non 0's or 1's digit");
        ILFShiftRegister r = getRegister(8, 5);
        int[] seed = { 1, 1, 3, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
    }
}
