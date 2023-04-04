import java.util.LinkedList;
//import java.util.PriorityQueue;
import java.util.Queue;
import java.awt.Point;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static final int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};
	private Maze maze;
//	private Node[][] nodes;
	private int[][][] parent;
	private int[][][] distance;
	private boolean[][][] visited;
	private int[] roomCount;
//	private int[][][] stepsTaken;
//	private int endRow, endCol, startRow, startCol;
	private int endRow, endCol;
	private Node initialNode;
	private Queue<Node> queue;
	private boolean found;

	class Node {
		int x;
		int y;
		int powersLeft;
		int distance;

		public Node(int x, int y, int powersLeft, int distance) {
			this.x = x;
			this.y = y;
			this.powersLeft = powersLeft;
			this.distance = distance;
		}

//		@Override
//		public int compareTo(Node n) {
//			if (this.distance < n.distance) {
//				return -1;
//			} else if (this.distance > n.distance) {
//				return 1;
//			} else {
//				return 0;
//			}
//		}
	}

	public MazeSolverWithPower() {
		this.maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.parent = new int[maze.getRows()][maze.getColumns()][maze.getRows() * maze.getColumns()];
		this.visited = new boolean[maze.getRows()][maze.getColumns()][maze.getRows() * maze.getColumns()];
		this.distance = new int[maze.getRows()][maze.getColumns()][maze.getRows() * maze.getColumns()];
		this.roomCount = new int[maze.getRows() * maze.getColumns()];
//		this.stepsTaken = new int[maze.getRows()][maze.getColumns()][maze.getRows() * maze.getColumns()];

		// set all to 0
		for (int i = 0; i < this.roomCount.length; i++) {
			this.roomCount[i] = 0;
		}
		this.roomCount[0] = 1;
		this.found = false;
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

		for (int i = 0; i < this.maze.getRows(); ++i) {
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				for (int h = 0; h < this.maze.getRows() * this.maze.getColumns(); h++) {
					this.visited[i][j][h] = false;
					this.parent[i][j][h] = -1;
					this.distance[i][j][h] = -1;
//					this.stepsTaken[i][j][h] = -1;
				}
			}
		}

		// set all to 0
		for (int i = 0; i < this.roomCount.length; i++) {
			this.roomCount[i] = 0;
		}
		this.roomCount[0] = 1;

		Node startingNode = new Node(startRow, startCol, 0, 0);
		this.initialNode = startingNode;
		this.endRow = endRow;
		this.endCol = endCol;

		this.queue = new LinkedList<>();
		this.found = false;

		Node p = Dijkstra(startRow, startCol, 0);
		if (p == null) {
			return null;
		}
		return tracePath(p.x, p.y, p.powersLeft);
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < this.maze.getRows(); ++i) {
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				for (int h = 0; h < this.maze.getRows() * this.maze.getColumns(); h++) {
					this.visited[i][j][h] = false;
					this.parent[i][j][h] = -1;
					this.distance[i][j][h] = -1;
//					this.stepsTaken[i][j][h] = -1;
				}
			}
		}

		// set all to 0
		for (int i = 0; i < this.roomCount.length; i++) {
			this.roomCount[i] = 0;
		}
		this.roomCount[0] = 1;

		Node startingNode = new Node(startRow, startCol, superpowers, 0);
		this.initialNode = startingNode;
		this.endRow = endRow;
		this.endCol = endCol;

		this.queue = new LinkedList<>();
		this.found = false;

//		System.out.println(startRow + " " + startCol + " " + superpowers + " " + "0");
		Node p = Dijkstra(startRow, startCol, superpowers);
