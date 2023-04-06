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

import java.util.ArrayList;
import java.util.List;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.AbstractSoftware;

@SuppressWarnings("serial")
public class Payment extends AbstractSoftware<PaymentListener> {
	private static Payment instance;
	private List<Payment> children;
	
	public Payment(SelfCheckoutStation station) {
		super(station);
		//Initialize this instance and children once.
		if (instance == null) {
			instance = this;
			children = new ArrayList<Payment>();
			children.add(new WithCoin(station));
			children.add(new WithBill(station));
			children.add(new WithCard(station));
		}
	}
	
	/**
	 * @return List of active subclasses.
	 */
	protected List<Payment> getChildren() {
		return children;
	}
	
	/**
	 * @return This current active instance of this class. Could be null.
	 */
	public static Payment getInstance() {
		return instance;
	}
	
}
