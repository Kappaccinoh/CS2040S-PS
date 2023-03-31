import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;

	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};

	private Maze maze;
	private boolean[][] visited;
	private int[][] scarinessLevel;
	private int[][] parent;
	private int endRow, endCol;
	private int startRow, startCol;

	private PriorityQueue<Triple> pQueue;

	class Triple implements Comparable<Triple> {
		int x;
		int y;
		int z;

		public Triple(int x, int y, int z) {
			// x coord
			this.x = x;

			// y coord
			this.y = y;

			// scariness value
			this.z = z;
		}

		@Override
		public int compareTo(Triple t) {
			if (this.z < t.z) {
				return -1;
			} else if (this.z > t.z) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public MazeSolver() {
		maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.scarinessLevel = new int[maze.getRows()][maze.getColumns()];
		this.parent = new int[maze.getRows()][maze.getColumns()];
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
		this.maze = maze;
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
				this.visited[i][j] = false;
				this.scarinessLevel[i][j] = Integer.MAX_VALUE;
			}
		}

		this.endRow = endRow;
		this.endCol = endCol;
		this.startRow = startRow;
		this.startCol = startCol;

		this.pQueue = new PriorityQueue();

		return Dijkstra(startRow, startCol);

	}

	// Implementation of Dijkstra's Algorithm
	// Return minimum possible fear level upon reaching the end room
	public Integer Dijkstra(int startRow, int startCol) {
		Triple p = new Triple(startRow, startCol, 0);
		this.pQueue.add(p);
		this.visited[p.x][p.y] = true;
		this.scarinessLevel[p.x][p.y] = 0;

		while (!this.pQueue.isEmpty()) {
			// Removes the point which has the highest priority
			Triple curr = this.pQueue.poll();
			this.visited[curr.x][curr.y] = true;
			Room currRoom = maze.getRoom(curr.x, curr.y);

			// North

			if (canGo(curr.x, curr.y, 0) && currRoom.getNorthWall() != TRUE_WALL && !this.visited[curr.x - 1][curr.y]) {
				// Relax
				relax(curr, currRoom,0);
			}

			// East
			if (canGo(curr.x, curr.y, 1) && currRoom.getEastWall() != TRUE_WALL && !this.visited[curr.x][curr.y + 1]) {
				// Relax
				relax(curr, currRoom,1);
			}

			// West
			if (canGo(curr.x, curr.y, 2) && currRoom.getWestWall() != TRUE_WALL && !this.visited[curr.x][curr.y - 1]) {
				// Relax
				relax(curr, currRoom,2);
			}

			// South
			if (canGo(curr.x, curr.y, 3) && currRoom.getSouthWall() != TRUE_WALL && !this.visited[curr.x + 1][curr.y]) {
				// Relax
				relax(curr, currRoom,3);
			}
		}
		if (scarinessLevel[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		}
		return scarinessLevel[endRow][endCol];
	}

	public void relax(Triple t, Room r, int dir) {

		int val = 0;

		switch(dir) {
			case 0:
				val = r.getNorthWall();
				break;
			case 1:
				val = r.getEastWall();
				break;
			case 2:
				val = r.getWestWall();
				break;
			case 3:
				val = r.getSouthWall();
				break;
		}

		if (val == EMPTY_SPACE) {
			val = 1;
		}

		int potentialSum = scarinessLevel[t.x][t.y] + val;
		int newRow = t.x + DELTAS[dir][0];
		int newCol = t.y + DELTAS[dir][1];

		int valueOfNextPoint = scarinessLevel[newRow][newCol];

		if (potentialSum < valueOfNextPoint) {
			// update
			scarinessLevel[newRow][newCol] = potentialSum;
		}
		// Add room into pQueue
		Triple nT = new Triple(newRow, newCol, potentialSum);
		this.pQueue.add(nT);
	}

	private boolean canGo(int row, int col, int dir) {
		// not needed since our maze has a surrounding block of wall
		// but Joe the Average Coder is a defensive coder!
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;

		return true;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.maze.getRows() || startCol >= this.maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= this.maze.getRows() || endCol >= this.maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < this.maze.getRows(); ++i) {
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				this.scarinessLevel[i][j] = Integer.MAX_VALUE;
			}
		}

		this.endRow = endRow;
		this.endCol = endCol;
		this.startRow = startRow;
		this.startCol = startCol;

		this.pQueue = new PriorityQueue();

		return Dijkstra1(startRow, startCol);
	}

	public Integer Dijkstra1(int startRow, int startCol) {
		Triple p = new Triple(startRow, startCol, 0);
		this.pQueue.add(p);
		this.visited[p.x][p.y] = true;
		this.scarinessLevel[p.x][p.y] = 0;

		while (!this.pQueue.isEmpty()) {
			// Removes the point which has the highest priority
			Triple curr = this.pQueue.poll();
			this.visited[curr.x][curr.y] = true;
			Room currRoom = maze.getRoom(curr.x, curr.y);

			// North

			if (canGo(curr.x, curr.y, 0) && currRoom.getNorthWall() != TRUE_WALL && !this.visited[curr.x - 1][curr.y]) {
				// Relax
				relax1(curr, currRoom,0);
			}

			// East
			if (canGo(curr.x, curr.y, 1) && currRoom.getEastWall() != TRUE_WALL && !this.visited[curr.x][curr.y + 1]) {
				// Relax
				relax1(curr, currRoom,1);
			}

			// West
			if (canGo(curr.x, curr.y, 2) && currRoom.getWestWall() != TRUE_WALL && !this.visited[curr.x][curr.y - 1]) {
				// Relax
				relax1(curr, currRoom,2);
			}

			// South
			if (canGo(curr.x, curr.y, 3) && currRoom.getSouthWall() != TRUE_WALL && !this.visited[curr.x + 1][curr.y]) {
				// Relax
				relax1(curr, currRoom,3);
			}
		}
		if (scarinessLevel[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		}
		return scarinessLevel[endRow][endCol];
	}

	public void relax1(Triple t, Room r, int dir) {

		int val = 0;

		switch(dir) {
			case 0:
				val = r.getNorthWall();
				break;
			case 1:
				val = r.getEastWall();
				break;
			case 2:
				val = r.getWestWall();
				break;
			case 3:
				val = r.getSouthWall();
				break;
		}

		if (val == EMPTY_SPACE) {
			val = 1;
		}

		int potentialSum = val;
		int newRow = t.x + DELTAS[dir][0];
		int newCol = t.y + DELTAS[dir][1];

		int valueOfNextPoint = scarinessLevel[newRow][newCol];

		if (potentialSum < valueOfNextPoint) {
			// update
			scarinessLevel[newRow][newCol] = potentialSum;
		}
		// Add room into pQueue
		Triple nT = new Triple(newRow, newCol, potentialSum);
		this.pQueue.add(nT);
	}


	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-simple.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 0, 4));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
