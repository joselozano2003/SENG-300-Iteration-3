package com.autovend.software.GUI;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.item.ByTextSearch;
import com.autovend.products.Product;
import java.util.ArrayList;


//uses the ByTextSearch class in items 
public class AddItemByTextSearch extends JPanel {

    private JTextField textField;
    private JButton searchButton;

    public AddItemByTextSearch() {
        textField = new JTextField(20);
        add(textField);

        searchButton = new JButton("Search Product");
        searchButton.addActionListener(e -> {
            String inputString = textField.getText();
            // initialize the ByTextSearch
            SelfCheckoutStation station = new SelfCheckoutStation(null, null, null, ABORT, ABORT);
            ByTextSearch byTextSearch = new ByTextSearch(station);
            //uses the method from ByTextSearch class
            ArrayList<Product> productsToShow = byTextSearch.getProductsCorrespondingToTextSearch(inputString);
            // display the products in the GUI 
            JOptionPane.showMessageDialog(null, productsToShow);
        });
        add(searchButton);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new AddItemByTextSearch());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

