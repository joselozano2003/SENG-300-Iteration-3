package com.autovend.devices.observers;

import com.autovend.Coin;
import com.autovend.devices.ElectronicScale;

/**
 * Observes events emanating from an electronic scale.
 */
public interface ElectronicScaleObserver extends AbstractDeviceObserver {
	/**
	 * Announces that the weight on the indicated scale has changed.
	 * 
	 * @param scale
	 *            The scale where the event occurred.
	 * @param weightInGrams
	 *            The new weight.
	 */
	void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams);

	/**
	 * Announces that excessive weight has been placed on the indicated scale.
	 * 
	 * @param scale
	 *            The scale where the event occurred.
	 */
	void reactToOverloadEvent(ElectronicScale scale);

	/**
	 * Announces that the former excessive weight has been removed from the
	 * indicated scale, and it is again able to measure weight.
	 * 
	 * @param scale
	 *            The scale where the event occurred.
	 */
	void reactToOutOfOverloadEvent(ElectronicScale scale);
}
