// P3-4 Group Members
//
// Abdelrhafour, Achraf (30022366)
// Campos, Oscar (30057153)
// Cavilla, Caleb (30145972)
// Crowell, Madeline (30069333)
// Debebe, Abigia (30134608)
// Dhuka, Sara Hazrat (30124117)
// Drissi, Khalen (30133707)
// Ferreira, Marianna (30147733)
// Frey, Ben (30088566)
// Himel, Tanvir (30148868)
// Huayhualla Arce, Fabricio (30091238)
// Kacmar, Michael (30113919)
// Lee, Jeongah (30137463)
// Li, Ran (10120152)
// Lokanc, Sam (30114370)
// Lozano Cetina, Jose Camilo (30144736)
// Maahdie, Monmoy (30149094)
// Malik, Akansha (30056048)
// Mehedi, Abdullah (30154770)
// Polton, Scott (30138102)
// Rahman, Saadman (30153482)
// Rodriguez, Gabriel (30162544)
// Samin Rashid, Khondaker (30143490)
// Sloan, Jaxon (30123845)
// Tran, Kevin (30146900)
//


//package com.autovend.runnable;
//
//import java.math.BigDecimal;
//import java.util.Currency;
//
//import javax.swing.JButton;
//
//import com.autovend.Barcode;
//import com.autovend.BarcodedUnit;
//import com.autovend.Bill;
//import com.autovend.Coin;
//import com.autovend.Numeral;
//import com.autovend.PriceLookUpCode;
//import com.autovend.PriceLookUpCodedUnit;
//import com.autovend.ReusableBag;
//import com.autovend.devices.BillDispenser;
//import com.autovend.devices.CoinDispenser;
//import com.autovend.devices.OverloadException;
//import com.autovend.devices.ReusableBagDispenser;
//import com.autovend.devices.SelfCheckoutStation;
//import com.autovend.devices.SimulationException;
//import com.autovend.devices.SupervisionStation;
//import com.autovend.devices.TouchScreen;
//import com.autovend.external.ProductDatabases;
//import com.autovend.products.BarcodedProduct;
//import com.autovend.products.PLUCodedProduct;
//import com.autovend.software.attendant.AttendantController;
//import com.autovend.software.customer.CustomerController;
//import com.autovend.software.customer.CustomerStationLogic;
//import com.autovend.software.item.ProductsDatabase2;
//import com.autovend.software.ui.AttendantView;
//import com.autovend.software.ui.CustomerView;
//import com.autovend.software.ui.StationStatusView;
//
//public class AttendantTestMain {
//
//
//	public static void main(String[] args) throws InterruptedException, OverloadException {
//	
//		AttendantView stationView = new AttendantView(1);
//		
//
//		SupervisionStation attendantStation = new SupervisionStation();
//		attendantStation.screen.getFrame().add(stationView.loginView);
//		attendantStation.screen.getFrame().setVisible(true);
//		
//		
//		int[] billDenoms = { 5, 10, 15, 20, 50, 100 };
//		BigDecimal[] coinDenoms = { new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"),
//				new BigDecimal("1"), new BigDecimal("2") };
//		int scaleMaximumWeight = 1000;
//		int scaleSensitivity = 1;
//		SelfCheckoutStation station = new SelfCheckoutStation(Currency.getInstance("CAD"), billDenoms, coinDenoms,
//				scaleMaximumWeight, scaleSensitivity);
//		
//		ReusableBagDispenser dispenser = new ReusableBagDispenser(100);
//
//		int n = 0;
//		while (n < 100) {
//			dispenser.load(new ReusableBag());
//			n++;
//		}
//
//		for (int i = 0; i < billDenoms.length; i++) {
//			BillDispenser billDispenser = station.billDispensers.get(billDenoms[i]);
//			for (int j = 0; j < 100; j++) {
//				Bill bill = new Bill(billDenoms[i], Currency.getInstance("CAD"));
//				try {
//					billDispenser.load(bill);
//				} catch (SimulationException | OverloadException e) {
//				}
//			}
//		}
//		// Add 100 coins to each dispenser
//
//		for (int i = 0; i < coinDenoms.length; i++) {
//			CoinDispenser coinDispenser = station.coinDispensers.get(coinDenoms[i]);
//			for (int j = 0; j < 100; j++) {
//				Coin coin = new Coin(coinDenoms[i], Currency.getInstance("CAD"));
//				try {
//					coinDispenser.load(coin);
//				} catch (SimulationException | OverloadException e) {
//
//				}
//			}
//		}
//
//		station.printer.addInk(1000);
//		station.printer.addPaper(1000);
//
//		CustomerView customerView = new CustomerView();
//
//		CustomerController customerController = new CustomerController(station, dispenser, customerView);
//		
//		AttendantController attendantController = new AttendantController(attendantStation, stationView);
//		
//		
//		CustomerStationLogic logic = new CustomerStationLogic(station);
//		attendantController.addCustomerStation(customerController);
//		//stationView.loginView.register(attendantController);
//		stationView.textSearchView.register(attendantController);
//		
//		
//		
//		
//		
//		
//		
//	
//	}
//}