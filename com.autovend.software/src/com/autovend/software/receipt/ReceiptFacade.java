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
package com.autovend.software.receipt;

import java.math.BigDecimal;
import java.util.Map;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.AbstractFacade;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.ui.CustomerView;

@SuppressWarnings("serial")
public class ReceiptFacade extends AbstractFacade<ReceiptEventListener> {

	public ReceiptFacade(SelfCheckoutStation station, CustomerView customerView) {
		super(station, customerView);

		station.printer.register(new InnerListener());

	}

	private class InnerListener implements ReceiptPrinterObserver {
		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		}

		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		}

		@Override
		public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
			for (ReceiptEventListener listener : listeners)
				listener.onReceiptPrinterFailed();
		}

		@Override
		public void reactToOutOfInkEvent(ReceiptPrinter printer) {
			for (ReceiptEventListener listener : listeners)
				listener.onReceiptPrinterFailed();
		}

		@Override
		public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		}

		@Override
		public void reactToInkAddedEvent(ReceiptPrinter printer) {
		}
	}

	public void printReceipt(Map<Product, Double> shoppingCart) {
	    StringBuilder receiptText = new StringBuilder();
	    receiptText.append("======= RECEIPT =======\n\n");

	    receiptText.append(String.format("%-40s  %10s  %10s\n", "Name", "Quantity", "Total Price"));
	    receiptText.append(String.format("%-40s  %10s  %10s\n", "----", "--------", "----------"));

	    for (Map.Entry<Product, Double> entry : shoppingCart.entrySet()) {
	        Product product = entry.getKey();
	        String name;
			name = "";
	        if (product instanceof BarcodedProduct) {
	            name = ((BarcodedProduct) product).getDescription();
	        } else if (product instanceof PLUCodedProduct) {
	            name = ((PLUCodedProduct) product).getDescription();
	        } else if (product instanceof ReusableBagProduct) {
	            name = "Reusable Bag";
	        }

	        double quantity = entry.getValue();
	        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

	        receiptText.append(String.format("%-40s  %10.2f  %10.2f\n", name, quantity, totalPrice));
	    }

	    receiptText.append("\n========================\n");

	    try {
	        for (char c : receiptText.toString().toCharArray()) {
	            station.printer.print(c);
	        }
	        station.printer.cutPaper();
	        for (ReceiptEventListener listener : listeners) {
	            listener.onReceiptPrintedEvent(receiptText);
	        }
	    } catch (OverloadException | EmptyException e) {
	        
	    }
	}
}
