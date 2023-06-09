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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.autovend.ReusableBag;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.software.bagging.ReusableBagProduct;
import com.autovend.software.item.ProductsDatabase2;
import com.autovend.software.item.TextSearchProduct;
import com.autovend.software.test.Setup;

public class CustomerSessionTest {
	
	private CustomerSession session;
	private BarcodedProduct barcodedProduct;
	private PLUCodedProduct pluCodedProduct;
	private ReusableBagProduct reusableBagProduct;
	private TextSearchProduct textSearchProduct;
	
	
	@Before
	public void setUp() throws Exception {
		session = new CustomerSession();
		barcodedProduct = Setup.createBarcodedProduct123(12, 5, true);
		pluCodedProduct = Setup.createPLUProduct1234(12, 1, true); //In CustomerSession Class, quantity to add was assumed to be the weight.
		reusableBagProduct = new ReusableBagProduct();
		textSearchProduct = new TextSearchProduct("Candy", "Mars", BigDecimal.valueOf(4), 2);
	}

//TESTING PAYMENT-COMPLETION.
		//Customer pays the full amount for the one item.
		@Test
		public void isPaymentCompleteTestOneItem() {
			//One item has been added to cart. Customer needs to pay 12 dollars (Since 1 item is 12 dollars).
			session.addItemToCart(barcodedProduct, 1);
					
			session.addPayment(BigDecimal.valueOf(12));					
			// Check if payment is marked as complete
			assertTrue(session.isPaymentComplete());
		}
		//Customer doesn't pay the full amount for the one item.
		@Test
		public void isPaymentIncompleteTestoneItem() {
			//One item have been added to cart. Customer needs to pay 12 dollars (Since 1 item is 12 dollars).
			session.addItemToCart(barcodedProduct, 2);
					
			session.addPayment(BigDecimal.valueOf(1));	//Customer should pay 12 dollars, but he paid 1 dollar!				
			// Check if payment is marked as incomplete
			assertFalse(session.isPaymentComplete()); //So Payment isn't complete.
		}
				

		//Customer pays the full amount for the two items.
		@Test
		public void isPaymentCompleteTestTwoItems() {
			//Two items have been added to cart. Customer needs to pay 24 dollars (Since 1 item is 12 dollars).
			session.addItemToCart(barcodedProduct, 2);
				
			session.addPayment(BigDecimal.valueOf(24));					
			// Check if payment is marked as complete
			assertTrue(session.isPaymentComplete());
		}
		
		//Customer doesn't pay the full amount for the two items.
		@Test
		public void isPaymentIncompleteTestTwoItems() {
			//Two items have been added to cart. Customer needs to pay 24 dollars (Since 1 item is 12 dollars).
			session.addItemToCart(barcodedProduct, 2);
				
			session.addPayment(BigDecimal.valueOf(23));	//Customer should pay 24 dollars, but he paid 23 dollars!				
			// Check if payment is marked as incomplete
			assertFalse(session.isPaymentComplete()); //So Payment isn't complete.
		}
		//Customer pays the full amount for the 10 items.
		@Test
		public void isPaymentCompleteTestTenItems() {
			//Ten items have been added to cart. Customer needs to pay 120 dollars (Since 1 item is 12 dollars).
			session.addItemToCart(barcodedProduct, 10);
					
			session.addPayment(BigDecimal.valueOf(120));					
			// Check if payment is marked as complete
			assertTrue(session.isPaymentComplete());
		}
		//Customer doesn't pay the full amount for the ten items.
		@Test
		public void isPaymentIncompleteTestTenItems() {
			//Two items have been added to cart. Customer needs to pay 120 dollars (Since 1 item is 12 dollars).
			session.addItemToCart(barcodedProduct, 10);
					
			session.addPayment(BigDecimal.valueOf(119));//Customer should pay 120 dollars, but he paid 119 dollars!				
			// Check if payment is marked as incomplete
			assertFalse(session.isPaymentComplete()); //So Payment isn't complete.
			}
		
//TESTING CUSTOMER PAYMENT (addPayment method) ON BARCODED ITEMS
		
