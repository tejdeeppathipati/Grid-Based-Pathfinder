import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

public class GUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JButton openB = new JButton("Open file");
    private final JButton runB = new JButton("Run");
    private final JComboBox<String> jComboBox = new JComboBox<>();
    private final JLabel iterationType = new JLabel("Iteration type:");
    private final JTextArea resTextArea;
    {
        resTextArea = new JTextArea(30, 30);
    }
    private File selectedFile = null;
    private Field<Block> field;

    public static String iterateObstacles(Field<Block> field) {
        Iterator<Block> it = field.iterator("Obstacle");
        String str = "";
        while(it.hasNext())
            str = str + it.next() + " " + (it.hasNext() ? '\u2794': "");
        return str;
    }

    public static String iteratePassages(Field<Block> field) {
        Iterator<Block> it = field.iterator("Passage");
        String str = "";
        while(it.hasNext())
            str = str + it.next() + " " + (it.hasNext() ? '\u2794': "");
        return str;
    }

    public static String entireField(Field<Block> field) {
        Iterator<Block> it = field.iterator();
        String str = "";
        while(it.hasNext())
            str = str + it.next() + " " + (it.hasNext() ? '\u2794': "");
        return str;
    }


    public GUI() {
        setTitle("The football field");
        setSize(550, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.add(openB);
        topPanel.add(iterationType);
        topPanel.add(jComboBox);
        topPanel.add(runB);
        add(topPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(resTextArea);
        add(scrollPane, BorderLayout.CENTER);


        // Populate the iteration type combo box
        jComboBox.addItem("Best Starting Point");
        jComboBox.addItem("Best Maximum Sum");
        jComboBox.addItem("Best Route");
        jComboBox.addItem("Entire field");
        jComboBox.addItem("Obstacles only");
        jComboBox.addItem("Passages only");

        // Register action listeners
        openB.addActionListener(this);
        runB.addActionListener(this);
    }

    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openB) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                // Load the data from the file
                field = FieldGenerator.loadDataFromFile(selectedFile.getPath());
            }
        } else if (e.getSource() == runB) {
            if (field != null) {
                // Clear the result text area
                resTextArea.setText("");
                resTextArea.append("Field area: \n");
                resTextArea.append(field.toString());
                // Run the selected iteration type and display the result
                String iterationType = (String) jComboBox.getSelectedItem();
                switch (iterationType) {
                    case "Best Starting Point":
                        resTextArea.append("Best Starting Point: " +Game.bestStartingPoint(field).startingColumn + "\n");
                        break;
                    case "Best Maximum Sum":
                        resTextArea.append("Best Maximum Sum: " +Game.bestStartingPoint(field).totalPoints + "\n");
                        break;
                    case "Best Route":
                        resTextArea.append("Best Route: " +Game.bestRoute(field,Game.bestStartingPoint(field).startingColumn) + "\n");
                        break;
                    case "Entire field":
                        resTextArea.append("EntireField: \n");
                        resTextArea.append(entireField(field) + "\n");
                        break;
                    case "Obstacles only":
                        resTextArea.append("Obstacles: \n");
                        resTextArea.append(iterateObstacles(field) + "\n");
                        break;
                    case "Passages only":
                        resTextArea.append("Passages: \n");
                        resTextArea.append(iteratePassages(field) + "\n");
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a file first", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
