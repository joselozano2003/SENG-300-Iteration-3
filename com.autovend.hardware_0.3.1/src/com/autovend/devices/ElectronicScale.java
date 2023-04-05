package com.autovend.devices;

import java.util.ArrayList;

import com.autovend.SellableUnit;
import com.autovend.devices.observers.ElectronicScaleObserver;

/**
 * Represents a device for weighing items being purchased.
 */
public class ElectronicScale extends AbstractDevice<ElectronicScaleObserver> {
	private static final long serialVersionUID = 8457320342496912417L;
	private ArrayList<SellableUnit> items = new ArrayList<>();
	private double weightLimitInGrams;
	private double currentWeightInGrams = 0;
	private double weightAtLastEvent = 0;
	private double sensitivity;

	/**
	 * Constructs an electronic scale with the indicated maximum weight that it can
	 * handle before going into overload.
	 * 
	 * @param weightLimitInGrams
	 *            The weight threshold beyond which the scale will overload.
	 * @param sensitivity
	 *            The number of grams that can be added or removed since the last
	 *            change event, without causing a new change event.
	 * @throws SimulationException
	 *             If either argument is &le;0.
	 */
	public ElectronicScale(int weightLimitInGrams, int sensitivity) {
		if(weightLimitInGrams <= 0)
			throw new SimulationException(new IllegalArgumentException("The maximum weight cannot be zero or less."));

		if(sensitivity <= 0)
			throw new SimulationException(new IllegalArgumentException("The sensitivity cannot be zero or less."));

		this.weightLimitInGrams = weightLimitInGrams;
		this.sensitivity = sensitivity;
	}

	/**
	 * Gets the weight limit for the scale.
	 * 
	 * @return The weight limit.
	 */
	public double getWeightLimit() {
		return weightLimitInGrams;
	}

	/**
	 * Gets the current weight on the scale.
	 * 
	 * @return The current weight.
	 * @throws OverloadException
	 *             If the weight has overloaded the scale.
	 */
	public double getCurrentWeight() throws OverloadException {
		if(currentWeightInGrams <= weightLimitInGrams)
			return currentWeightInGrams;

		throw new OverloadException();
	}

	/**
	 * Gets the sensitivity of the scale. Changes smaller than this limit are not
	 * noticed or announced.
	 * 
	 * @return The sensitivity.
	 */
	public double getSensitivity() {
		return sensitivity;
	}

	/**
	 * Adds an item to the scale. If its weight is over the limit, announces an
	 * "overload" event. Otherwise, if the weight of the added unit is greater than
	 * the sensitivity, announces a "weightChanged" event.
	 * 
	 * @param item
	 *            The item to add.
	 * @throws SimulationException
	 *             If the same item is added more than once.
	 */
	public void add(SellableUnit item) {
		if(item == null)
			throw new SimulationException(new NullPointerException("item"));
		if(items.contains(item))
			throw new SimulationException("The same item cannot be added more than once to the scale.");

		currentWeightInGrams += item.getWeight();

		items.add(item);

		if(currentWeightInGrams > weightLimitInGrams)
			for(ElectronicScaleObserver observer : observers)
			observer.reactToOverloadEvent(this);
		else if(currentWeightInGrams - weightAtLastEvent > sensitivity) {
			weightAtLastEvent = currentWeightInGrams;
			
			for(ElectronicScaleObserver observer1 : observers)
				observer1.reactToWeightChangedEvent(this, currentWeightInGrams);
		}
	}

	/**
	 * Removes an item from the scale. If the scale was previously overloaded and
	 * now is not, announces an "outOfOverload" event. If the scale is now not
	 * overloaded and the weight has just changed by more than the sensitivity, a
	 * "weightChanged" event is announced.
	 * 
	 * @param item
	 *            The item to remove.
	 * @throws SimulationException
	 *             If the item is not on the scale.
	 */
	public void remove(SellableUnit item) {
		if(!items.remove(item))
			throw new SimulationException("The item was not found amongst those on the scale.");

		// To avoid drift in the sum due to round-off error, recalculate the weight.
		double newWeightInGrams = 0.0;
		for(SellableUnit itemOnScale : items)
			newWeightInGrams += itemOnScale.getWeight();

		double original = currentWeightInGrams;
		currentWeightInGrams = newWeightInGrams;

		if(original > weightLimitInGrams && newWeightInGrams <= weightLimitInGrams) {
			weightAtLastEvent = currentWeightInGrams;
			
			for(ElectronicScaleObserver observer : observers)
				observer.reactToOutOfOverloadEvent(this);
		}

		if(currentWeightInGrams <= weightLimitInGrams && weightAtLastEvent - currentWeightInGrams >= sensitivity) {
			weightAtLastEvent = currentWeightInGrams;
			
			for(ElectronicScaleObserver observer : observers)
				observer.reactToWeightChangedEvent(this, currentWeightInGrams);
		}
	}
}