		//Making Payment on 1 item
		@Test
		public void customerPaymentTestOneBarcodedItem() {
			
			session.addItemToCart(barcodedProduct, 1); // 12 dollar worth product
			
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
			
			//After making payment
			session.addPayment(BigDecimal.valueOf(10));
			assertEquals(BigDecimal.valueOf(10), session.getTotalPaid());
			 
			
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(2));
			assertEquals(BigDecimal.valueOf(12), session.getTotalPaid());	
		}
		
		//Making Payment on 2 items
		@Test
		public void customerPaymentTestTwoBarcodedItems() {
			
			session.addItemToCart(barcodedProduct, 2); // 12 dollar worth product
				
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
				
			//After making payment
			session.addPayment(BigDecimal.valueOf(10));
			assertEquals(BigDecimal.valueOf(10), session.getTotalPaid());
				 
				
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(14));
			assertEquals(BigDecimal.valueOf(24), session.getTotalPaid());	
			}
		
		//Making Payment on 20 items
		@Test
		public void customerPaymentTestTwentyBarcodedItems() {
				
			session.addItemToCart(barcodedProduct, 20); // 12 dollar worth product
					
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
					
			//After making payment
			session.addPayment(BigDecimal.valueOf(200));
			assertEquals(BigDecimal.valueOf(200), session.getTotalPaid());
					 
					
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(40));
			assertEquals(BigDecimal.valueOf(240), session.getTotalPaid());	
			}
		
//TESTING THAT THE CHANGE DUE CALCULATED IS CORRECT OR NOT
		
		/* Test Case: Paying 40 dollars for 36 dollar items and checking if changeDue is calculated correctly.
		 * 
		 * Description: If the customer pays more than the total cost, the customer will be paid back the money they 
		 * are owed.
		 * 
		 * Expected Result: The chnageDue calculated for the customer is 4 dollars.
		 */
		@Test
		public void changeDueCalculationTestFourDollars() {
			session.addItemToCart(barcodedProduct, 3); // 12 dollar worth product
			BigDecimal expected = new BigDecimal("4.00");

			session.addPayment(BigDecimal.valueOf(40));
			assertEquals(expected, session.getChangeDue());
		}
		/* Test Case: Paying 25 dollars for 24 dollar items and checking if changeDue is calculated correctly.
		 * 
		 * Description: If the customer pays more than the total cost, the customer will be paid back the money they 
		 * are owed.
		 * 
		 * Expected Result: The chnageDue calculated for the customer is 1 dollar.
		 */
		@Test
		public void changeDueCalculationTestOneDollar() {
			session.addItemToCart(barcodedProduct, 2); // 12 dollar worth product
			BigDecimal expected = new BigDecimal("1.00");

			session.addPayment(BigDecimal.valueOf(25));
			assertEquals(expected, session.getChangeDue());
		}
		/* Test Case: Paying 70 dollars for 60 dollar items and checking if changeDue is calculated correctly.
		 * 
		 * Description: If the customer pays more than the total cost, the customer will be paid back the money they 
		 * are owed.
		 * 
		 * Expected Result: The chnageDue calculated for the customer is 10 dollars.
		 */
		@Test
		public void changeDueCalculationTestTenDollar() {
			session.addItemToCart(barcodedProduct, 5); // 12 dollar worth product
			BigDecimal expected = new BigDecimal("10.00");

			session.addPayment(BigDecimal.valueOf(70));
			assertEquals(expected, session.getChangeDue());
		}
	
//TEST TO CHECK IF FAILED PAYMENTS ARE TRACKED PROPERLY
		
		@Test
		public void OneFailedPaymentTest() {
			session.addFailedPayment();
			assertEquals(1, session.getNumberOfFailedPayments());		
		}
		
		@Test
		public void TwoFailedPaymentTest() {
			session.addFailedPayment();
			session.addFailedPayment();
			assertEquals(2, session.getNumberOfFailedPayments());		
		}
		
		@Test
		public void OneHundredFailedPaymentTest() {
			for(int i = 0; i < 100; i++) {
				session.addFailedPayment();
			}				
			assertEquals(100, session.getNumberOfFailedPayments());		
		}
		@Test
		public void OneThousandFailedPaymentTest() {
			for(int i = 0; i < 1000; i++) {
				session.addFailedPayment();
			}				
			assertEquals(1000, session.getNumberOfFailedPayments());		
		}
		
					
