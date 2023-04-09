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

import java.util.List;

import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.Product;
import com.autovend.software.customer.CustomerStationLogic;
import com.autovend.software.item.ItemFacade;

import auth.AttendantAccount;
import auth.AuthFacade;

public class AttendantController {

	private static List<CustomerStationLogic> customerStations;
	private AttendantModel model;
	private AttendantView view;
	private AuthFacade auth;

	public AttendantController(AttendantModel model, AttendantView view, List<CustomerStationLogic> list) {
		if (model == null || view == null || list == null)
			throw new NullPointerException("Null arguments given");
		this.model = model;
		this.view = view;
		this.auth = new AuthFacade();
		customerStations = list;
	}

	public void startupStation(/* station */) {

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

	public boolean startRemoveItem(ItemFacade item, Product product) {
		return item.removeProduct(product);
	}

	public void addInkToStation(int stationNumber, int inkLevel) throws OverloadException {
		SelfCheckoutStation station = customerStations.get(stationNumber).getStation();
		station.printer.addInk(inkLevel);
	}
}
