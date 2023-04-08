package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class AddItemByBrowsing {
    //variables need for Search Item By Browsing GUY
	JFrame AddItemByBrowsing_frame;
    JPanel Catalogue_panel,SearchByLetter_Panel,slider_panel;
    JTextField tempCelsius;
    JLabel Title, fahrenheitLabel,SliderLabel ;
    JSlider Slider;
    JButton Items;
    String[] letters = {"A", "B", "C", "D", "E", "F",
    	    "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
    	    "S", "T", "U", "V", "W", "X", "Y", "Z"};
    JButton[] letterButtons = new JButton[letters.length];
    
    
    //constructor
    public AddItemByBrowsing() {
    	AddItemByBrowsing_frame = new JFrame();
    	Catalogue_panel = new JPanel();
    	SearchByLetter_Panel = new JPanel();
    	Title = new JLabel();
    	Catalogue_panel.setLayout(new GridLayout(5,5, 2, 2)); 
    	SearchByLetter_Panel.setLayout(new GridLayout(0,26, 2, 2));
    	
    	//frame set uo 
    	AddItemByBrowsing_frame.setTitle("Add Item By Browsing");
    	AddItemByBrowsing_frame.setSize(1300,750);
    	AddItemByBrowsing_frame.setLayout(null);
    	AddItemByBrowsing_frame.setDefaultCloseOperation(AddItemByBrowsing_frame.EXIT_ON_CLOSE);
    	
    	//icon set up currently with a bug 
    	ImageIcon logoIcon = new ImageIcon("logo.png");
    	AddItemByBrowsing_frame.setIconImage(logoIcon.getImage());
    	AddItemByBrowsing_frame.getContentPane().setBackground(Color.LIGHT_GRAY);
    	
    	 
    	//Title for the page set up 
		Title.setText("Store Catalogue");
		Title.setFont(new Font("Mv Boli",Font.PLAIN,20));
		//Title.setVerticalAlignment(JLabel.TOP);
		//Title.setHorizontalAlignment(JLabel.CENTER);
		Title.setBounds(600, 25, 200, 50); //set x,y position within a frame as well as dimensions
		
		
		//butons set up for panel search by letter and catalogue panel 
		SearchByLetter_Panel.setBackground(Color.WHITE);
		SearchByLetter_Panel.setBounds(25,190,1250,100);
		
		Catalogue_panel.setBackground(Color.WHITE);
		Catalogue_panel.setBounds(50,300,1200,400);
		
		//Catalogue slider set up 
		SliderLabel = new JLabel();
		slider_panel = new JPanel();
		Slider = new JSlider();
		
		Slider.setPreferredSize(new Dimension(400,200));
		
		
		
		
		
		//current widgets to the GUI
		AddAlphabet();
		AddCatalogue();
		
		
		//adding everything to the main frame 
		//AddItemByBrowsing_frame.pack();
		AddItemByBrowsing_frame.add(Title);
		AddItemByBrowsing_frame.add(SearchByLetter_Panel);	
		AddItemByBrowsing_frame.add(Catalogue_panel);
		AddItemByBrowsing_frame.add(Slider);
		AddItemByBrowsing_frame.add(SliderLabel);
		AddItemByBrowsing_frame.add(slider_panel);
		AddItemByBrowsing_frame.setVisible(true);
		
    }
    
    private void AddAlphabet() {
    	//action listener 
    	/*
//    	ActionListener al = new ActionListener() {
//            public void actionPerformed1(ActionEvent evt) {
//                Object src = evt.getSource();
//                if (evt instanceof JButton) {
//                    currentLetter = ((JButton) src).getText();
//                    // do something with letter
//                } // else something seriously wrong
//            }
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//        };
 * 
 */
    	
        for (int i = 0; i < letters.length; ++i) {
            letterButtons[i] = new JButton(letters[i]);
            SearchByLetter_Panel.add(letterButtons[i]);
            System.out.println(letters[i]);
            //letterButtons[i].addActionListener(al);
        }	
        
    }
    
    private void AddCatalogue() {
    	
    	//Catalogue Slider 
    	
    	
    	//add the items
    	for(int j = 1; j < 26; j++) {
    		Items = new JButton("Item" + Integer.toString(j));
    		Catalogue_panel.add(Items);
    	}	
    }
    
  //signals to the customer to place item in the bagging area 
    private void SignalCustomer() {
    	JOptionPane.showMessageDialog(null, "Please Place Item in\n Bagging area\n", "Item to be bagged", JOptionPane.INFORMATION_MESSAGE);
    	
    }
    
    //signals to the system that an item is to be added, indicating the information about item 
    private void SignalSystem() {
    	//signals to the customer to place item in the bagging area 
    }
//    private double celsiusToFahrenheit(double celsius) {
//    }
    
    public static void main(String[] args) {
    	AddItemByBrowsing catalogue = new AddItemByBrowsing(); 
    	
    }
    
    
    

}