//TEST TO CHECK IF ITEM HAS BEEN ADDED TO CART	
	
		@Test
		public void addOneItemToEmptyCartTest() {
			session.addItemToCart(barcodedProduct, 1);			
			assertEquals(1.00, session.getShoppingCart().get(barcodedProduct), 0.1);			
		}
		@Test
		public void addTwoItemToEmptyCartTest() {
			session.addItemToCart(barcodedProduct, 2);			
			assertEquals(2.00, session.getShoppingCart().get(barcodedProduct), 0.1);			
		}
		@Test
		public void addTenItemToEmptyCartTest() {
			session.addItemToCart(barcodedProduct, 10);			
			assertEquals(10.00, session.getShoppingCart().get(barcodedProduct), 0.1);			
		}
		@Test
		public void addOneHundredItemToEmptyCartTest() {
			session.addItemToCart(barcodedProduct, 10);			
			assertEquals(10.00, session.getShoppingCart().get(barcodedProduct), 0.1);			
		}
		@Test
		public void addNinetyNineItemToEmptyCartTest() {
			session.addItemToCart(barcodedProduct, 99);			
			assertEquals(99.00, session.getShoppingCart().get(barcodedProduct), 0.1);			
		}
		@Test
		public void addOneHundredOneItemToEmptyCartTest() {
			session.addItemToCart(barcodedProduct, 101);			
			assertEquals(101.00, session.getShoppingCart().get(barcodedProduct), 0.1);			
		}
		
		@Test
		public void addOneItemToNonEmptyCartTest() {
			//Cart already has 5 items
			session.addItemToCart(barcodedProduct, 5);	
			session.addItemToCart(barcodedProduct, 1);
			assertEquals(6.00, session.getShoppingCart().get(barcodedProduct), 0.1); //Cart should now have 6 items
		}
		@Test
		public void addTwoItemToNonEmptyCartTest() {
			//Cart already has 5 items
			session.addItemToCart(barcodedProduct, 5);	
			session.addItemToCart(barcodedProduct, 2);
			assertEquals(7.00, session.getShoppingCart().get(barcodedProduct), 0.1); //Cart should now have 7 items
		}
		@Test
		public void addTenItemToNonEmptyCartTest() {
			//Cart already has 5 items
			session.addItemToCart(barcodedProduct, 5);	
			session.addItemToCart(barcodedProduct, 10);
			assertEquals(15.00, session.getShoppingCart().get(barcodedProduct), 0.1); //Cart should now have 15 items
		}
		@Test
		public void addOneHundredItemToNonEmptyCartTest() {
			//Cart already has 5 items
			session.addItemToCart(barcodedProduct, 5);	
			session.addItemToCart(barcodedProduct, 100);
			assertEquals(105.00, session.getShoppingCart().get(barcodedProduct), 0.1); //Cart should now have 105 items
		}
		@Test
		public void addNinetyNineItemToNonEmptyCartTest() {
			//Cart already has 5 items
			session.addItemToCart(barcodedProduct, 5);	
			session.addItemToCart(barcodedProduct, 99);
			assertEquals(104.00, session.getShoppingCart().get(barcodedProduct), 0.1); //Cart should now have 104 items
		}
		@Test
		public void addOneHundredOneItemToNonEmptyCartTest() {
			//Cart already has 5 items
			session.addItemToCart(barcodedProduct, 5);	
			session.addItemToCart(barcodedProduct, 101);
			assertEquals(106.00, session.getShoppingCart().get(barcodedProduct), 0.1); //Cart should now have 106 items
		}	
		
