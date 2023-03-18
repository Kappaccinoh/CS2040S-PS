import java.util.HashMap;
import java.util.Random;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	// Model each kgram has its own probability table with keys_NextCharacter and values_Probability
	private Map<String, Map<String, Integer>> model;
	private int order;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		Map<String, Map<String,Integer>> model = new HashMap<String, Map<String, Integer>>();
		this.model = model;
		this.order = order;

		// Initialize the random number generator
		this.generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		for (int i = 0; i < text.length() - this.order; i++) {
			// get a kgram
			String kgram = text.substring(i, i + this.order);

			// get probability table for this particular kgram
			Map<String, Integer> kgramTable = this.model.get(kgram);
			if (kgramTable == null) {
				kgramTable = new HashMap<String, Integer>();
			}

			// get character after kgram in question
			String currChar = text.substring(i + this.order, i + this.order + 1);

			// search for curr char and update probability table
			Integer frequency = kgramTable.get(currChar);
			if (frequency == null) {
				kgramTable.put(currChar, 1);
			}  else {
				frequency += 1;
				kgramTable.put(currChar, frequency);
			}

			// update model
			this.model.put(kgram, kgramTable);
		}

	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() != this.order) {
			return 0;
		}

		Map<String, Integer> kgramTable = this.model.get(kgram);
		if (kgramTable == null) {
			return 0;
		}
		Integer frequency = 0;
		for (Integer i : kgramTable.values()) {
			frequency += i;
		}
		return frequency;
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		Map<String, Integer> kgramTable = this.model.get(kgram);
		String s = String.valueOf(c);
		Integer frequency = kgramTable.get(s);
		if (frequency == null) {
			return 0;
		} else {
			return frequency;
		}
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		Integer arraySize = getFrequency(kgram);
		if (arraySize == 0) {
			return NOCHARACTER;
		}
		String[] probabilityArray = new String[arraySize];

		Map<String, Integer> kgramTable = this.model.get(kgram);
		Integer index = 0;

//		System.out.println(kgramTable);

		List<String> sortedKeys = new ArrayList<String>(kgramTable.keySet());
		Collections.sort(sortedKeys);

//		System.out.println(kgramTable);

		for (String x : sortedKeys) {
			String currChar = x;
			int frequency = kgramTable.get(x);
			while (frequency > 0) {
				probabilityArray[index] = currChar;
				frequency--;
				index++;
			}
		}

		int rngIndex = this.generator.nextInt(arraySize);
		return probabilityArray[rngIndex].charAt(0); // return as char
	}
}
