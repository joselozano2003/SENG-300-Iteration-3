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
import com.autovend.software.customer.CustomerSession;
import com.autovend.software.payment.PaymentListener;

@SuppressWarnings("serial")
public class ReceiptFacade extends AbstractFacade<ReceiptListener> {

	private SelfCheckoutStation selfCheckoutStation;
	private CustomerSession customerSession;

	public ReceiptFacade(SelfCheckoutStation selfCheckoutStation, CustomerSession currentSession) {
		this.selfCheckoutStation = selfCheckoutStation;
		this.customerSession = currentSession;
		this.selfCheckoutStation.printer.register(new InnerListener());
	}

	private class InnerListener implements ReceiptPrinterObserver {

		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
			for (ReceiptListener listener : listeners) {
				listener.onLowPaper();
			}

		}

		@Override
		public void reactToOutOfInkEvent(ReceiptPrinter printer) {
			for (ReceiptListener listener : listeners) {
				listener.onLowInk();
			}

		}

		@Override
		public void reactToPaperAddedEvent(ReceiptPrinter printer) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToInkAddedEvent(ReceiptPrinter printer) {
			// TODO Auto-generated method stub

		}

	}

	public void printReceipt() {
		Map<Product, Double> shoppingCart = customerSession.getShoppingCart();
		if (shoppingCart.size() == 0) {
			return;
		} else {
			try {
				for (Map.Entry<Product, Double> entry : shoppingCart.entrySet()) {
					Product product = entry.getKey();
					String itemName;
					if (product instanceof BarcodedProduct) {
						itemName = ((BarcodedProduct) product).getDescription();
					} else if (product instanceof PLUCodedProduct) {
						itemName = ((PLUCodedProduct) product).getDescription();
					} else {
						itemName = "Unknown";
						// should never happen
					}

					String itemCost = product.getPrice().toString();
					String receiptLine = itemName + "    " + itemCost + "\n";
					char[] subStrings = receiptLine.toCharArray();
					for (char c : subStrings) {
						this.selfCheckoutStation.printer.print(c);
					}
				}
				this.selfCheckoutStation.printer.cutPaper();
				for (ReceiptListener listener : listeners) {
					listener.onPrinted();
				}
			} catch (EmptyException | OverloadException e) {
				for (ReceiptListener listener : listeners) {
					listener.reactToHardwareFailure(this.selfCheckoutStation.printer);;
				}
			}
		}
	}

}