//TEST TO CHECK IF getAmountLeft() IS CALCULATING PROPERLY
		
		@Test
		public void amountLeftOneTest() {
			session.addItemToCart(barcodedProduct, 2); //Each product 12 dollars worth		
			session.addPayment(BigDecimal.valueOf(23)); //Paid 1 dollars less
			
			BigDecimal expected = new BigDecimal("1.00");
			assertEquals(expected, session.getAmountLeft());			
		}
		@Test
		public void amountLeftSixTest() {
			session.addItemToCart(barcodedProduct, 3); //Each product 12 dollars worth		
			session.addPayment(BigDecimal.valueOf(30)); //Paid 6 dollars less
			
			BigDecimal expected = new BigDecimal("6.00");
			assertEquals(expected, session.getAmountLeft());			
		}
		@Test
		public void amountLeftTenTest() {
			session.addItemToCart(barcodedProduct, 5); //Each product 12 dollars worth		
			session.addPayment(BigDecimal.valueOf(50)); //Paid 10 dollars less
			
			BigDecimal expected = new BigDecimal("10.00");
			assertEquals(expected, session.getAmountLeft());			
		}
		@Test
		public void amountLeftOneHundredTest() {
			session.addItemToCart(barcodedProduct, 10); //Each product 12 dollars worth		
			session.addPayment(BigDecimal.valueOf(20)); //Paid 100 dollars less
			
			BigDecimal expected = new BigDecimal("100.00");
			assertEquals(expected, session.getAmountLeft());			
		}
		
//TEST TO CHECK IF TOTAL COST IS CALCULATED CORRECTLY
		@Test
		public void totalCostBeforeAnyPaymentTestOneProduct() {
			session.addItemToCart(barcodedProduct, 1);
			BigDecimal expected = new BigDecimal("12.00");
			
			assertEquals(expected, session.getTotalCost());			
		}
		@Test
		public void totalCostBeforeAnyPaymentTestTwoProducts() {
			session.addItemToCart(barcodedProduct, 2);
			BigDecimal expected = new BigDecimal("24.00");
			
			assertEquals(expected, session.getTotalCost());			
		}
		@Test
		public void totalCostBeforeAnyPaymentTestTenProducts() {
			session.addItemToCart(barcodedProduct, 10);
			BigDecimal expected = new BigDecimal("120.00");
			
			assertEquals(expected, session.getTotalCost());			
		}
		@Test
		public void totalCostBeforeAnyPaymentTestNinetyNineProducts() {
			session.addItemToCart(barcodedProduct, 99);
			BigDecimal expected = new BigDecimal("1188.00");
			
			assertEquals(expected, session.getTotalCost());			
		}
		@Test
		public void totalCostBeforeAnyPaymentTestOneHundredProducts() {
			session.addItemToCart(barcodedProduct, 100);
			BigDecimal expected = new BigDecimal("1200.00");
			
			assertEquals(expected, session.getTotalCost());			
		}
		@Test
		public void totalCostBeforeAnyPaymentTestOneHundredOneProducts() {
			session.addItemToCart(barcodedProduct, 101);
			BigDecimal expected = new BigDecimal("1212.00");
			
			assertEquals(expected, session.getTotalCost());			
		}
		
		@Test
		public void totalCostAfterAnyPaymentTestTwoProduct() {
			session.addItemToCart(barcodedProduct, 2);
			BigDecimal expected = new BigDecimal("24.00");
			
			session.addPayment(BigDecimal.valueOf(5)); //Paid some amount (not full)
			assertEquals(expected, session.getTotalCost()); //Total cost shouldn't be updated	
		}
		@Test
		public void totalCostAfterAnyPaymentTestTenProduct() {
			session.addItemToCart(barcodedProduct, 10);
			BigDecimal expected = new BigDecimal("120.00");
			
			session.addPayment(BigDecimal.valueOf(5)); //Paid some amount (not full)
			assertEquals(expected, session.getTotalCost()); //Total cost shouldn't be updated	
		}
		@Test
		public void totalCostAfterAnyPaymentTestOneHundredProduct() {
			session.addItemToCart(barcodedProduct, 100);
			BigDecimal expected = new BigDecimal("1200.00");
			
			session.addPayment(BigDecimal.valueOf(100)); //Paid some amount (not full)
			assertEquals(expected, session.getTotalCost()); //Total cost shouldn't be updated	
		}
		@Test
		public void totalCostAfterAnyPaymentTestOneHundredOneProduct() {
			session.addItemToCart(barcodedProduct, 101);
			BigDecimal expected = new BigDecimal("1212.00");
			
			session.addPayment(BigDecimal.valueOf(1000)); //Paid some amount (not full)
			assertEquals(expected, session.getTotalCost()); //Total cost shouldn't be updated	
		}			
		
