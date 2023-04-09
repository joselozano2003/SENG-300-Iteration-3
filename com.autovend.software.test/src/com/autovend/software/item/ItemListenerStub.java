package com.autovend.software.item;

import static org.junit.Assert.fail;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.products.Product;

/**Stubs primarily check if/how many times observer events occurred.
 * Tests should fail if an unexpected event is reported.
 * Override any event in this stub that you don't want to fail.
 */
class ItemListenerStub implements ItemEventListener {
	@Override
	public void reactToHardwareFailure() {fail();}
	@Override
	public void reactToDisableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {fail();}
	@Override
	public void reactToEnableDeviceRequest(AbstractDevice<? extends AbstractDeviceObserver> device) {fail();}
	@Override
	public void reactToDisableStationRequest() {fail();}
	@Override
	public void reactToEnableStationRequest() {fail();}
	@Override
	public void onItemAddedEvent(Product product, double quantity) {fail();}
	@Override
	public void onItemNotFoundEvent() {fail();}
}
