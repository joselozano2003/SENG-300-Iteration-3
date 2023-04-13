package com.autovend.devices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioSystem;

import com.autovend.Bill;
import com.autovend.Coin;

/**
 * Simulates the overall self-checkout station.
 * <p>
 * A self-checkout possesses the following units of hardware that the customer
 * can see and interact with:
 * <ul>
 * <li>two electronic scales (one in the bagging area and one near the scanner),
 * with a configurable maximum weight before it overloads;</li>
 * <li>one reusable bag dispenser;</li>
 * <li>one touch screen;</li>
 * <li>one receipt printer;</li>
 * <li>one card reader;</li>
 * <li>two scanners (the main one and the handheld one);</li>
 * <li>one input slot for bills;</li>
 * <li>one output slot for bills;</li>
 * <li>one input slot for coins;</li>
 * <li>one output tray for coins; and,</li>
 * <li>one speaker for audio output (note: you should directly use the
 * {@link AudioSystem} class, if you want to produce sounds).</li>
 * </ul>
 * </p>
 * <p>
 * In addition, these units of hardware are accessible to personnel with a key
 * to unlock the front of the station:
 * <li>one bill storage unit, with configurable capacity;</li>
 * <li>one or more bill dispensers, one for each supported denomination of bill,
 * as configured;</li>
 * <li>one coin storage unit, with configurable capacity; and,</li>
 * <li>one or more coin dispensers, one for each supported denomination of coin,
 * as configured.</li>
 * </ul>
 * </p>
 * <p>
 * And finally, there are certain, additional units of hardware that would only
 * be accessible to someone with the appropriate tools (like a screwdriver,
 * crowbar, or sledge hammer):
 * <ul>
 * <li>one bill validator; and</li>
 * <li>one coin validator.</li>
 * </ul>
 * </p>
 * <p>
 * Many of these devices are interconnected, to permit coins or bills to pass
 * between them. Specifically:
 * <ul>
 * <li>the coin slot is connected to the coin validator (this is a
 * one-directional chain of devices);</li>
 * <li>the coin validator is connected to each of the coin dispensers (i.e., the
 * coin dispensers can be replenished with coins entered by customers), to the
 * coin storage unit (for any overflow coins that do not fit in the dispensers),
 * and to the coin tray for any rejected coins either because the coins are
 * invalid or because even the overflow storage unit is full (this is a
 * one-directional chain of devices);
 * <li>each coin dispenser is connected to the coin tray, to provide change
 * (this is a one-directional chain of devices);</li>
 * <li>the bill input slot is connected to the bill validator (this is a
 * <b>two</b>-directional chain of devices as an entered bills that are rejected
 * by the validator can be returned to the customer);</li>
 * <li>the bill validator is connected to the bill storage unit (this is a
 * one-directional chain of devices); and,</li>
 * <li>each bill dispenser is connected to the output bill slot; these
 * dispensers cannot be replenished by bills provided by customers (this is a
 * one-directional chain of devices).</li>
 * </ul>
 * </p>
 * <p>
 * All other functionality of the system must be performed in software,
 * installed on the self-checkout station through custom listener classes
 * implementing the various listener interfaces provided.
 * </p>
 * <p>
 * Note that bill denominations are required to be positive integers, while coin
 * denominations are positive decimal values ({@link BigDecimal} is used for the
 * latter to avoid roundoff problems arising from floating-point operations).
 */
public class SelfCheckoutStation {
	private BidirectionalChannel<Bill> validatorSource;
	private SupervisionStation supervisor = null;

	/**
	 * The electronic scale in the scanning area.
	 */
	public final ElectronicScale scale;

	/**
	 * The electronic scale in the bagging area.
	 */
	public final ElectronicScale baggingArea;

	/**
	 * The touch screen.
	 */
	public final TouchScreen screen;
	
	/**
	 * The printer.
	 */
	public final ReceiptPrinter printer;

	/**
	 * The card reader.
	 */
	public final CardReader cardReader;