//TESTING PAYMENT ON PLU CODED PRODUCTS
		//Making Payment on 1 item
		@Test
		public void customerPaymentTestOnePLUItem() {
			
			session.addItemToCart(pluCodedProduct, 1); // 12 dollar worth product
			
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
			
			//After making payment
			session.addPayment(BigDecimal.valueOf(10));
			assertEquals(BigDecimal.valueOf(10), session.getTotalPaid());
			 
			
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(2));
			assertEquals(BigDecimal.valueOf(12), session.getTotalPaid());	
		}
		
		//Making Payment on 2 items
		@Test
		public void customerPaymentTestTwoPLUItems() {
			
			session.addItemToCart(pluCodedProduct, 2); // 12 dollar worth product
				
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
				
			//After making payment
			session.addPayment(BigDecimal.valueOf(10));
			assertEquals(BigDecimal.valueOf(10), session.getTotalPaid());
				 
				
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(14));
			assertEquals(BigDecimal.valueOf(24), session.getTotalPaid());	
			}
		
		//Making Payment on 20 items
		@Test
		public void customerPaymentTestTwentyPLUItems() {
				
			session.addItemToCart(pluCodedProduct, 20); // 12 dollar worth product
					
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
					
			//After making payment
			session.addPayment(BigDecimal.valueOf(200));
			assertEquals(BigDecimal.valueOf(200), session.getTotalPaid());
					 
					
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(40));
			assertEquals(BigDecimal.valueOf(240), session.getTotalPaid());	
			}
		
//TESTING PAYMENT ON REUSABLE BAG PRODUCTS
		
		//Making Payment on 1 item
		@Test
		public void customerPaymentTestOneReusableBagItem() {
			
			session.addItemToCart(reusableBagProduct, 1); // 12 dollar worth product
			
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
			
			//Making payment
			session.addPayment(BigDecimal.valueOf(0.05));
			assertEquals(BigDecimal.valueOf(0.05), session.getTotalPaid());
			 
			
				
		}
		
		//Making Payment on 2 items
		@Test
		public void customerPaymentTestTwoReusableBagItems() {
			
			session.addItemToCart(reusableBagProduct, 2); // 12 dollar worth product
				
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
				
			//Making payment
			session.addPayment(BigDecimal.valueOf(0.10));
			assertEquals(BigDecimal.valueOf(0.10), session.getTotalPaid());
				 	
			}
		
		//Making Payment on 20 items
		@Test
		public void customerPaymentTestTwentyReusableBagItems() {
				
			session.addItemToCart(reusableBagProduct, 20); // 12 dollar worth product
					
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
					
			//Making payment
			session.addPayment(BigDecimal.valueOf(1));
			assertEquals(BigDecimal.valueOf(1), session.getTotalPaid());
	
			}
		
