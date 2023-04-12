package com.autovend.runnable;

import java.math.BigDecimal;
import java.util.Currency;

import javax.swing.JButton;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.Numeral;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.ReusableBag;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReusableBagDispenser;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.SupervisionStation;
import com.autovend.devices.TouchScreen;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.attendant.AttendantController;
import com.autovend.software.customer.CustomerController;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.ui.CustomerView;
import com.autovend.software.ui.StationStatusView;

public class AttendantTestMain {


	public static void main(String[] args) throws InterruptedException, OverloadException {
	

		TouchScreen touchScreen = new TouchScreen();
		StationStatusView stationView = new StationStatusView(2);
		
		touchScreen.getFrame().add(stationView);
		touchScreen.setVisible(true);
	
	}
}