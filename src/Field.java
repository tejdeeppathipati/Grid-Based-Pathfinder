import java.util.Iterator;
import java.util.NoSuchElementException;

public class Field<T> implements FlexibleIterable<T> {

    private int height;
    private T[][] matrix;
    private int width;
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public T getElement(int row, int col) {
        return matrix[row][col];
    }
    public Field(int height, int width) {
        this.height = height;
        this.width = width;
        matrix = (T[][])new Object[height][width];
    }
    public void setElement(int row, int col, T el) {
        matrix[row][col] = el;
    }
    /**
     * The default iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int row = 0;
            private int col = 0;

            @Override
            public boolean hasNext() {
                return row < getHeight();
            }

            @Override
            public T next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                T el;
                el = (T) getElement(row, col++);
                if (col >= getWidth()) {
                    col = 0;
                    row++;
                }
                return el;

            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    /**
     * This is the overloaded iterator method as explained in the FlexibleIterable interface. The only thing
     * that this method does is to create and return a new FieldIterator object.
     * @return Iterator
     */
    @Override
    public Iterator<T> iterator(String iterableObjectName) {
        return new FieldIterator<>(this, iterableObjectName);
    }
    /**
     * The string representations of all the matrix elements are merged into a single string. Use a singlespace separator between the elements and a newline character at the end of each row. No added
     * leading or trailing spaces.
     * @return String
     */
    @Override
    public String toString() {
        int i; int j;
        String fieldtoString;
        fieldtoString = "";
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                fieldtoString += matrix[i][j] + " ";
            }
            fieldtoString = fieldtoString + "\n";
        }
        return fieldtoString;
    }
}
