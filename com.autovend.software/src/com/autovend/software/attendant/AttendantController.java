/* P3-4 Group Members
 * 
 * Abdelrhafour, Achraf (30022366)
 * Campos, Oscar (30057153)
 * Cavilla, Caleb (30145972)
 * Crowell, Madeline (30069333)
 * Debebe, Abigia (30134608)
 * Dhuka, Sara Hazrat (30124117)
 * Drissi, Khalen (30133707)
 * Ferreira, Marianna (30147733)
 * Frey, Ben (30088566)
 * Himel, Tanvir (30148868)
 * Huayhualla Arce, Fabricio (30091238)
 * Kacmar, Michael (30113919)
 * Lee, Jeongah (30137463)
 * Li, Ran (10120152)
 * Lokanc, Sam (30114370)
 * Lozano Cetina, Jose Camilo (30144736)
 * Maahdie, Monmoy (30149094)
 * Malik, Akansha (30056048)
 * Mehedi, Abdullah (30154770)
 * Polton, Scott (30138102)
 * Rahman, Saadman (30153482)
 * Rodriguez, Gabriel (30162544)
 * Samin Rashid, Khondaker (30143490)
 * Sloan, Jaxon (30123845)
 * Tran, Kevin (30146900)
 */
package com.autovend.software.attendant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.*;
import com.autovend.products.Product;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.customer.CustomerStationListener;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.customer.CustomerController.State;
import com.autovend.software.item.ItemFacade;

import auth.AttendantAccount;
import auth.AuthFacade;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.ui.AttendantUIEventListener;
import com.autovend.software.ui.AttendantView;

public class AttendantController implements CustomerStationListener, AttendantUIEventListener{

	private static ArrayList<CustomerController> customerStations;
	private AttendantModel model;
	private AuthFacade auth;
	private ArrayList<HashMap<Product, Double>> removedProductsRequest;
	private AttendantAccount stationAccount;
	private SupervisionStation superStation;
	private AttendantView aView;

	public AttendantController(SupervisionStation supervisionStation, AttendantView attView) {
		this.auth = new AuthFacade();
		customerStations = new ArrayList<CustomerController>();
		removedProductsRequest = new ArrayList<HashMap<Product, Double>>();
		superStation = supervisionStation;
		aView = attView;
	}
	public boolean startLogIn(AttendantAccount attendantAccount) {
		return auth.logIn(attendantAccount);
	}

	public boolean startLogOut() {
		if (stationAccount == null) return false;
		return auth.logOut(stationAccount);
	}

