package com.autovend.software.item;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import com.autovend.Barcode;
import com.autovend.Card.CardData;
import com.autovend.PriceLookUpCode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillValidator;
import com.autovend.devices.CardReader;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.devices.observers.CoinValidatorObserver;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.software.AbstractFacade;
import com.autovend.software.customer.CustomerSession;

public class ItemAdditionFacade extends AbstractFacade<ItemAdditionListener> {

	private SelfCheckoutStation selfCheckoutStation;
	private CustomerSession customerSession;

	private class InnerListener
			implements BarcodeScannerObserver/* , PLUCodeEnteredObserver(UI), ItemBrowsingObserver(UI) */ {

		@Override
		public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// TODO Auto-generated method stub

		}

		@Override
		public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
			BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			for (ItemAdditionListener listener : listeners) {
				listener.reactToItemAddedEvent(product, 1);
			}

		}
		
		public void reactToPriceLookUpCodeEnteredEvent(PriceLookUpCode priceLookUpCode, double quantity) {
			PLUCodedProduct product = ProductDatabases.PLU_PRODUCT_DATABASE.get(priceLookUpCode);
			for (ItemAdditionListener listener : listeners) {
				listener.reactToItemAddedEvent(product, quantity);
			}

		}

	}

	public ItemAdditionFacade(SelfCheckoutStation selfCheckoutStation, CustomerSession customerSession) {
		this.selfCheckoutStation = selfCheckoutStation;
		this.selfCheckoutStation.mainScanner.register(new InnerListener());
		this.selfCheckoutStation.handheldScanner.register(new InnerListener());

	}

	

}
