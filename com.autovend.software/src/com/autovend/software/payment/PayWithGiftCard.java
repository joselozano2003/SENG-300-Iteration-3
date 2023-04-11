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
package com.autovend.software.payment;

import java.math.BigDecimal;

import com.autovend.Card.CardData;
import com.autovend.ChipFailureException;
import com.autovend.GiftCard;
import com.autovend.GiftCard.GiftCardInsertData;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CardReader;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.external.CardIssuer;
import com.autovend.software.BankIO;
import com.autovend.software.ui.CustomerView;

@SuppressWarnings("serial")
public class PayWithGiftCard extends PaymentFacade implements CardReaderObserver {

	public PayWithGiftCard(SelfCheckoutStation station, CustomerView customerView) {
		super(station, customerView, true);

		station.cardReader.register(this);

	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
	}

	@Override
	public void reactToCardInsertedEvent(CardReader reader) {
	}

	@Override
	public void reactToCardRemovedEvent(CardReader reader) {
	}

	@Override
	public void reactToCardTappedEvent(CardReader reader) {
	}

	@Override
	public void reactToCardSwipedEvent(CardReader reader) {
	}

	@Override
	public void reactToCardDataReadEvent(CardReader reader, CardData data) {
		if (data instanceof GiftCard.GiftCardInsertData) {
			BigDecimal value = getAmountDue();

			if (!GiftCardDatabase.isGiftCard(data.getNumber())) {
				for (PaymentEventListener listener : listeners) {
					listener.onPaymentFailure();
				}
				return;
			}

			data = (GiftCard.GiftCardInsertData) data;

			try {
				if (((GiftCardInsertData) data).deduct(value)) {
					subtractAmountDue(value);
					reader.remove();
					for (PaymentEventListener listener : listeners) {
						listener.onPaymentAddedEvent(value);
					}
				} else {
					for (PaymentEventListener listener : listeners) {
						listener.onPaymentFailure();
					}
				}
			} catch (ChipFailureException e) {
				for (PaymentEventListener listener : listeners) {
					listener.onPaymentFailure();
				}
			}

		}
	}
}
