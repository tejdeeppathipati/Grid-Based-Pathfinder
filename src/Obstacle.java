
/**
 * Itís a concrete class that represents a single cell of the field that is specifically an obstacle. It inherits
 * from Block.
 */
public class Obstacle extends Block {
    private String label;
    /**
     * Since itís impossible to go through an obstacle, it is assigned 0 points.
     * @return 0, the value of the obstacle
     */
    public int getValue() {
        return 0;
    }
    /**
     * A simple constructor.
     * @param label the label of the obstacle
     */
    public Obstacle(String label) {
        this.label = label;
    }
    /**
     * Returns the label of the object.
     * @return the label of the obstacle
     */
    public String toString() {
        return label;
    }

}

