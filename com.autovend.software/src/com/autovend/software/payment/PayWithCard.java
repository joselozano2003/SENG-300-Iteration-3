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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;

import com.autovend.Card;
import com.autovend.Card.CardData;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CardReader;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.external.CardIssuer;
import com.autovend.software.BankIO;

@SuppressWarnings("serial")
class WithCard extends PaymentFacade implements CardReaderObserver {

	protected WithCard(SelfCheckoutStation station) {
		super(station);
		try {
			station.cardReader.register(this);
		} catch (Exception e) {
//			for (PaymentListener listener : listeners) {
//				listener.reactToHardwareFailure();
//		
//			}
		}
	}
	
	public boolean payByTap(Card card) throws IOException {
		BigDecimal value = getAmountDue();
		if(card == null) {
			throw new NullPointerException("Argument cannot be null!");
		}
		
		CardReader reader = this.station.cardReader;
		CardData cardData = reader.tap(card);
		
		if(cardData.getType()=="DebitCard" ^ cardData.getType()=="CreditCard") {
			return reactToCardDataReadEvent(reader, cardData);
		}
		else {
//			for (PaymentListener listener : listeners) {
//				listener.onPaymentFailureEvent(value);
//			}
			return false;
		}
	}
	
	public boolean payBySwipe(Card card, BufferedImage signature) throws IOException {
		BigDecimal value = getAmountDue();
		if(card == null) {
			throw new NullPointerException("Argument cannot be null!");
		}
		
		CardReader reader = this.station.cardReader;
		CardData cardData = reader.swipe(card, signature);
		
		if(cardData.getType()=="DebitCard" ^ cardData.getType()=="CreditCard") {
			return reactToCardDataReadEvent(reader, cardData);
		}
		else {
//			for (PaymentListener listener : listeners) {
//				listener.onPaymentFailureEvent(value);
//			}
			return false;
		}
	}
	
	public boolean payByInsert(Card card, String pin) throws IOException{
		BigDecimal value = getAmountDue();
		if(card == null) {
			throw new NullPointerException("Argument cannot be null!");
		}
		
		CardReader reader = this.station.cardReader;
		CardData cardData = reader.insert(card, pin);
		
		if(cardData.getType()=="DebitCard" ^ cardData.getType()=="CreditCard") {
			return reactToCardDataReadEvent(reader, cardData);
		}
		else {
//			for (PaymentListener listener : listeners) {
//				listener.onPaymentFailureEvent(value);
//			}
			return false;
		}
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
	@Override
	public void reactToCardInsertedEvent(CardReader reader) {}
	@Override
	public void reactToCardRemovedEvent(CardReader reader) {}
	@Override
	public void reactToCardTappedEvent(CardReader reader) {}
	@Override
	public void reactToCardSwipedEvent(CardReader reader) {}
	@Override
	public boolean reactToCardDataReadEvent(CardReader reader, CardData data) {
		BigDecimal value = getAmountDue();
		String cardIssuerName = data.getType();
		CardIssuer issuer = BankIO.getCardIssuers().get(cardIssuerName);
		if (issuer == null) {
//			for (PaymentListener listener : listeners) {
//				listener.onPaymentFailureEvent(value);
//			}
			return false;
		} else {
			int holdNumber = issuer.authorizeHold(data.getNumber(), value);

			if (holdNumber == -1) {
//				for (PaymentListener listener : listeners) {
//					listener.onPaymentFailureEvent(value);
//				}
				return false;
			} else {
				boolean transactionResult = issuer.postTransaction(data.getNumber(), holdNumber, value);
				if (transactionResult) {
					this.subtractAmountDue(value);
					reader.remove();
//					for (PaymentListener listener : listeners) {
//						listener.onPaymentSuccessfulEvent(value);
//						listener.onCardRemovedEvent();
//					}
					return true;
				} else {
//					for (PaymentListener listener : listeners) {
//						listener.onPaymentFailureEvent(value);
//					}
					return false;
				}
			}
		}
	}

	public void reactToEnabledEvent(AbstractDevice device) {
		// TODO Auto-generated method stub
		
	}

	public void reactToDisabledEvent(AbstractDevice device) {
		// TODO Auto-generated method stub
		
	}
}