//		System.out.println(p.x + " " + p.y + " " + p.powersLeft + " " + p.distance);
		if (p == null) {
			return null;
		}
		return tracePath(p.x, p.y, p.powersLeft);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k >= this.roomCount.length) {
			return 0;
		}
		return this.roomCount[k];
	}

	// Node(int x, int y, int powersLeft, int distance);
	public Node Dijkstra(int startRow, int startCol, int powerCount) {
		Node n = new Node(startRow, startCol, powerCount, 0);
		this.queue.add(n);
		this.visited[startRow][startCol][powerCount] = true;
		this.distance[startRow][startCol][powerCount] = n.distance;
		this.parent[startRow][startCol][powerCount] = -1;
//		this.stepsTaken[this.startRow][this.startCol][powerCount] = 0;

		Node finalPoint = null;

		while (!this.queue.isEmpty()) {
			Node currNode = this.queue.remove();
			this.visited[currNode.x][currNode.y][currNode.powersLeft] = true;

			if (currNode.x == this.endRow && currNode.y == this.endCol && !this.found) {
//				System.out.println(currNode.x + " " + currNode.y + " " + currNode.powersLeft + " " + currNode.distance);
//				return null;
				this.found = true;
				finalPoint = new Node(currNode.x, currNode.y, currNode.powersLeft, currNode.distance);
			}

			for (int dir = 0; dir < 4; dir++) {
				if (withinMaze(currNode.x, currNode.y, dir)) {
					if (hasWall(currNode.x, currNode.y, dir) && currNode.powersLeft == 0) {
						continue;
					}

					int powerCountNew = 0;
					if (hasWall(currNode.x, currNode.y, dir)) {
						if (currNode.powersLeft > 0) {
							powerCountNew = currNode.powersLeft - 1;
						}
					} else {
						powerCountNew = currNode.powersLeft;
					}

					int newRow = currNode.x + DELTAS[dir][0];
					int newCol = currNode.y + DELTAS[dir][1];
					int newDistance = currNode.distance + 1;

					Node nN = new Node(
							newRow, newCol, powerCountNew, newDistance
					);

					if (this.visited[newRow][newCol][powerCountNew]) {
						continue;
					}

					int currSteps = this.distance[currNode.x][currNode.y][currNode.powersLeft] + 1;
					setStepsTaken(currNode.x, currNode.y, dir, currSteps, powerCountNew);
					setParent(newRow, newCol, dir, powerCountNew);
					this.queue.add(nN);
					this.distance[newRow][newCol][powerCountNew] = newDistance;
					this.visited[newRow][newCol][powerCountNew] = true;
				}
			}
		}

//		if (this.distance[endRow][endCol] == -1) {
//			return null;
//		}

//		return this.distance[endRow][endCol];
		return finalPoint;
	}

	public void setStepsTaken(int row, int col, int dir, int currSteps, int newPowerCount) {
		this.roomCount[currSteps] += 1;
		switch (dir) {
			case NORTH:
				this.distance[row - 1][col][newPowerCount] = currSteps;
				return;
			case SOUTH:
				this.distance[row + 1][col][newPowerCount] = currSteps;
				return;
			case EAST:
				this.distance[row][col + 1][newPowerCount] = currSteps;
				return;
			case WEST:
				this.distance[row][col - 1][newPowerCount] = currSteps;
		}
	}

	private void setParent(int row, int col, int dir, int newPowerCount) {
		switch (dir) {
			case NORTH:
				this.parent[row][col][newPowerCount] = SOUTH;
				return;
			case SOUTH:
				this.parent[row][col][newPowerCount] = NORTH;
				return;
			case EAST:
				this.parent[row][col][newPowerCount] = WEST;
				return;
			case WEST:
				this.parent[row][col][newPowerCount] = EAST;
		}
	}

	private boolean hasWall(int row, int col, int dir) {
		switch (dir) {
			case NORTH:
				return maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return maze.getRoom(row, col).hasEastWall();
			case WEST:
				return maze.getRoom(row, col).hasWestWall();
		}

		// will never reach
		return false;
	}

	private boolean withinMaze(int row, int col, int dir) {
		// not needed since our maze has a surrounding block of wall
		// but Joe the Average Coder is a defensive coder!
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;

		return true;
	}

	public Integer tracePath(int row, int col, int powers) {
		int rooms = 1;
		int directionValue = this.parent[row][col][powers];

		if (this.initialNode.x == this.endRow && this.initialNode.y == this.endCol) {
			maze.getRoom(this.initialNode.x, this.initialNode.y).onPath = true;
			return 0;
		}

//		System.out.println(row + " " + col + " " + powers);

		while (directionValue != -1) {
//			System.out.println(directionValue + " " + row + " " + col + " " + powers);
			directionValue = this.parent[row][col][powers];
			maze.getRoom(row, col).onPath = true;
			if (directionValue == 0) { // Go North
				rooms++;
				if (maze.getRoom(row, col).hasNorthWall()) {
					powers += 1;
				}
				row = row - 1;
			} else if (directionValue == 1) { // Go South
				rooms++;
				if (maze.getRoom(row, col).hasSouthWall()) {
					powers += 1;
				}
				row = row + 1;
			} else if (directionValue == 2) { // Go East
				rooms++;
				if (maze.getRoom(row, col).hasEastWall()) {
					powers += 1;
				}
				col = col + 1;
			} else if (directionValue == 3) { // Go West
				rooms++;
				if (maze.getRoom(row, col).hasWestWall()) {
					powers += 1;
				}
				col = col - 1;
			} else {
				return rooms - 1;
			}
		}

		return null;
	}


	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-spiral.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(2, 2, 4, 0, 1));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
