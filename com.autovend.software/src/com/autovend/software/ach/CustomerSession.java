package com.autovend.software.ach;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.math.RoundingMode;

import com.autovend.*;
import com.autovend.Card.CardData;
import com.autovend.devices.*;
import com.autovend.devices.observers.*;
import com.autovend.external.*;
import com.autovend.products.*;
import com.autovend.software.ach.virtual.OutOfBagsException;
import com.autovend.software.ach.virtual.VirtualBagDispenser;

/*
 * Group Members: Achraf Abdelrhafour: (30022366), Marianna Ferreira (30147733),
 * Ryan Chrumka (30144174), Alireza Vafisani (30150496), Ali Savab Pour
 * (30154744), Aryan Nambiar (30140671), Shijia Wang (30018276), Carson Bergen
 * (30127827), Md Abdullah Mehedi Patwary (30154770), Vita Vysochina (30118374),
 * Michael Kacmar, (30113919), Deepshikha Dhammi (30140157)
 */

/*
 * Description: Manages the shopping cart and payment processing. This class has methods to add items to the cart, calculate total cost, process cash and card payments, dispense change, and print receipts.
 * Focused on maintaining the state and data specific to an individual customer's transaction. It keeps track of the products in the customer's shopping cart, the total cost of the items, the amount paid, and the change due, among other things.
 * The session also provides methods for adding items to the cart, processing payments, verifying weights, and printing receipts. The session is an instance-specific representation of the customer's interaction with the self-checkout system.
 */
public class CustomerSession {

	public SelfCheckoutStation selfCheckoutStation;

	private VirtualBagDispenser bagDispenser;
	private BarcodedProduct reusableBagProduct;

	private BigDecimal totalCost = BigDecimal.ZERO;
	private BigDecimal totalPaid = BigDecimal.ZERO;
	private BigDecimal changeDispensed = BigDecimal.ZERO;
	private BigDecimal changeDue = BigDecimal.ZERO;
	private BigDecimal payAmount = BigDecimal.ZERO;
	
	//Used for testing
	public boolean receiptPrinted = false;

	private double expectedWeight = 0.0;
	private double allowableTolerance = 0.05; // default value for weight scale in bagging area

	private ArrayList<Product> shoppingCart;

	public Map<String, CardIssuer> cardIssuers;

	public CustomerSession(SelfCheckoutStation selfCheckoutStation, BarcodedProduct reusableBagProduct,
			VirtualBagDispenser bagDispenser) {
		this.selfCheckoutStation = selfCheckoutStation;
		this.bagDispenser = bagDispenser;

		this.reusableBagProduct = reusableBagProduct;

		shoppingCart = new ArrayList<>();

		cardIssuers = new HashMap<>();
	}

	public void addBarcodedItemToCart(BarcodedProduct product) {
		shoppingCart.add(product);
		totalCost = totalCost.add(product.getPrice());
		expectedWeight += product.getExpectedWeight();

	}

	public void addPLUCodedItemToCart(PLUCodedProduct product, double weightToBuy) {

		shoppingCart.add(product);
		totalCost = totalCost.add(product.getPrice());
		expectedWeight += weightToBuy;

	}

	public void processCashPayment(BigDecimal amount) {
		totalPaid = totalPaid.add(amount);

	}

	public void processCardPayment(CardData data) {

		String cardIssuerName = data.getType();
		CardIssuer issuer = cardIssuers.get(cardIssuerName);
		if (issuer == null) {
			// System.out.println("Issuer not found");
		} else {
			int holdNumber = issuer.authorizeHold(data.getNumber(), payAmount);

			if (holdNumber == -1) {
				// System.out.println("Insufficient hold - try another payment method. ");
			} else {
				boolean transcationResult = issuer.postTransaction(data.getNumber(), holdNumber, payAmount);
				if (transcationResult == true) {
					totalPaid = totalPaid.add(payAmount);

				} else {
					// System.out.println("Transaction failed. ");

				}

			}

		}

	}

