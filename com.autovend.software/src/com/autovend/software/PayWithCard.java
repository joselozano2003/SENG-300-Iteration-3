///* P3-4 Group Members
// *
// * Abdelrhafour, Achraf (30022366)
// * Campos, Oscar (30057153)
// * Cavilla, Caleb (30145972)
// * Crowell, Madeline (30069333)
// * Debebe, Abigia (30134608)
// * Dhuka, Sara Hazrat (30124117)
// * Drissi, Khalen (30133707)
// * Ferreira, Marianna (30147733)
// * Frey, Ben (30088566)
// * Himel, Tanvir (30148868)
// * Huayhualla Arce, Fabricio (30091238)
// * Kacmar, Michael (30113919)
// * Lee, Jeongah (30137463)
// * Li, Ran (10120152)
// * Lokanc, Sam (30114370)
// * Lozano Cetina, Jose Camilo (30144736)
// * Maahdie, Monmoy (30149094)
// * Malik, Akansha (30056048)
// * Mehedi, Abdullah (30154770)
// * Polton, Scott (30138102)
// * Rahman, Saadman (30153482)
// * Rodriguez, Gabriel (30162544)
// * Samin Rashid, Khondaker (30143490)
// * Sloan, Jaxon (30123845)
// * Tran, Kevin (30146900)
// */
//package com.autovend.software;
//
//import java.math.BigDecimal;
//
//import com.autovend.Card.CardData;
//import com.autovend.devices.AbstractDevice;
//import com.autovend.devices.CardReader;
//import com.autovend.devices.SelfCheckoutStation;
//import com.autovend.devices.SimulationException;
//import com.autovend.devices.observers.AbstractDeviceObserver;
//import com.autovend.devices.observers.CardReaderObserver;
//import com.autovend.external.CardIssuer;
//
//@SuppressWarnings("serial")
//public class PayWithCard extends Pay implements CardReaderObserver {
//
//	private BigDecimal amountToPay;
//	private CardIssuer cardIssuer;
//
//	public PayWithCard(SelfCheckoutStation station, CardIssuer cardIssuer) {
//		super(station);
//		BigDecimal amountToPay = PurchasedItems.getAmountLeftToPay();
//
//		if (cardIssuer == null) {
//            throw new SimulationException(new NullPointerException("No argument may be null."));
//        }
//
//		// Ensure that no change is produced when paying with card
//		if (amountToPay.compareTo(super.getAmountDue().subtract(PurchasedItems.getAmountPaid())) > 0) {
//			this.amountToPay = super.getAmountDue().subtract(PurchasedItems.getAmountPaid());
//		} else this.amountToPay = amountToPay;
//
//		this.cardIssuer = cardIssuer;
//	}
//
//	@Override
//	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
//	@Override
//	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {}
//	@Override
//	public void reactToCardInsertedEvent(CardReader reader) {}
//	@Override
//	public void reactToCardRemovedEvent(CardReader reader) {}
//	@Override
//	public void reactToCardTappedEvent(CardReader reader) {}
//	@Override
//	public void reactToCardSwipedEvent(CardReader reader) {}
//	@Override
//	public void reactToCardDataReadEvent(CardReader reader, CardData data) {
//		int holdNumber = cardIssuer.authorizeHold(data.getNumber(), PurchasedItems.getAmountLeftToPay()); 						  	// Contact card issuer and attempt to place a hold
//		if (holdNumber == -1) return; 																		// Return if hold is unable to be placed
//		boolean transactionPosted = cardIssuer.postTransaction(data.getNumber(), holdNumber, PurchasedItems.getAmountLeftToPay()); 	// Contact card issuer to attempt to post transaction
//		if (transactionPosted) super.pay(PurchasedItems.getAmountLeftToPay()); 														// If transaction is posted, pay the amount
//	}
//}
