/**
 * An abstract class that represents a single cell of the field.
 * The Passage and the Obstacle classes will derive from it.
 */
public abstract class Block {

    /**
     * A placeholder method to be overridden by Passage and Obstacle classes.
     * @return the value of the block
     */
    public abstract int getValue();
}
