public class Guessing {

    // Your local variables here
    private int low = 0;
    private int high = 1000;

    int var_guess = 1000;
    boolean flag = false;

    /**
     * Implement how your algorithm should make a guess here
     */
    public int guess() {
        if (flag) {
            low = var_guess + 1;
            var_guess = (high + low) / 2;
        } else {
            high = var_guess;
            var_guess = (high + low) / 2;
        }
        System.out.println(var_guess);
        return var_guess;
    }

    /**
     * Implement how your algorithm should update its guess here
     */
    public void update(int answer) {
        if (answer == -1) {
            flag = true;
        } else {
            flag = false;
        }
    }
}
