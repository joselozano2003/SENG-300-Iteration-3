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

import java.util.*;

import com.autovend.Bill;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.products.Product;
import com.autovend.software.customer.*;
import com.autovend.software.item.ItemFacade;

import auth.AttendantAccount;
import auth.AuthFacade;
import com.autovend.software.item.ProductsDatabase2;

public class AttendantController implements CustomerStationListener {

	private static ArrayList<CustomerStationLogic> customerStations;
	private AttendantModel model;
	private AttendantView view;
	private AuthFacade auth;
	private ArrayList<HashMap<Product, Double>> removedProductsRequest;
	//testing

	public AttendantController(AttendantModel model, AttendantView view) {
		if (model == null || view == null)
			throw new NullPointerException("Null arguments given");
		this.model = model;
		this.view = view;
		this.auth = new AuthFacade();
		customerStations = new ArrayList<CustomerStationLogic>();
		removedProductsRequest = new ArrayList<HashMap<Product, Double>>();
	}

	public boolean startLogIn(AttendantAccount attendantAccount) {
		return auth.logIn(attendantAccount);
	}

	public boolean startLogOut(AttendantAccount attendantAccount) {
		return auth.logOut(attendantAccount);
	}

	public boolean startAddAccount(AttendantAccount attendantAccount, AttendantAccount addedAccount) {
		return auth.addAccount(attendantAccount, addedAccount);
	}

	public boolean startDeleteAccount(AttendantAccount attendantAccount, AttendantAccount removeAccount) {
		return auth.deleteAccount(attendantAccount, removeAccount);
	}

	// TODO: Gets triggered from GUI
	public void addItemToStationByTextSearch(int stationNumber, String product, double quantity) {
		CustomerStationLogic station = customerStations.get(stationNumber);
		CustomerSession currentSession = station.getController().getCurrentSession();
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
		SelfCheckoutStation station = customerStations.get(stationNumber).getController().getStation();
		try {
			station.printer.addInk(inkLevel);
			customerStations.get(stationNumber).getController().inkAdded += inkLevel;
			customerStations.get(stationNumber).getController().setState(CustomerController.State.INITIAL);
		} catch (OverloadException e) {
			// TODO: Show attendant screen that too much ink was tried to be added
		}
	}
	public void addPaperToStation(int stationNumber, int paperLevel) {
		SelfCheckoutStation station = customerStations.get(stationNumber).getController().getStation();
		try {
			station.printer.addPaper(paperLevel);
			customerStations.get(stationNumber).getController().paperAdded += paperLevel;
			customerStations.get(stationNumber).getController().setState(CustomerController.State.INITIAL);
		} catch (OverloadException e) {
			//TODO: Show attendant screen that too much paper was tried to be added
		}
	}

	public void removeItemfromStation(int stationNumber, Product product, double quantity) {
		CustomerStationLogic station = customerStations.get(stationNumber);
		station.getController().getCurrentSession().removeItemFromCart(product, quantity);
	}

	public void addCustomerStation(CustomerStationLogic station) {
		customerStations.add(station);
	}

	public List<CustomerStationLogic> getCustomerStations() {
		return customerStations;
	}

	public void shutDownStation(int stationNumber) {
		customerStations.get(stationNumber).getController().setState(CustomerController.State.SHUTDOWN);
	}

	public void startUpStation(int stationNumber) {
		customerStations.get(stationNumber).getController().setState(CustomerController.State.STARTUP);
	}

	public void permitStationUse(int stationNumber) {
		customerStations.get(stationNumber).getController().setState(CustomerController.State.INITIAL);
	}

	public void denyStationUse(int stationNumber) {
		customerStations.get(stationNumber).getController().setState(CustomerController.State.DISABLED);
	}

	public void reEnableStationUse(int stationNumber) {
		customerStations.get(stationNumber).getController().setState(CustomerController.State.ADDING_ITEMS);
	}

	public void adjustBills(int stationNumber, int bills, int amountToAdd) throws OverloadException {
		SelfCheckoutStation station = customerStations.get(stationNumber).getController().getStation();
		BillDispenser dispenser = station.billDispensers.get(bills);
		Bill bill = new Bill(bills, Currency.getInstance("CAD"));
		for (int i = 0; i < amountToAdd; i++) {
			dispenser.load(bill, bill);
		}

	}

	@Override
	public void reactToHardwareFailure() {

	}

	@Override
	public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {

	}

	@Override
	public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {

	}

	@Override
	public void reactToDisableStationRequest() {
		//TODO: Send message to attendant view
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
}
