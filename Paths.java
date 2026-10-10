public class Paths {
    private Path[] paths;
    private int size;

    public Paths() {
        paths = new Path[10000];
        size = 0;
    }

    public int size() {
        return size;
    }

    public void add(Path p) {
        paths[size] = p;
        size++;
    }

    public void remove() {
        if (size > 0) {
            size--;
        }
    }

    public Path get(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException("Invalid index: " + i);
        }
        return paths[i];
    }
}
