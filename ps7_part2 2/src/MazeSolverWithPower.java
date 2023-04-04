import java.util.LinkedList;
import java.util.Queue;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};
	private Node[][][] node;
	private Maze maze;
	private boolean[][] visited;
	private int[] roomCount;

	class Node {
		int x;
		int y;
		int power;
		int distance;
		Node parent;
		boolean visited;

		public Node(int x, int y, int power, int distance, Node parent, boolean visited) {
			this.x = x;
			this.y = y;
			this.power = power;
			this.distance = distance;
			this.parent = parent;
			this.visited = visited;
		}
	}

	public MazeSolverWithPower() {
		this.maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.node = new Node[maze.getRows()][maze.getColumns()][maze.getRows() * maze.getColumns()];
		this.roomCount = new int[maze.getRows() * maze.getColumns()];
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
	}

	public void clean(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		for (int i = 0; i < this.roomCount.length; i++) {
			this.roomCount[i] = 0;
		}
		this.roomCount[0] = 1;

		for (int j = 0; j < maze.getRows(); j++) {
			for (int h = 0; h < maze.getColumns(); h++) {
				this.visited[j][h] = false;
			}
		}

		for (int i = 0; i < maze.getRows(); i++) {
			for (int h = 0; h < maze.getColumns(); h++) {
				for (int j = 0; j < maze.getRows() * maze.getColumns(); j++) {
					this.node[i][h][j] = null;
				}
			}
		}
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		clean(startRow, startCol, endRow, endCol);
		Integer numberOfSteps = Dikstras(startRow, startCol, endRow, endCol, 0);
		return numberOfSteps;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		clean(startRow, startCol, endRow, endCol);
		Integer numberOfSteps = Dikstras(startRow, startCol, endRow, endCol, superpowers);
		return numberOfSteps;
	}

	public Integer Dikstras(int startRow, int startCol, int endRow, int endCol, int superpowers) {

		Queue<Node> queue = new LinkedList<Node>();
		Node startingNode = new Node(startRow, startCol, superpowers, 0, null, true);
		this.visited[startRow][startCol] = true;
		boolean found = false;
		int numberOfSteps = -1;
		Node finalNode = null;
		queue.add(startingNode);

		while (!queue.isEmpty()) {
			Node currNode = queue.remove();
			currNode.visited = true;
			this.visited[startRow][startCol] = true;
			if (currNode.x == endRow && currNode.y == endCol && !found) {
				numberOfSteps = currNode.distance;
				finalNode = currNode;
				found = true;
			}

			for (int dir = 0; dir < 4; dir++) {

				// if travel out of the maze then skip
				if (!withinMaze(currNode.x, currNode.y, dir)) {
					continue;
				}

				// if direction is a wall and no more power then skip
				if (hasWall(currNode.x, currNode.y, dir) && currNode.power <= 0) {
					continue;
				}

				// else, safe to create a new node
				int newRow = currNode.x + DELTAS[dir][0];
				int newCol = currNode.y + DELTAS[dir][1];
				int newPower = currNode.power;
				if (hasWall(currNode.x, currNode.y, dir)) {
					newPower = newPower - 1;
				}
				int newDistance = currNode.distance + 1;
				Node oldParent = currNode;

				Node newNode = new Node(newRow, newCol, newPower, newDistance, oldParent, true);

				// if node in particular has been visited before then skip
				if (this.node[newNode.x][newNode.y][newNode.power] != null) {
					if (this.node[newNode.x][newNode.y][newNode.power].visited == true) {
						continue;
					}
				}
				this.node[newRow][newCol][newPower] = newNode;
				queue.add(newNode);

				if (!this.visited[newRow][newCol]) {
					this.roomCount[newDistance]++;
					this.visited[newRow][newCol] = true;
				}
			}
		}

		drawPath(finalNode);

		return numberOfSteps == -1 ? null : numberOfSteps;
	}

	public void drawPath(Node finalNode) {
		if (finalNode == null) {
			return;
		}

		Node currNode = finalNode;

		while (currNode.parent != null) {
//			System.out.println(currNode.x + " " + currNode.y + " " + currNode.power);
			maze.getRoom(currNode.x, currNode.y).onPath = true;
			currNode = currNode.parent;
		}

        maze.getRoom(currNode.x, currNode.y).onPath = true;
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
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;

		return true;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k >= this.roomCount.length) {
			return 0;
		}
		return this.roomCount[k];
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-dense.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 1, 1, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
