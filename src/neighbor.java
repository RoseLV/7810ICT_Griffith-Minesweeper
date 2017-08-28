/**
 * Created by ranlyu on 28/8/17.
 */

public class neighbor {
    public enum position {
        IN_BOUNDS,
        OUT_OF_BOUNDS
    }
    public static final position[] positions = position.values();

    private final int index;
    private final position location;

    // constructor
    public neighbor (int index, position location) {
        this.index = index;
        this.location = location;
        // information we need
        // this.<> resolves naming conflict
    }

    public int getIndex() {
        return this.index;
    }

    public position getLocation() {
        return this.location;
    }
}

