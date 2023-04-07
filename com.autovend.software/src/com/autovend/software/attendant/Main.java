package com.autovend.software.attendant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.customer.CustomerStationLogic;

public class Main {

	public static void main(String[] args) {
		
		AttendantModel model = new AttendantModel();
		AttendantView view = new AttendantView();
		AttendantController controller = new AttendantController(model, view, setupCustomerStations(2));
		view.setVisible(true);
	}
	
	/**
	 * Set up the physical customer stations in the Company's store. 
	 * @param amount The amount of fixed stations available.
	 */
	public static List<CustomerStationLogic> setupCustomerStations(int amount) {
		ArrayList<CustomerStationLogic> stationList = new ArrayList<CustomerStationLogic>();
		for (int i = 0; i < amount; i++) {
			int[] billDenoms = {5, 10 , 15, 20, 50, 100};
			BigDecimal[] coinDenoms = {new BigDecimal("0.05"), new BigDecimal("0.10"), 
					new BigDecimal("0.25"), new BigDecimal("1"), new BigDecimal("2")};
			int scaleMaximumWeight = 50;
			int scaleSensitivity = 10;
			SelfCheckoutStation station = new SelfCheckoutStation(Currency.getInstance("CAD"),
					billDenoms, coinDenoms, scaleMaximumWeight, scaleSensitivity);
			stationList.add(CustomerStationLogic.installOn(station));
		}
		return stationList;
	}

}
