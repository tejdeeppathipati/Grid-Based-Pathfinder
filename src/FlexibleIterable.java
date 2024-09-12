import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic interface that extends the Iterable interface.
 */
public interface FlexibleIterable<T> extends Iterable<T> {

    /**
     * Creates an iterator that iterates only on objects whose datatype name is iterableObjectName and skips everything
     * else in the iterable object. For example, if the value of the iterableObjectName is ìObstacleî, this iterator
     * will iterate only over the Obstacle cells of the field. Similarly, if the value of the iterableObjectName is
     * ìPassageî, the iterator will iterate only over the Passage cells of the field.
     * If you have an object named obj, you can use the following code to get the name of its datatype:
     * obj.getClass().getName()
     * @param iterableObjectName the name of the datatype to iterate over
     * @return an iterator that iterates over objects of type iterableObjectName
     * @throws NoSuchElementException if there are no more elements in the iteration
     */
    public Iterator<T> iterator(String iterableObjectName) throws NoSuchElementException;
}
