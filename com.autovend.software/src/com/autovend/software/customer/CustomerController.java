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
package com.autovend.software.customer;

import java.math.BigDecimal;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.products.Product;
import com.autovend.software.bagging.BaggingFacade;
import com.autovend.software.bagging.BaggingListener;
import com.autovend.software.communication.CustomerIOListener;
import com.autovend.software.item.ItemAdditionFacade;
import com.autovend.software.item.ItemAdditionListener;
import com.autovend.software.payment.PaymentFacade;
import com.autovend.software.payment.PaymentListener;
import com.autovend.software.receipt.ReceiptFacade;
import com.autovend.software.receipt.ReceiptListener;

public class CustomerController implements BaggingListener, CustomerIOListener, ItemAdditionListener, PaymentListener, ReceiptListener  {

	private SelfCheckoutStation selfCheckoutStation;
	private CustomerSession currentSession;
	
	private PaymentFacade paymentFacade;
	private ItemAdditionFacade itemAdditionFacade;
	private ReceiptFacade receiptPrinterFacade;
	private BaggingFacade baggingFacade;
	
	


	public CustomerController(SelfCheckoutStation selfCheckoutStation) {
		this.selfCheckoutStation = selfCheckoutStation;
		this.currentSession = new CustomerSession();
		this.paymentFacade = new PaymentFacade(selfCheckoutStation, currentSession);
		this.itemAdditionFacade = new ItemAdditionFacade(selfCheckoutStation, currentSession);
		this.receiptPrinterFacade = new ReceiptFacade(selfCheckoutStation, currentSession);
		this.baggingFacade = new BaggingFacade(selfCheckoutStation, currentSession);

		
		// Register the CustomerController as a listener for the facades
		paymentFacade.register(this);
		itemAdditionFacade.register(this);
		receiptPrinterFacade.register(this);
		baggingFacade.register(this);
		
	}

	

	@Override
	public void reactToHardwareFailure(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
		device.disable();

	}

	@Override
	public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {
		device.enable();

	}

	@Override
	public void reactToDisableStationRequest() {

	}

	@Override
	public void reactToEnableStationRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToItemAddedEvent(Product product, double quantity) {
		currentSession.addItemToCart(product, quantity);

	}

	@Override
	public void onPrinted() {
		
		
	}

	@Override
	public void onPaymentSuccessful(BigDecimal amount) {
		currentSession.addPayment(amount);
	}

	@Override
	public void onPaymentFailure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChangeDispensed() {
		
	}

	@Override
	public void onChangeDispensedFailure() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onLowInk() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onLowPaper() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onWeightChanged(double weightInGrams) {
		try {
			boolean expectedWeightEqualsActual = (currentSession.getExpectedWeight() == selfCheckoutStation.baggingArea.getCurrentWeight());
			if(expectedWeightEqualsActual) {
				//set state back to adding items
			} else {
				// should disable station, notify attendant
			}
		} catch (OverloadException e) {
			
		}
		
	}

}
