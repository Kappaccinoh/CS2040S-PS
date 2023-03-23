import java.util.Random;
public class MazeGenerator {

	private MazeGenerator() { }

	private static Random random;
	private static int[][] grid;


	public static Maze generateMaze(int rows, int columns, int seed) {
		grid = new int[rows][columns];
		random = new Random(seed);

		// legend
		// 0 - wall
		// 1 - path


		// Every index will either be a 0 or a 1,
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				// random binary chance - 0 = wall, 1 = no wall
				int chance = random.nextInt(2);
				if (chance == 1) {
					grid[i][j] = 1;
					System.out.println(" ");
				} else {
					grid[i][j] = 0;
					System.out.println("#");
				}
			}
			System.out.println("");
		}

		return null;
	}
}
