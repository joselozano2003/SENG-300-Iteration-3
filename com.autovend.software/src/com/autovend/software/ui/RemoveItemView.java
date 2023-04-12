package com.autovend.software.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.autovend.PriceLookUpCode;
import com.autovend.Numeral;
import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.software.item.ItemEventListener;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.ui.UIEventListener;
import com.autovend.software.ui.BrowsingViewObserver;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.attendant.AttendantController;

public class RemoveItemView extends JPanel {

    // CODE TO COPY BELOW:

    // a

    // Maximum number of items per screen.
    static int MAX_ITEMS = 20;

    private List<BrowsingViewObserver> observers;
    private List<UIEventListener> listeners;

    private AttendantController attendController;


    private static ArrayList<CustomerStationLogic> customerList;
    JButton backButton;
    JLabel messageLabel;


    JPanel browsingPanel = this;
    JPanel middlePanel;
    JPanel cataloguePanel;
    JPanel letterPanel;
    JScrollPane scrollBar;

    Map<Product,Double> cusCart;



    JLabel searchLabel;
    JPanel borderPanel;
    JButton itemButtons[] = new JButton[MAX_ITEMS];
    int num_items = 0; // Actual number of items to display on screen.

    // Character array to represent string entered so far by user.
    ArrayList<Character> stringEntered = new ArrayList<Character>();

    // Tracks page number we're currently on in the catalogue.

    // Called when add item by browsing button is pressed, this method will
    // make calls to the controller to add items to shopping cart.


    /**
     * Add Item by browsing views
     *
     */



    public RemoveItemView() {
        // swing compounents needed for the GUI

        observers = new ArrayList<>();
        listeners = new ArrayList<>();

        messageLabel = new JLabel("");

        browsingPanel.add(messageLabel);

        cataloguePanel = new JPanel();
        middlePanel = new JPanel();
        borderPanel = new JPanel();
        letterPanel = new JPanel();

        scrollBar = new JScrollPane(borderPanel);
        // scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        browsingPanel.setLayout(new BoxLayout(browsingPanel, BoxLayout.PAGE_AXIS));
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.LINE_AXIS));
        borderPanel.setLayout(new BorderLayout());

        JLabel Title = new JLabel();
        searchLabel = new JLabel("");
        // Item buttons will be placed in the catalogue panel.
        cataloguePanel.setLayout(new FlowLayout());

        // icon set up currently with a bug
        ImageIcon logoIcon = new ImageIcon("logo.png");
        // Title for the page set up
        Title.setText("Remove Customer Item");
        Title.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

        cataloguePanel.setBackground(Color.WHITE);

        cataloguePanel.setAlignmentX(browsingPanel.CENTER_ALIGNMENT);

        cataloguePanel.setPreferredSize(new Dimension(1200, 5000));
        scrollBar.setPreferredSize(new Dimension(1200, 400));

        Title.setAlignmentX(browsingPanel.CENTER_ALIGNMENT);

        borderPanel.add(cataloguePanel);
        browsingPanel.add(Box.createVerticalStrut(50));
        browsingPanel.add(Title);
        browsingPanel.add(Box.createVerticalStrut(30));
        browsingPanel.add(searchLabel);
        browsingPanel.add(middlePanel);
        middlePanel.add(Box.createGlue());
        middlePanel.add(scrollBar);
        middlePanel.add(Box.createGlue());
        browsingPanel.add(Box.createGlue());
        browsingPanel.setVisible(true);

        cusCart = attendController.getSession.getShoppingCart();

        Product[] prodArray = cusCart.keySet().toArray(new Product[cusCart.keySet().size()]);

        for (int i = 0; i < prodArray.length; i++) {
            Product currentProduct = prodArray[i];

            JButton currentButton = new JButton();
            cataloguePanel.add(currentButton);
            currentButton.setPreferredSize(new Dimension(400, 200));

            if (currentProduct instanceof PLUCodedProduct) {
                PLUCodedProduct pluProduct = (PLUCodedProduct) currentProduct;
                currentButton.setText(pluProduct.getDescription());

            }
            currentButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeProductButtonPressed(productGot);
                    currentButton.setBackground(Color.GREEN);
                }
            });
        }
    }


    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel testPanel = new RemoveItemView();
        testFrame.setVisible(true);
        testFrame.add(testPanel);
        testFrame.validate();
    }


    public void registerAttendantController(AttendantController attController) {
        if (attController == null) return;
        this.attendController = attController;
    }

    public void removeProductButtonPressed(PLUCodedProduct pProduct) {

    }

}
