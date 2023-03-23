import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver implements IMazeSolver {

	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private boolean[][] visited;

	// direction + stepstaken
	// x - direction - tells at a particular coord(x,y), where does its parent lie - NSEW?
	// y - stepstaken - number of steps a particular card has
	private Point[][] reachable;
	private int[] roomCount;
	private int endRow, endCol;
	private int startRow, startCol;

	private Queue<Point> queue;

	// breadth-first approach
	public MazeSolver() {
		maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
		this.reachable = new Point[maze.getRows()][maze.getColumns()];
		this.roomCount = new int[maze.getRows() * maze.getColumns()];
		this.queue = new LinkedList<>();

		// set all visited flag to false
		// before we begin our search
		for (int i = 0; i < this.maze.getRows(); ++i) {
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				this.reachable[i][j] = new Point(-1, -1);
				maze.getRoom(i, j).onPath = false;
			}
		}

		// set all to 0
		for (int i = 0; i < this.roomCount.length; i++) {
			this.roomCount[i] = 0;
		}
		this.roomCount[0] = 1;

	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		// set all visited flag to false
		// before we begin our search
		for (int i = 0; i < this.maze.getRows(); ++i) {
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				this.reachable[i][j] = new Point(-1, -1);
				maze.getRoom(i, j).onPath = false;
			}
		}

		// set all to 0
		for (int i = 0; i < this.roomCount.length; i++) {
			this.roomCount[i] = 0;
		}
		this.roomCount[0] = 1;

		this.endRow = endRow;
		this.endCol = endCol;
		this.startRow = startRow;
		this.startCol = startCol;

		this.queue = new LinkedList<Point>();

		Point p = new Point(startRow, startCol);
		Point endPoint = solve(p);
		return tracePath(endPoint);
	}

	private boolean canGo(int row, int col, int dir) {
		// not needed since our maze has a surrounding block of wall
		// but Joe the Average Coder is a defensive coder!
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;

		switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !maze.getRoom(row, col).hasWestWall();
		}

		return false;
	}

	// Breadth First Search
	public Point solve(Point p) {
		this.queue.add(p);

		Point finalPoint = new Point(this.endRow,endCol);

		this.reachable[p.x][p.y] = new Point(-1, 0);

		this.visited[p.x][p.y] = true;

		while (!this.queue.isEmpty()) {
			Point currPoint = this.queue.remove();

			int row = currPoint.x;
			int col = currPoint.y;

			// reset count

			if (row == endRow && col == endCol) {
				// Break out of the loop, and trace back to origin
				finalPoint = currPoint;
			}

			// North
			if (canGo(row, col, 0) && !visited[row - 1][col]) {
				this.visited[row - 1][col] = true;
				int sT = 1 + this.reachable[row][col].y;
				this.reachable[row - 1][col] = new Point(1, sT);
				if (sT != -1) {
					this.roomCount[sT] += 1;
				}
				Point nextP = new Point(row - 1, col);
				this.queue.add(nextP);
			}

			// South
			if (canGo(row, col, 1) && !visited[row + 1][col]) {
				this.visited[row + 1][col] = true;
				int sT = 1 + this.reachable[row][col].y;
				this.reachable[row + 1][col] = new Point(0, sT);
				if (sT != -1) {
					this.roomCount[sT] += 1;
				}
				Point nextP = new Point(row + 1, col);
				this.queue.add(nextP);
			}

			// East
			if (canGo(row, col, 2) && !visited[row][col + 1]) {
				this.visited[row][col + 1] = true;
				int sT = 1 + this.reachable[row][col].y;
				this.reachable[row][col + 1] = new Point(3, sT);
				if (sT != -1) {
					this.roomCount[sT] += 1;
				}
				Point nextP = new Point(row, col + 1);
				this.queue.add(nextP);
			}

			// West
			if (canGo(row, col, 3) && !visited[row][col - 1]) {
				this.visited[row][col - 1] = true;
				int sT = 1 + this.reachable[row][col].y;
				this.reachable[row][col - 1] = new Point(2, sT);
				if (sT != -1) {
					this.roomCount[sT] += 1;
				}
				Point nextP = new Point(row, col - 1);
				this.queue.add(nextP);
			}
		}
		return finalPoint;
	}

	public Integer tracePath(Point p) {
		int rooms = 1;
		int directionValue = this.reachable[p.x][p.y].x;

		if (this.startRow == this.endRow && this.startCol == this.endCol) {
			maze.getRoom(this.startRow, this.startCol).onPath = true;
			return 0;
		}

		while (directionValue != -1) {
			directionValue = this.reachable[p.x][p.y].x;
			maze.getRoom(p.x, p.y).onPath = true;
			if (directionValue == 0) { // Go North
				rooms++;
				p = new Point(p.x - 1, p.y);
			} else if (directionValue == 1) { // Go South
				rooms++;
				p = new Point(p.x + 1, p.y);
			} else if (directionValue == 2) { // Go East
				rooms++;
				p = new Point(p.x, p.y + 1);
			} else if (directionValue == 3) { // Go West
				rooms++;
				p = new Point(p.x, p.y - 1);
			} else {
				return rooms - 1;
			}
		}

		return null;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k >= this.roomCount.length) {
			return 0;
		}
		return this.roomCount[k];
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-2by2.txt");
			IMazeSolver solver = new MazeSolver();

			solver.initialize(maze);
			System.out.println(solver.pathSearch(1, 0, 1, 1));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
