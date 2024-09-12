import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic class that provides an iterator for the Field. It implements the Iterator interface.
 */
public class FieldIterator<T> implements Iterator<T> {

    private int row = 0;
    private String iterableObjectName;
    private Field<T> field;
    private int col = 0;

    /**
     *
     * @param field
     * @param iterableObjectName
     */
    public FieldIterator(Field<T> field, String iterableObjectName) {
        this.iterableObjectName = iterableObjectName;
        this.field = field;
    }

    /**
     * Returns true or false depending on whether or not there exist more elements on the Field to iterate on.
     * @return boolean
     */

    public boolean hasNext() {
        int tempRow = row;
        int tempCol = col;
        int abc = 0;
        while (tempRow < field.getHeight()) {
            while (tempCol < field.getWidth()) {
                T el;
                el = (T) field.getElement(tempRow, tempCol);
                if (el != null && el.getClass().getSimpleName().equals(iterableObjectName)) {
                    row = tempRow;
                    col = tempCol;
                    return true;
                }
                tempCol++;
            }
            tempCol = 0;
            tempRow++;
        }
        return false;
    }

    /**
     * @return Block
     */
    public T next() {
        int a = 0;
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        T el;
        el = (T) field.getElement(row, col++);
        if (col >= field.getWidth()) {
            row++;
            col = 0;
        }
        return el;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}

