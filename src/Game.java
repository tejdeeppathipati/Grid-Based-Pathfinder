import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * This class contains the main method that you can use to run and test your application
 *
 */
public class Game {

    public static class TwoValues {
        public int startingColumn;
        public int totalPoints;
        TwoValues(int startingColumn, int totalPoints){
            this.startingColumn = startingColumn;
            this.totalPoints = totalPoints;
        }
    }
    public static void main(String args[]) {
        //Field<Block> field = FieldGenerator.loadDataFromFile("/Users/tejdeeppathipati/Desktop/input.txt");
        Field<Block> field = FieldGenerator.randomIntegers(7, 7, 1, 9, 10);

        /**
         print the whole field
         */
        System.out.println(field);

        //Iterator<Block> it = field.iterator("Passage");
      //Iterator<Block> it = field.iterator("Obstacle"); // same thing for Obstacle objects
//        while(it.hasNext())
//            System.out.println(it.next());

        TwoValues br = bestStartingPoint(field);
        System.out.println("Best starting point is in column " + br.startingColumn + " and the total points collected from this route is " + br.totalPoints);

        ArrayList<Block> al = bestRoute(field, br.startingColumn);
        for (Block b : al)
            System.out.println(b);
    }

    /**
     * It finds the best starting point, i.e. what column of the first row of the Field we must use in order
     * to collect the highest number of points while crossing the Field. It also calculates the sum of these
     * points
     *
     * @param field
     * @return
     */
    public static TwoValues bestStartingPoint(Field<Block> field) {
        int bestCol = 0;
        int bestSum = Integer.MIN_VALUE;

        for (int col = 0; col < field.getWidth(); col++) {
            int currentSum = maximumRoutePath(field, 0, col);
            if (currentSum > bestSum) {
                bestSum = currentSum;
                bestCol = col;
            }
        }
        return new TwoValues(bestCol, bestSum);
    }
    /**
     * The method itreates to left right and the straight..
     * @param field
     * @param row
     * @param col
     * @return
     */
    public static int maximumRoutePath(Field<Block> field, int row, int col) {
        // Check if we are traversing with in boundaries of ground - width and height to
        // be considered

        if (row >= field.getHeight() || col >= field.getWidth() || col < 0) {
            return 0;
        }

        if (((Block) (field.getElement(row, col))) instanceof Obstacle && row != field.getHeight() - 1) {
            // While traversing check if its an obstacle, if yes then stop traversal and
            // return with current value
            return ((Block) (field.getElement(row, col))).getValue();
        } else if (((Block) (field.getElement(row, col))) instanceof Obstacle && row == field.getHeight() - 1) {
            // if we are finding obstacle in last row(Goal keeper end) then don't consider
            // that path as we consider only complete and best paths
            return -99999;
        }

        int middle = 0;
        // Traverse down into middle
        int left = maximumRoutePath(field, row + 1, col);
        if (col != 0) {
            // Traverse left if there is a path, sometimes player might be at extreme left
            // boundary
            middle = maximumRoutePath(field, row + 1, col - 1);
        }
        // Traverse to right side
        int right = maximumRoutePath(field, row + 1, col + 1);

        // Find out max element of left,right,middle and add it to current value - this
        // is to obtain maximum sum
        return Math.max(middle, Math.max(left, right)) + ((Block) (field.getElement(row, col))).getValue();
    }

    /**
     * Given a starting point (i.e. a column index in row 0) it returns the list of Blocks that form the best
     * route from one endline to the other one
     *
     * @param field
     * @param
     * @return
     */
    public static ArrayList<Block> bestRoute(Field<Block> field, int bestStartingColumn) {
        ArrayList<Block> route = new ArrayList<>();
        int row = 0;
        int col = bestStartingColumn;

        while (row < field.getHeight() - 1) {
            // Add current block to route
            route.add(field.getElement(row, col));

            // Find next block to traverse
            int middle = Integer.MIN_VALUE;
            int left = col > 0 ? maximumRoutePath(field, row + 1, col - 1) : Integer.MIN_VALUE;
            int right = col < field.getWidth() - 1 ? maximumRoutePath(field, row + 1, col + 1) : Integer.MIN_VALUE;

            if (col >= 0 && col < field.getWidth()) {
                middle = maximumRoutePath(field, row + 1, col);
            }

            if (left >= middle && left >= right) {
                col--;
            } else if (right >= middle && right >= left) {
                col++;
            }
            row++;
        }

        // Add last block to route
        route.add(field.getElement(row, col));

        return route;

    }






}
