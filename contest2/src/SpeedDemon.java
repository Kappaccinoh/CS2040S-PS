import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Hashtable;

public class SpeedDemon {
    Hashtable<String, Integer> ht;

    /**
     * Returns the number of pairs of entries containing an identical multiset,
     * as described in the pdf.
     *
     * @param filename name of the file containing the input
     * @return number of pairs
     */
    public int processData(String filename){
        try {
            FileReader dataFile = new FileReader(filename);
            BufferedReader bufferedDataFile = new BufferedReader(dataFile);
            Hashtable<String, Integer> hashtable = new Hashtable<>();
            this.ht = hashtable;

            boolean first = true;

            while (bufferedDataFile.ready()) {
                String line = bufferedDataFile.readLine();

                if (first) {
                    first = false;
                } else {
//                    System.out.println(line);
                    String sorted = SortString(line);
                    System.out.println(sorted);
                    populate(sorted);
                }
            }

            return calculate();

        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public void populate(String line) {
        this.ht.put(line, this.ht.getOrDefault(line, 0) + 1);
    }

    public int calculate() {
        int sum = 0;
        for (Integer value : this.ht.values()) {
            if (value != 1) {
                sum += roll(value);
            }
        }
        return sum;
    }

    public int roll(int num) {
        int sum = 0;
        for (int i = num - 1; i >= 0; i--) {
            sum += i;
        }
        return sum;
    }

    public String SortString(String s) {
        char charArray[] = s.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    // DO NOT EDIT this method for contest submission, as it will used by the grader
    public static void main(String[] args){
        SpeedDemon dataProcessor = new SpeedDemon();
        int answer = dataProcessor.processData(args[0]);    // Expects input file name as CLI argument
        System.out.println(answer);
    }
}