	public boolean startAddAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount) {
		return auth.addAccount(attendantAccount, addedAccount);
	}

	public boolean startDeleteAccount(AttendantAccount attendantAccount, AttendantAccount removeAccount) {
		return auth.deleteAccount(attendantAccount, removeAccount);
	}

	// TODO: Gets triggered from GUI
	/**
	 * This method is called when the attendant wants to add a new customer station
	 * @param stationNumber, stationNumber is the number of the station that the attendant wants to oprate on
	 *
	 * @param product, product is the product that the attendant wants to add to the station. It is given through the keyboard
	 *
	 * @param quantity, quantity is the quantity of the product that the attendant wants to add to the station. It is given through the keyboard
	 */
	public void addItemToStationByTextSearch(int stationNumber, String product, double quantity) {
		CustomerController controller = customerStations.get(stationNumber);
		CustomerSession currentSession = controller.getCurrentSession();
		if (ProductsDatabase2.Products_Textsearch_Keywords_Database.containsKey(product)) {
			Product item = ProductsDatabase2.Products_Textsearch_Keywords_Database.get(product);
			currentSession.addItemToCart(item, quantity);
		}
		else {
			//TODO: Display Item not found
		}
	}

	// This is triggered from the INITIAL state
	public void addInkToStation(int stationNumber, int inkLevel) {
		SelfCheckoutStation station = customerStations.get(stationNumber).getStation();
		try {
			station.printer.addInk(inkLevel);
			customerStations.get(stationNumber).inkAdded += inkLevel;
			customerStations.get(stationNumber).setState(CustomerController.State.INITIAL);
		} catch (OverloadException e) {
			// TODO: Show attendant screen that too much ink was tried to be added
		}
	}
	public void addPaperToStation(int stationNumber, int paperLevel) {
		SelfCheckoutStation station = customerStations.get(stationNumber).getStation();
		try {
			station.printer.addPaper(paperLevel);
			customerStations.get(stationNumber).paperAdded += paperLevel;
			customerStations.get(stationNumber).setState(CustomerController.State.INITIAL);
		} catch (OverloadException e) {
			//TODO: Show attendant screen that too much paper was tried to be added
		}
	}
	
	public void updateView(JPanel newView) {
		JFrame frame = superStation.screen.getFrame();
		frame.getContentPane().removeAll();
		frame.getContentPane().add(newView);
		frame.revalidate();
		frame.repaint();
		System.out.print("swapping to " + newView);
	}

	public void removeItemfromStation(int stationNumber, Product product, double quantity) {
		CustomerController controller = customerStations.get(stationNumber);
		controller.getCurrentSession().removeItemFromCart(product, quantity);
	}

	public void addCustomerStation(CustomerController controller) {
		customerStations.add(controller);
	}

	public ArrayList<CustomerController> getCustomerStationsManaged() {
		return customerStations;
	}

	public void adjustBills(int stationNumber, int bills, int amountToAdd) throws OverloadException {
		SelfCheckoutStation station = customerStations.get(stationNumber).getStation();
		BillDispenser dispenser = station.billDispensers.get(bills);
		Bill bill = new Bill(bills, Currency.getInstance("CAD"));
		for (int i = 0; i < amountToAdd; i++) {
			dispenser.load(bill, bill);
		}
	}

	public void adjustCoins(int stationNumber, int coins, int amountToAdd) throws OverloadException {
		SelfCheckoutStation station = customerStations.get(stationNumber).getStation();
		BigDecimal value = BigDecimal.valueOf(coins);
		CoinDispenser dispenser = station.coinDispensers.get(value);
		Coin coin = new Coin(value, Currency.getInstance("CAD"));
		for (int i = 0; i < amountToAdd; i++) {
			dispenser.load(coin, coin);
		}
	}
	@Override
	public void reactToDisableStationRequest() {

	}

	@Override
	public void reactToEnableStationRequest() {

	}

	@Override
	public void reactToRemoveItemRequest(Product product, double quantity, CustomerStationLogic stationLogic) {
		int stationNumber = customerStations.indexOf(stationLogic);
		removedProductsRequest.get(stationNumber).put(product, quantity);
		String productName = "Request to remove " + quantity + " of " + product.toString() + " from station " + stationNumber;
		//TODO: Show this message in attendant view
	}

	@Override
	public void lowInkAlert(CustomerStationLogic stationLogic) {
		int stationNumber = customerStations.indexOf(stationLogic);
		//TODO: Show to attendant view the station number that needs ink
	}

	@Override
	public void lowPaperAlert(CustomerStationLogic stationLogic) {
		int stationNumber = customerStations.indexOf(stationLogic);
		//TODO: Show to attendant view the station number that needs paper
	}

	@Override
	public void onOverride(int stationNumber) {
		CustomerController customerController = customerStations.get(stationNumber-1);
		customerController.setState(customerController.stateSave);
	}

	@Override
	public void onStationShutdown(int stationNumber) {
		CustomerController customerController = customerStations.get(stationNumber-1);
		customerController.setState(State.SHUTDOWN);
		
	}

	@Override
	public void onStationTurnon(int stationNumber) {
		CustomerController customerController = customerStations.get(stationNumber-1);
		customerController.setState(State.STARTUP);
		
	}

	@Override
	public void onStationLock(int stationNumber) {
		CustomerController customerController = customerStations.get(stationNumber-1);
		customerController.setState(State.DISABLED);
	}

	@Override
	public void onStationUnlock(int stationNumber) {
		CustomerController customerController = customerStations.get(stationNumber-1);
		
		if (customerController.getCurrentState() == State.DISABLED) {
			customerController.setState(customerController.stateSave);
		}
	}

	@Override
	public void onAttendantLoginAttempt(AttendantAccount account) {
		System.out.println("a");
		if (startLogIn(account)) {
			System.out.println("working");
			updateView(aView.stationView);
		}
	}

	@Override
	public void onStationLogout() {
		// TODO Auto-generated method stub
		auth.logOut(null);
		
	}
	@Override
	public void onStationAddByTextPressed(int value) {
		// how does this fit??
	}

	@Override
	public void onStationRemoveItemPressed(int value) {
		// how does this fit??
	}
}