	/**
	 * The main barcode scanner in the scanning area.
	 */
	public final BarcodeScanner mainScanner;

	/**
	 * A secondary, handheld scanner.
	 */
	public final BarcodeScanner handheldScanner;

	/**
	 * The reusable bag dispenser.
	 */
	public final ReusableBagDispenser bagDispenser;

	/**
	 * The default capacity of the reusable bag dispenser.
	 */
	public final static int BAG_DISPENSER_CAPACITY = 100;
	
	/**
	 * The slot for inserting bills.
	 */
	public final BillSlot billInput;

	/**
	 * The slot for returning bills as change.
	 */
	public final BillSlot billOutput;

	/**
	 * The device that validates bills.
	 */
	public final BillValidator billValidator;

	/**
	 * The device that stores bills.
	 */
	public final BillStorage billStorage;

	/**
	 * The default capacity of the bill storage device.
	 */
	public final static int BILL_STORAGE_CAPACITY = 1000;

	/**
	 * The supported denominations of bills.
	 */
	public final int[] billDenominations;

	/**
	 * The devices that dispense bills for change, one per denomination.
	 */
	public final Map<Integer, BillDispenser> billDispensers;

	/**
	 * The default capacity of bill dispenser devices.
	 */
	public final static int BILL_DISPENSER_CAPACITY = 100;

	/**
	 * The coin input device.
	 */
	public final CoinSlot coinSlot;

	/**
	 * The device that validates coins.
	 */
	public final CoinValidator coinValidator;

	/**
	 * The device that stores coins.
	 */
	public final CoinStorage coinStorage;

	/**
	 * The default capacity of the coin storage device.
	 */
	public static final int COIN_STORAGE_CAPACITY = 1000;

	/**
	 * The supported denominations of coins, as decimals of the unit currency.
	 */
	public final List<BigDecimal> coinDenominations;

	/**
	 * The devices that dispense coins, one per denomination.
	 */
	public final Map<BigDecimal, CoinDispenser> coinDispensers;

	/**
	 * The default capacity of the coin dispenser devices.
	 */
	public static final int COIN_DISPENSER_CAPACITY = 200;

	/**
	 * The device that receives returned coins.
	 */
	public final CoinTray coinTray;

	/**
	 * The default capacity of the coin tray device.
	 */
	public static final int COIN_TRAY_CAPACITY = 20;