//TESTING FOR EXPECTED WEIGHT CALCULATION
		
		@Test
		public void addOneItemBarcodedTestWeight() {
				
			session.addItemToCart(barcodedProduct, 1); // Weight of one item is 5												
										
			assertEquals(5.0, session.getExpectedWeight(), 0.1);
	
			}
		@Test
		public void addTwoItemsBarcodedTestWeight() {
				
			session.addItemToCart(barcodedProduct, 2); // Weight of one item is 5												
										
			assertEquals(10.0, session.getExpectedWeight(), 0.1);
	
			}
		@Test
		public void addOneHundredBarcodedItemsTestWeight() {
				
			session.addItemToCart(barcodedProduct, 100); // Weight of one item is 5												
										
			assertEquals(500.0, session.getExpectedWeight(), 0.1);
	
			}
		@Test
		public void addNinetyNineBarcodedItemsTestWeight() {
				
			session.addItemToCart(barcodedProduct, 99); // Weight of one item is 5												
										
			assertEquals(495.0, session.getExpectedWeight(), 0.1);
			
			}
		
		
		@Test
		public void addOneItemPLUTestWeight() {
				
			session.addItemToCart(pluCodedProduct, 1); // Weight of one item is 5												
										
			assertEquals(1.0, session.getExpectedWeight(), 0.1);
	
			}
		@Test
		public void addTwoItemsPLUTestWeight() {
				
			session.addItemToCart(pluCodedProduct, 2); // Weight of one item is 5												
										
			assertEquals(2.0, session.getExpectedWeight(), 0.1);
	
			}
		@Test
		public void addOneHundredPLUItemsTestWeight() {
				
			session.addItemToCart(pluCodedProduct, 100); // Weight of one item is 5												
										
			assertEquals(100.0, session.getExpectedWeight(), 0.1);
	
			}
		@Test
		public void addNinetyNinePLUItemsTestWeight() {
				
			session.addItemToCart(pluCodedProduct, 99); // Weight of one item is 5												
										
			assertEquals(99.0, session.getExpectedWeight(), 0.1);
			
			}
	/**	
//TESTING IF ITEM TOO HEAVY FOR SCALE IS DEDUCTED FROM WEIGHT
		@Test
		public void addOneTooHeavyBarcodedItemWeightTest() {
				
			session.addItemToCart(barcodedProduct, 1); // Weight of one item is 5, assuming 5 is too heavy here.	
			session.itemAddedToCartTooHeavyForScale(5);
										
			assertEquals(0, session.getExpectedWeight(), 0.1);
			
			}
		@Test
		public void addTwoTooHeavyBarcodedItemWeightTest() {
				
			session.addItemToCart(barcodedProduct, 2); // Weight of one item is 5.	
			session.itemAddedToCartTooHeavyForScale(10);
										
			assertEquals(0, session.getExpectedWeight(), 0.1);
			
		}
		@Test
		public void addOneHundredTooHeavyBarcodedItemWeightTest() {
				
			session.addItemToCart(barcodedProduct, 100); // Weight of one item is 5.	
			session.itemAddedToCartTooHeavyForScale(500);
										
			assertEquals(0, session.getExpectedWeight(), 0.1);
			
	}
	**/	
//TESTING IF TOTAL COST FOR TEXT SEARCH PRODUCTS ARE CALCULATED CORRECTLY
		//Making Payment on 1 item
		@Test
		public void customerPaymentTestOnetextSearchItem() {
			
			session.addItemToCart(textSearchProduct, 1); // 4 dollar worth product
			
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
			
			//After making payment
			session.addPayment(BigDecimal.valueOf(2));
			assertEquals(BigDecimal.valueOf(2), session.getTotalPaid());
			 
			
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(2));
			assertEquals(BigDecimal.valueOf(4), session.getTotalPaid());	
		}
		
		//Making Payment on 2 items
		@Test
		public void customerPaymentTestTwoTextSearchItems() {
			
			session.addItemToCart(textSearchProduct, 2); //4 dollar worth product
				
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
				
			//After making payment
			session.addPayment(BigDecimal.valueOf(5));
			assertEquals(BigDecimal.valueOf(5), session.getTotalPaid());
				 
				
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(3));
			assertEquals(BigDecimal.valueOf(8), session.getTotalPaid());	
			}
		
		//Making Payment on 20 items
		@Test
		public void customerPaymentTestTwentyTextSearchItems() {
				
			session.addItemToCart(textSearchProduct, 20); // 4 dollar worth product
					
			//Before payment, total paid amount is 0	
			assertEquals(BigDecimal.valueOf(0), session.getTotalPaid());
					
			//After making payment
			session.addPayment(BigDecimal.valueOf(73));
			assertEquals(BigDecimal.valueOf(73), session.getTotalPaid());
					 
					
			//Making full payment afterwards
			session.addPayment(BigDecimal.valueOf(7));
			assertEquals(BigDecimal.valueOf(80), session.getTotalPaid());	
			}
		
		
