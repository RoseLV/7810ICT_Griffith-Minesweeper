
public class neighbor {
    // Value
    public enum position {IN_BOUNDS, OUT_OF_BOUNDS}
    public static final position[] positions = position.values();
    private final int index;
    private final position location;

    // Constructor(method)
    public neighbor (int index, position location) {
        this.index = index;
        this.location = location;
    }

    // Getter
    public int getIndex() {
        return this.index;
    }
    public position getLocation() {
        return this.location;
    }
}

