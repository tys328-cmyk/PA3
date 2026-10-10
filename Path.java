public class Path implements Cloneable {
    private Coordinate[] path;
    private int size;

    public Path() {
        path = new Coordinate[200];
        size = 0;
    }
    
    public int size() {
        return size;
    }

    public void add(Coordinate c) {
        path[size] = c;
        size++;
    }

    public void remove() {
        if (size > 0) {
            size--;
        }
    }

    public Coordinate get(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException("Invalid index: " + i);
        }
        return path[i];
    }

    public Object clone() {
        Path copy = new Path();
        for (int i = 0; i < size; i++) {
            copy.add(new Coordinate(path[i].row(), path[i].col()));
        }
        return copy;
    }
}