// TESTING IF BARCODED PRODUCT IS REMOVED FROM SHOPPING CART
		
		@Test
		public void removeOneBarcodedProductTest() {
			session.addItemToCart(barcodedProduct, 5);
			session.removeItemFromCart(barcodedProduct, 1);
			assertEquals(4, session.getShoppingCart().get(barcodedProduct), 0.1);
		}
		@Test
		public void removeTwoBarcodedProductTest() {
			session.addItemToCart(barcodedProduct, 5);
			session.removeItemFromCart(barcodedProduct, 2);
			assertEquals(3, session.getShoppingCart().get(barcodedProduct), 0.1);
		}
		@Test
		public void removeOneHundredBarcodedProductTest() {
			session.addItemToCart(barcodedProduct, 107);
			session.removeItemFromCart(barcodedProduct, 100);
			assertEquals(7, session.getShoppingCart().get(barcodedProduct), 0.1);
		}
		@Test
		public void emptyCartBarcodedProductTest() {
			session.addItemToCart(barcodedProduct, 6);
			session.removeItemFromCart(barcodedProduct, 6);
			assertEquals(0, session.getShoppingCart().get(barcodedProduct), 0.1);
		}
	
//TESTING IF TOTAL COST IS UPDATED FOR BARCODED PRODUCT AFTER ITEM IS REMOVED
		@Test
		public void oneBarcodedProductRemovedTotalCostUpdatedTest() {
			
			session.addItemToCart(barcodedProduct, 10); // 12 dollar worth product
			
			//Before payment, total paid amount is 0	
			BigDecimal expectedCostBeforeRemoving = new BigDecimal("120.00");
			assertEquals(expectedCostBeforeRemoving, session.getTotalCost());
			
			//After removing item
			session.removeItemFromCart(barcodedProduct, 1);;
			BigDecimal expectedCostAfterRemoving = new BigDecimal("108.00");
			assertEquals(expectedCostAfterRemoving, session.getTotalCost());			 						
		}
		@Test
		public void TwoBarcodedProductRemovedTotalCostUpdatedTest() {
			
			session.addItemToCart(barcodedProduct, 10); // 12 dollar worth product
			
			//Before payment, total paid amount is 0	
			BigDecimal expectedCostBeforeRemoving = new BigDecimal("120.00");
			assertEquals(expectedCostBeforeRemoving, session.getTotalCost());
			
			//After removing item
			session.removeItemFromCart(barcodedProduct, 2);;
			BigDecimal expectedCostAfterRemoving = new BigDecimal("96.00");
			assertEquals(expectedCostAfterRemoving, session.getTotalCost());			 						
		}
		@Test
		public void fiftyOneBarcodedProductRemovedTotalCostUpdatedTest() {
			
			session.addItemToCart(barcodedProduct, 100); // 12 dollar worth product
			
			//Before payment, total paid amount is 0	
			BigDecimal expectedCostBeforeRemoving = new BigDecimal("1200.00");
			assertEquals(expectedCostBeforeRemoving, session.getTotalCost());
			
			//After removing item
			session.removeItemFromCart(barcodedProduct, 51);;
			BigDecimal expectedCostAfterRemoving = new BigDecimal("588.00");
			assertEquals(expectedCostAfterRemoving, session.getTotalCost());			 						
		}
		
		@Test
		public void oneHundredBarcodedProductRemovedTotalCostUpdatedTest() {
			
			session.addItemToCart(barcodedProduct, 110); // 12 dollar worth product
			
			//Before payment, total paid amount is 0	
			BigDecimal expectedCostBeforeRemoving = new BigDecimal("1320.00");
			assertEquals(expectedCostBeforeRemoving, session.getTotalCost());
			
			//After removing item
			session.removeItemFromCart(barcodedProduct, 100);;
			BigDecimal expectedCostAfterRemoving = new BigDecimal("120.00");
			assertEquals(expectedCostAfterRemoving, session.getTotalCost());			 						
		}
		
// TESTING IF PLU CODED PRODUCT IS REMOVED FROM SHOPPING CART

		@Test
		public void removeOnePLUCodedProductTest() {
			session.addItemToCart(pluCodedProduct, 5);
			session.removeItemFromCart(pluCodedProduct, 1);
			assertEquals(4, session.getShoppingCart().get(pluCodedProduct), 0.1);
		}
		@Test
		public void removeTwoPLUCodedProductTest() {
			session.addItemToCart(pluCodedProduct, 5);
			session.removeItemFromCart(pluCodedProduct, 2);
			assertEquals(3, session.getShoppingCart().get(pluCodedProduct), 0.1);
		}
		@Test
		public void removeOneHundredPLUCodedProductTest() {
			session.addItemToCart(pluCodedProduct, 108);
			session.removeItemFromCart(pluCodedProduct, 100);
			assertEquals(8, session.getShoppingCart().get(pluCodedProduct), 0.1);
		}
		@Test
		public void emptyCartPLUCodedProductTest() {
			session.addItemToCart(pluCodedProduct, 6);
			session.removeItemFromCart(pluCodedProduct, 6);
			assertEquals(0, session.getShoppingCart().get(pluCodedProduct), 0.1);
		}
		

