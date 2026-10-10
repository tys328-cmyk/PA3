import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Maze implements Cloneable {
    private char[][] maze;
    private int rows;
    private int cols;
    public static long recursions;

    // O(rows * cols) - Java fills every cell of the new array
    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        maze = new char[rows][cols];
    }

    // O(rows * cols) - reads one character for every cell
    public void load(String filename) {
        try {
            Scanner read = new Scanner(new File(filename));
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    maze[i][j] = read.next().charAt(0);
                }
            }
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found.");
        }
    }

    // O(1)
    public char getPoint(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new ArrayIndexOutOfBoundsException("Invalid location: (" + row + ", " + col + ")");
        }
        return maze[row][col];
    }

    // O(1)
    public void setPoint(int row, int col, char c) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new ArrayIndexOutOfBoundsException("Invalid location: (" + row + ", " + col + ")");
        }
        maze[row][col] = c;
    }

    // O(1)
    private boolean isValid(Coordinate c) {
        if (c.row() >= 0 && c.row() < rows && c.col() >= 0 && c.col() < cols) {
            return true;
        }
        return false;
    }

    // O(4^(rows * cols)). Worst case of the recursive helper
    public Path findPath(Coordinate start, Coordinate end) {
        recursions = 0;
        Path currentPath = new Path();
        boolean[][] visited = new boolean[rows][cols];
        if (findPath(start, end, currentPath, visited)) {
            return currentPath;
        }
        return null;
    }

    // O(4^(rows * cols)) - each call can branch into 4 recursive calls,
    // and the depth can reach rows * cols cells in the worse case
    private boolean findPath(Coordinate current, Coordinate end, Path currentPath, boolean[][] visited) {
        recursions++;
        if (!isValid(current)) {
            return false;
        }
        if (maze[current.row()][current.col()] == '#') {
            return false;
        }
        if (visited[current.row()][current.col()]) {
            return false;
        }
        if (current.equals(end)) {
            currentPath.add(current);
            return true;
        }
        visited[current.row()][current.col()] = true;
        currentPath.add(current);
        // right
        if (findPath(new Coordinate(current.row(), current.col() + 1), end, currentPath, visited)) {
            return true;
        }
        // left
        if (findPath(new Coordinate(current.row(), current.col() - 1), end, currentPath, visited)) {
            return true;
        }
        // up
        if (findPath(new Coordinate(current.row() - 1, current.col()), end, currentPath, visited)) {
            return true;
        }
        // down
        if (findPath(new Coordinate(current.row() + 1, current.col()), end, currentPath, visited)) {
            return true;
        }
        currentPath.remove();
        visited[current.row()][current.col()] = false;
        return false;
    }

    // O(4^(rows * cols)). Worst case of the recursive helper below
    public Paths findAllPaths(Coordinate start, Coordinate end) {
        recursions = 0;
        Paths paths = new Paths();
        boolean[][] visited = new boolean[rows][cols];
        Path currentPath = new Path();
        findAllPaths(start, end, currentPath, paths, visited);
        return paths;
    }

    // O(4^(rows * cols)). Explores every possible path, each call can
    // branch into 4 recursive calls with a depth of up to rows * cols

    private void findAllPaths(Coordinate current, Coordinate end, Path currentPath, Paths paths, boolean[][] visited) {
        recursions++;
        if (!isValid(current)) {
            return;
        }
        if (maze[current.row()][current.col()] == '#') {
            return;
        }
        if (visited[current.row()][current.col()]) {
            return;
        }
        currentPath.add(current);
        visited[current.row()][current.col()] = true;
        if (current.equals(end)) {
            paths.add((Path) currentPath.clone());
        } else {
            findAllPaths(new Coordinate(current.row(), current.col() + 1), end, currentPath, paths, visited);
            findAllPaths(new Coordinate(current.row(), current.col() - 1), end, currentPath, paths, visited);
            findAllPaths(new Coordinate(current.row() - 1, current.col()), end, currentPath, paths, visited);
            findAllPaths(new Coordinate(current.row() + 1, current.col()), end, currentPath, paths, visited);
        }
        currentPath.remove();
        visited[current.row()][current.col()] = false;
    }

    // O(rows * cols). Copies every cell into the new maze
    public Object clone() {
        Maze copy = new Maze(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy.maze[i][j] = maze[i][j];
            }
        }
        return copy;
    }

    // O(rows * cols). Visits every cell once
    public String toString() {
        String out = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                out = out + maze[i][j] + " ";
            }
            out = out + "\n";
        }
        return out;
    }

    // O(rows * cols). Clone and toString are O(rows * cols), and the path
    // has at most rows * cols coordinates
    public void visualizePath(Path path) {
        Maze copy = (Maze) clone();
        for (int i = 0; i < path.size(); i++) {
            Coordinate c = path.get(i);
            if (copy.maze[c.row()][c.col()] == '.') {
                copy.maze[c.row()][c.col()] = '*';
            }
        }
        System.out.println("Maze with solution path");
        System.out.println(copy);
    }
}

/*
Test Case 5 Analysis:

findPath stops as soon as it reaches the end, and since the mazes are open, going right first
then down leads almost straight to the exit. Its number of recursions grows slowly, roughly in proportion
to the number of cells (much better than the worst case of O(4^(n*n)). Because it must explore every possible 
path, it cannot stop early. So its number of recursions increases by a higher factor as the maze grows. This matches
the exponenetial time complexity O(4^(n*n)): adding just one row and column multiplies the work many times,
so finding all paths quickly becomes impractical for larger mazes.

*/
