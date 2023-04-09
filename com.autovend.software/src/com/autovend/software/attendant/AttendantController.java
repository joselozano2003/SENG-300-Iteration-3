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

	public AttendantController(AttendantModel model, List<CustomerStationLogic> list) {
		if (model == null || list == null)
			throw new NullPointerException("Null arguments given");
		this.model = model;
		this.auth = new AuthFacade();
		customerStations = list;
		// Construct attendant view. For now, just set language to english.
		this.view = new AttendantView(this, customerStations.size(), "ENG");
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
	
	// Called when weight discrepancy is detected. Notifies attendant, who can resolve.
	public void alertWeightDiscrepancy(int stationNumber, double discrepancy) {
		view.notifyWeightDiscrepancy(stationNumber, discrepancy);
	}
	
	// Called if weight discrepancy is resolved by customer adding/removing items. Updates GUI.
	public void alertWeightDiscrepancyResolved(int stationNumber) {
		view.weightDiscrepancyResolved(stationNumber);
	}
	
	// Called to prompt attendant to approve bags.
	public void alertBagApproval(int stationNumber) {
		view.notifyBagApproval(stationNumber);
	}
	
	// Called by AttendantView when attendant wants to override a weight discrepancy at specified station.
	public void overrideWeightDiscrepancy(int stationNumber) {
		
	}
	
	// Called by AttendantView when attendant approves bags at specified station.
	public void bagsApproved(int stationNumber) {
		
	}
	
	// Called by AttendantView when attendant rejects bags. CustomerView should be signalled and
	// customer should be prompted to remove bags.
	public void bagsRejected(int stationNumber) {
		
	}
	
}