// TESTING RESUABLE BAG PRODUCT IS REMOVED FROM SHOPPING CART

		@Test
		public void removeOneReusableBagProductTest() {
			session.addItemToCart(reusableBagProduct, 5);
			session.removeItemFromCart(reusableBagProduct, 1);
			assertEquals(4, session.getShoppingCart().get(reusableBagProduct), 0.1);
		}
		@Test
		public void removeTwoReusableBagProductTest() {
			session.addItemToCart(reusableBagProduct, 5);
			session.removeItemFromCart(reusableBagProduct, 2);
			assertEquals(3, session.getShoppingCart().get(reusableBagProduct), 0.1);
		}
		@Test
		public void removeOneHundredReusableBagProductTest() {
			session.addItemToCart(reusableBagProduct, 108);
			session.removeItemFromCart(reusableBagProduct, 100);
			assertEquals(8, session.getShoppingCart().get(reusableBagProduct), 0.1);
		}
		@Test
		public void emptyCartReusableBagProductTest() {
			session.addItemToCart(reusableBagProduct, 6);
			session.removeItemFromCart(reusableBagProduct, 6);
			assertEquals(0, session.getShoppingCart().get(reusableBagProduct), 0.1);
		}

// TESTING TEXT SEARCH PRODUCT IS REMOVED FROM SHOPPING CART

		@Test
		public void removeOneTextSearchProductTest() {
			session.addItemToCart(textSearchProduct, 5);
			session.removeItemFromCart(textSearchProduct, 1);
			assertEquals(4, session.getShoppingCart().get(textSearchProduct), 0.1);
		}
		@Test
		public void removeTwoTextSearchProductTest() {
			session.addItemToCart(textSearchProduct, 5);
			session.removeItemFromCart(textSearchProduct, 2);
			assertEquals(3, session.getShoppingCart().get(textSearchProduct), 0.1);
		}
		@Test
		public void removeOneHundredTextSearchProductTest() {
			session.addItemToCart(textSearchProduct, 108);
			session.removeItemFromCart(textSearchProduct, 100);
			assertEquals(8, session.getShoppingCart().get(textSearchProduct), 0.1);
		}
		@Test
		public void emptyCartTextSearchProductTest() {
			session.addItemToCart(textSearchProduct, 6);
			session.removeItemFromCart(textSearchProduct, 6);
			assertEquals(0, session.getShoppingCart().get(textSearchProduct), 0.1);
		}			
	
//TESTS FOR ADDING MEMBERSHIP 
		@Test
		public void membershipNumberTest() {
			session.addMembershipNumber("30143490");
			assertEquals("30143490", session.getMembershipNumber());
		}

// TEST TO SEE IF PAYMENT METHOD IS SET
		@Test
		public void setPaymentMethodCreditTest() {
			session.setPaymentMethod("Credit Card");
			assertEquals("Credit Card", session.getPaymentMethod());
		}
		@Test
		public void setPaymentMethodDebitTest() {
			session.setPaymentMethod("Debit Card");
			assertEquals("Debit Card", session.getPaymentMethod());
		}
		@Test
		public void setPaymentMethodCoinTest() {
			session.setPaymentMethod("Coin");
			assertEquals("Coin", session.getPaymentMethod());
		}
		@Test
		public void setPaymentMethodBillTest() {
			session.setPaymentMethod("Bill");
			assertEquals("Bill", session.getPaymentMethod());
		}
		@Test
		public void setPaymentMethodGiftCardTest() {
			session.setPaymentMethod("Gift Card");
			assertEquals("Gift Card", session.getPaymentMethod());
		}

}
		

	

