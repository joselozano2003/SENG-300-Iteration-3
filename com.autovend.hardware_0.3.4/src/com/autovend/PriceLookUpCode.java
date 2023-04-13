package com.autovend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.autovend.devices.SimulationException;

/**
 * Represents a price look up (PLU) code value, a sequence of digits that, in
 * principle, could lie anywhere in the range 0000-99999. Specific subranges are
 * reserved for specific purposes in the real world, but we will not worry about
 * that here.
 */
public class PriceLookUpCode implements Serializable {
	private static final long serialVersionUID = -7362400581341230230L;
	private Numeral[] numerals;

	/**
	 * Constructs a PLU code from a string of numerals. There must be at least 4
	 * digits and at most 5.
	 * 
	 * @param code
	 *            A string of digits.
	 * @throws SimulationException
	 *             If any character in the input is not a digit between 0 and 9,
	 *             inclusive.
	 * @throws SimulationException
	 *             If the code contains less than 4 digits or more than 5 digits.
	 * @throws NullPointerException
	 *             If code is null.
	 */
	public PriceLookUpCode(Numeral... code) {
		if(code == null)
			throw new SimulationException(new NullPointerException("code is null"));

		if(code.length > 5)
			throw new SimulationException(
				new IllegalArgumentException("The code cannot contain more than five digits."));

		if(code.length < 4)
			throw new SimulationException(
				new IllegalArgumentException("The code cannot contain less than four digits."));

		for(int i = 0; i < code.length; i++)
			if(code[i] == null)
				throw new SimulationException(new NullPointerException("code[" + i + "] is null"));

		numerals = new Numeral[code.length];
		System.arraycopy(code, 0, numerals, 0, code.length);
	}

	/**
	 * Gets the count of numerals in this code.
	 * 
	 * @return The count of numerals.
	 */
	public int numeralLength() {
		return numerals.length;
	}

	/**
	 * Translates this code to a list of numerals. The returned list is not
	 * modifiable.
	 * 
	 * @return This code as an unmodifiable list of numerals.
	 */
	public List<Numeral> asNumerals() {
		return Collections.unmodifiableList(Arrays.asList(numerals));
	}

	/**
	 * Gets the numeral at the indicated index within the code.
	 * 
	 * @param index
	 *            The index of the numeral, &ge;0 and &lt;count.
	 * @return The numeral at the indicated index.
	 * @throws SimulationException
	 *             If the index is outside the legal range.
	 */
	public Numeral getNumeralAt(int index) {
		try {
			return numerals[index];
		}
		catch(IndexOutOfBoundsException e) {
			throw new SimulationException(e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(Numeral numeral : numerals)
			sb.append(numeral.getValue());

		return sb.toString();
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof PriceLookUpCode) {
			PriceLookUpCode other = (PriceLookUpCode)object;

			if(other.numerals.length != numerals.length)
				return false;

			for(int i = 0; i < numerals.length; i++)
				if(!numerals[i].equals(other.numerals[i]))
					return false;

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(numerals);
	}
}
