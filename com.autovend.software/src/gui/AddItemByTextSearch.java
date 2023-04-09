package gui;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//need to import the BYTEXTSEARCH class from the master branch

public class AddItemByTextSearch extends JPanel {

    private JTextField textField;
    private JButton searchButton;

    public AddItemByTextSearch() {
        textField = new JTextField(20);
        add(textField);

        searchButton = new JButton("Search Product");
        searchButton.addActionListener(e -> {
            String inputString = textField.getText();
            ByTextSearch byTextSearch = new ByTextSearch(new SelfCheckoutStation()); // initialize the ByTextSearch
            ArrayList<Product> productsToShow = byTextSearch.getProductsCorrespondingToTextSearch(inputString);
            // display the products in the GUI 
            JOptionPane.showMessageDialog(null, productsToShow);
        });
        add(searchButton);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new AddItemByTextSearchButton());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