	public boolean isPaymentComplete() {
		if (totalPaid.compareTo(totalCost) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public void dispenseChange() {
		changeDue = totalPaid.subtract(totalCost);

		if (changeDue.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Change required must be non-negative.");
		}

		// Dispense bills
		for (int i = selfCheckoutStation.billDenominations.length - 1; i >= 0; i--) {
			int billDenomination = selfCheckoutStation.billDenominations[i];
			BillDispenser billDispenser = selfCheckoutStation.billDispensers.get(billDenomination);
			BigDecimal billValue = BigDecimal.valueOf(billDenomination);
			while (changeDue.compareTo(billValue) >= 0 && billDispenser.size() > 0) {
				try {
					billDispenser.emit();
					changeDue = changeDue.subtract(billValue);
					totalCost = changeDue.negate();
					changeDispensed = changeDispensed.add(billValue);
					// danglingChange = true; can get this through the hardware
					// currentState = State.DISABLE_STATION;
					// return;
				} catch (DisabledException e) {
				} catch (EmptyException e) {
					// should never happen
				} catch (OverloadException e) {
				}

			}
		}

		// Dispense coins
		for (int i = selfCheckoutStation.coinDenominations.size() - 1; i >= 0; i--) {
			BigDecimal coinDenomination = selfCheckoutStation.coinDenominations.get(i);
			CoinDispenser coinDispenser = selfCheckoutStation.coinDispensers.get(coinDenomination);

			while (changeDue.compareTo(coinDenomination) >= 0 && coinDispenser.size() > 0) {

				try {
					coinDispenser.emit();
					changeDue = changeDue.subtract(coinDenomination);
					changeDispensed = changeDispensed.add(coinDenomination);
				} catch (DisabledException e) {
				} catch (OverloadException e) {
					// Notify customer to remove some coins from the tray
				} catch (EmptyException e) {
				}

			}
		}

		// Edge case for if the change due is less than the smallest coin denomination
		if (changeDue.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal smallestCoinDenomination = selfCheckoutStation.coinDenominations.get(0);
			CoinDispenser smallestCoinDispenser = selfCheckoutStation.coinDispensers.get(smallestCoinDenomination);
			try {
				smallestCoinDispenser.emit();
				changeDispensed = changeDispensed.add(smallestCoinDenomination);
			} catch (DisabledException | OverloadException | EmptyException e) {
				// notify attendant

			}

		}

	}

	public void resetSession() {

		shoppingCart.clear();
		totalCost = BigDecimal.ZERO;
		totalPaid = BigDecimal.ZERO;
		changeDispensed = BigDecimal.ZERO;
		expectedWeight = 0.0;
		changeDue = BigDecimal.ZERO;
	}

	public boolean verifyWeight() throws WeightDiscrepancyException {
		try {
			if (Math.abs(selfCheckoutStation.baggingArea.getCurrentWeight() - expectedWeight) < allowableTolerance) {
				return true;
			} else {
				throw new WeightDiscrepancyException();
			}
		} catch (OverloadException e) {
			return false;

		}

	}

	public void printReceipt() {

		if (shoppingCart.size() == 0) {
			// System.out.println("There are no items in your Cart. Receipt cannot be
			// printed!");
		} else {
			try {
				for (Product product : shoppingCart) {
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
						receiptPrinted = true;
					}
				}
				this.selfCheckoutStation.printer.cutPaper();
				System.out.println(this.selfCheckoutStation.printer.removeReceipt());
				System.out.println("Receipt Printed!");
			} catch (EmptyException | OverloadException e) {
				System.out.println(
						"Something went Wrong(out of paper or ink, something wrong with receipt, wait for an attendant!");
				receiptPrinted = false;
			}

		}

	}

	public boolean dispenseBags(int numberOfBags) throws OutOfBagsException {
		for (int i = 0; i < numberOfBags; i++) {
			try {
				bagDispenser.dispenseBag();
			} catch (OutOfBagsException e) {
				// Notify client that bags cannot be dispensed at the current time
				throw e;
			} catch (SimulationException e) {
				return false;
			}
			addBarcodedItemToCart(reusableBagProduct);
		}

		return true;
	}

	public void setExpectedWeight(double weightToSet) {
		this.expectedWeight = weightToSet;
	}

	public void setTotalCost(BigDecimal costToSet) {
		this.totalCost = costToSet;
	}

	public void setPayAmount(BigDecimal amountToSet) {
		this.payAmount = amountToSet;
	}
	
	public void setTotalPaid(BigDecimal amountToSet) {
		this.totalPaid = amountToSet;
	}

	public BigDecimal getTotalCost() {
		return this.totalCost;
	}

	public BigDecimal getTotalPaid() {
		return this.totalPaid;
	}

	public BigDecimal getPayAmount() {
		return this.payAmount;
	}

	public Map<String, CardIssuer> getCardIssuers() {
		return this.cardIssuers;
	}

	public ArrayList<Product> getShoppingCart() {
		return this.shoppingCart;
	}

}