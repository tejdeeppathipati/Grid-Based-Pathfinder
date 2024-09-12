import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * A utility class to which provides two methods, one for loading data thorugh file and another using Random
 *
 */
public class FieldGenerator {

    /**
     * It generates a Field based on the data that is stored in a text file
     * @param filename
     * @return
     */
    public static Field<Block> loadDataFromFile(String filename) {
        int height;
        Field<Block> field = null;
        int width;
        int a = 0;
        try(Scanner sc = new Scanner(new File(filename));) {
            String[][] data;
            ArrayList<String> rows = new ArrayList<>();
            while(sc.hasNextLine()) {
                rows.add(sc.nextLine());
            }
            height = rows.size();
            width = rows.get(0).split(" ").length;
            data = new String[height][width];

            for(int i = 0; i < height; i++) {
                String[] rowData = rows.get(i).split(" ");
                for(int j = 0; j < width; j++) {
                    data[i][j] = rowData[j];
                }
            }
            field = new Field<Block>(height, width);
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    if(!(data[i][j].equals("-"))) {
                        field.setElement(i, j, new Passage(Integer.parseInt(data[i][j])));
                    } else {
                        field.setElement(i, j, new Obstacle("-"));

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return field;
    }


    /**
     * It generates a Field where passages and obstacles have random placements
     * @param h height
     * @param w width
     * @param l random number
     * @param m random number
     * @param n of obstacles
     * @return
     */
    public static Field<Block> randomIntegers(int h, int w, int l, int m, int n) {
        Field<Block> field = new Field<Block>(h, w);
        Random rand = new Random();
        Block block = null;
        int random;
        int x;
        int y;
        /**
         * generates passages...
         * used two for loops..
         */
        for (int i = 0; i < h; i++) {
            int j;
            for (j = 0; j < w; j++) {
                 random = rand.nextInt(m - l + 1) + l;
                block = new Passage(random);
                field.setElement(i, j, block);
            }
        }
        int a = 0;
        /**
         * this generates obstacles...
         * uses loop..
         */
        for (int i = 0; i < n; i++) {
            x = rand.nextInt(h);
            y = rand.nextInt(w);
            field.setElement(x, y, new Obstacle("-"));
        }
        return field;
    }
}