	/**
	 * Creates a self-checkout station.
	 * 
	 * @param currency
	 *            The kind of currency permitted.
	 * @param billDenominations
	 *            The set of denominations (i.e., $5, $10, etc.) to accept.
	 * @param coinDenominations
	 *            The set of denominations (i.e., $0.05, $0.10, etc.) to accept.
	 * @param scaleMaximumWeight
	 *            The most weight that can be placed on the scale before it
	 *            overloads.
	 * @param scaleSensitivity
	 *            Any weight changes smaller than this will not be detected or
	 *            announced.
	 * @throws SimulationException
	 *             If any argument is null or negative.
	 * @throws SimulationException
	 *             If the number of bill or coin denominations is &lt;1.
	 */
	public SelfCheckoutStation(Currency currency, int[] billDenominations, BigDecimal[] coinDenominations,
		int scaleMaximumWeight, int scaleSensitivity) {
		if(currency == null || billDenominations == null || coinDenominations == null)
			throw new SimulationException(new NullPointerException("No argument may be null."));

		if(scaleMaximumWeight <= 0)
			throw new SimulationException(new IllegalArgumentException("The scale's maximum weight must be positive."));

		if(scaleSensitivity <= 0)
			throw new SimulationException(new IllegalArgumentException("The scale's sensitivity must be positive."));

		if(billDenominations.length == 0)
			throw new SimulationException(
				new IllegalArgumentException("There must be at least one allowable bill denomination defined."));

		if(coinDenominations.length == 0)
			throw new SimulationException(
				new IllegalArgumentException("There must be at least one allowable coin denomination defined."));

		// Create the devices.
		scale = new ElectronicScale(scaleMaximumWeight, scaleSensitivity);
		baggingArea = new ElectronicScale(scaleMaximumWeight, scaleSensitivity);
		screen = new TouchScreen();
		printer = new ReceiptPrinter();
		cardReader = new CardReader();
		mainScanner = new BarcodeScanner();
		handheldScanner = new BarcodeScanner();
		bagDispenser = new ReusableBagDispenser(BAG_DISPENSER_CAPACITY);

		this.billDenominations = billDenominations;
		billInput = new BillSlot(false);
		billValidator = new BillValidator(currency, billDenominations);
		billStorage = new BillStorage(BILL_STORAGE_CAPACITY);
		billOutput = new BillSlot(true);

		billDispensers = new HashMap<>();

		for(int i = 0; i < billDenominations.length; i++)
			billDispensers.put(billDenominations[i], new BillDispenser(BILL_DISPENSER_CAPACITY));

		this.coinDenominations = Arrays.asList(coinDenominations);
		coinSlot = new CoinSlot();
		coinValidator = new CoinValidator(currency, this.coinDenominations);
		coinStorage = new CoinStorage(COIN_STORAGE_CAPACITY);
		coinTray = new CoinTray(COIN_TRAY_CAPACITY);

		coinDispensers = new HashMap<>();

		for(int i = 0; i < coinDenominations.length; i++)
			coinDispensers.put(coinDenominations[i], new CoinDispenser(COIN_DISPENSER_CAPACITY));

		// Hook up everything.
		interconnect(billInput, billValidator);
		interconnect(billValidator, billStorage);

		for(BillDispenser dispenser : billDispensers.values())
			interconnect(dispenser, billOutput);

		interconnect(coinSlot, coinValidator);
		interconnect(coinValidator, coinTray, coinDispensers, coinStorage);

		for(CoinDispenser coinDispenser : coinDispensers.values())
			interconnect(coinDispenser, coinTray);
	}

	boolean isSupervised() {
		return supervisor != null;
	}

	void setSupervisor(SupervisionStation supervisor) {
		this.supervisor = supervisor;
	}

	private void interconnect(BillSlot slot, BillValidator validator) {
		validatorSource = new BidirectionalChannel<Bill>(slot, validator);
		slot.connect(validatorSource);
	}

	private void interconnect(BillValidator validator, BillStorage storage) {
		UnidirectionalChannel<Bill> bc = new UnidirectionalChannel<Bill>(storage);
		validator.connect(validatorSource, bc);
	}

	private void interconnect(BillDispenser dispenser, BillSlot slot) {
		UnidirectionalChannel<Bill> bc = new UnidirectionalChannel<Bill>(slot);
		dispenser.connect(bc);
	}

	private void interconnect(CoinSlot slot, CoinValidator validator) {
		UnidirectionalChannel<Coin> cc = new UnidirectionalChannel<Coin>(validator);
		slot.connect(cc);
	}

	private void interconnect(CoinValidator validator, CoinTray tray, Map<BigDecimal, CoinDispenser> dispensers,
		CoinStorage storage) {
		UnidirectionalChannel<Coin> rejectChannel = new UnidirectionalChannel<Coin>(tray);
		Map<BigDecimal, UnidirectionalChannel<Coin>> dispenserChannels = new HashMap<BigDecimal, UnidirectionalChannel<Coin>>();

		for(BigDecimal denomination : dispensers.keySet()) {
			CoinDispenser dispenser = dispensers.get(denomination);
			dispenserChannels.put(denomination, new UnidirectionalChannel<Coin>(dispenser));
		}

		UnidirectionalChannel<Coin> overflowChannel = new UnidirectionalChannel<Coin>(storage);

		validator.connect(rejectChannel, dispenserChannels, overflowChannel);
	}

	private void interconnect(CoinDispenser dispenser, CoinTray tray) {
		UnidirectionalChannel<Coin> cc = new UnidirectionalChannel<Coin>(tray);
		dispenser.connect(cc);
	}
}
