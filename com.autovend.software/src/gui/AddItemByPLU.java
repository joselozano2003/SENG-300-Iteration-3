package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.autovend.software.item.ByPLUCode;

//Adds Item by PLU using the method ByPLU in item folder in the master branch
//adds an item using plu which should be only 1 item and does not weigh the produce

public class AddItemByPLU extends JFrame {
    private JPanel panel;
    private JTextField inputField;
    private JLabel infoLabel;
    private JButton addButton;
    private ByPLUCode itemFacade;

    public AddItemByPLU(ByPLUCode itemFacade) {
        this.itemFacade = itemFacade;
        setTitle("Add Item By PLU");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        inputField = new JTextField(20);
        infoLabel = new JLabel("Enter PLU Code for the product:");//prompts the user to input the plu code
        addButton = new JButton("Add Item");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = inputField.getText();
                if(code.equals("")) {
                    JOptionPane.showMessageDialog(new JPanel(),
                        "Please enter a PLU code.",
                        "Invalid PLU! Please try again!",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                itemFacade.reactToPLUCodeEnteredEvent(code, 1.0);
                JOptionPane.showMessageDialog(new JPanel(),
                    "Item with PLU code " + code + " was added to the cart.",
                    "Item is added",
                    JOptionPane.PLAIN_MESSAGE);
            }
        });

        panel.add(infoLabel);
        panel.add(inputField);
        panel.add(addButton);
        add(panel);
        setVisible(true);
    }
}
