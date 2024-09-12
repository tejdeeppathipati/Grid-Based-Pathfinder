
/**
 * Itís a concrete class that represents a single cell of the field that is specifically a passage. It inherits from
 * Block.
 */
public class Passage extends Block {
    private int value;
    /**
     * Returns the number of points assigned to the object when constructed.
     * @return the value assigned to the passage
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the label of the object which is simply its value.
     * @return the value assigned to the passage
     */
    public String toString() {
        return Integer.toString(value);
    }
    /**
     * A simple constructor.
     * @param value the value assigned to the passage
     */
    public Passage(int value) {
        this.value = value;
    }

}