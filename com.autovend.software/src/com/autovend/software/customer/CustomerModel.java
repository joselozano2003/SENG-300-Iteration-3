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

import com.autovend.software.item.ItemFacade;
import com.autovend.software.membership.MembershipFacade;
import com.autovend.software.payment.PaymentFacade;
import com.autovend.software.receipt.ReceiptFacade;
import com.autovend.software.weight.WeightFacade;

public class CustomerModel {
	
	private final ItemFacade itemFacade;
	private final PaymentFacade paymentFacade;
	private final ReceiptFacade receiptFacade;
	private final WeightFacade baggingFacade;
	private final MembershipFacade membershipFacade;

    public CustomerModel(ItemFacade item, PaymentFacade payment, ReceiptFacade receipt, WeightFacade bagging, MembershipFacade membership) {
		if (item == null || payment == null || receipt == null || bagging == null || membership == null)
			throw new NullPointerException("Arguments cannot be null");
    	this.itemFacade = item;
		this.paymentFacade = payment;
		this.receiptFacade = receipt;
		this.baggingFacade = bagging;
		this.membershipFacade = membership;
    }
    
    //
	
}
