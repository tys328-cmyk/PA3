public class Coordinate {
    private int row;
    private int col;

    public Coordinate(int r, int c) {
        row = r;
        col = c;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            Coordinate other = (Coordinate) o;
            if (row == other.row && col == other.col) {
                return true;
            }
        }
        return false;
    }
}
