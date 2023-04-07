package com.autovend;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.autovend.devices.SimulationException;

/**
 * Represents a barcode value. Real-world barcodes are a sequence of digits.
 */
public class Barcode implements Serializable {
	private static final long serialVersionUID = -3841123212073233225L;
	private Numeral[] digits;

	/**
	 * Constructs a barcode from a string of digits.
	 * 
	 * @param code
	 *            A string of digits.
	 * @throws SimulationException
	 *             If any character in the input is not a digit between 0 and 9,
	 *             inclusive.
	 * @throws SimulationException
	 *             If the code is null
	 * @throws SimulationException
	 *             If the code's length is &lt;1 or &gt;48.
	 */
	public Barcode(Numeral... code) {
		if(code == null)
			throw new SimulationException(new NullPointerException("code is null"));

		if(code.length < 1)
			throw new SimulationException(
				new IllegalArgumentException("A barcode cannot contain less than one digit."));

		if(code.length > 48)
			throw new SimulationException(
				new IllegalArgumentException("A barcode cannot contain more than forty-eight digits."));

		for(int i = 0; i < code.length; i++)
			if(code[i] == null)
				throw new SimulationException(new NullPointerException("code[" + i + "] is null"));

		// Copy code to digits to avoid potential security hole
		digits = new Numeral[code.length];
		System.arraycopy(code, 0, digits, 0, code.length);
	}

	/**
	 * Gets the count of digits in this code.
	 * 
	 * @return The count of digits.
	 */
	public int digitCount() {
		return digits.length;
	}

	/**
	 * Gets a list of the digits in this barcode.
	 * 
	 * @return An unmodifiable list of the digits in this barcode.
	 */
	public List<Numeral> digits() {
		return Collections.unmodifiableList(Arrays.asList(digits));
	}

	/**
	 * Gets the digit at the indicated index within the code.
	 * 
	 * @param index
	 *            The index of the digit, &ge;0 and &lt;count.
	 * @return The digit at the indicated index.
	 * @throws SimulationException
	 *             If the index is outside the legal range.
	 */
	public Numeral getDigitAt(int index) {
		try {
			return digits[index];
		}
		catch(IndexOutOfBoundsException e) {
			throw new SimulationException(e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for(Numeral digit : digits)
			sb.append(digit.getValue());

		return sb.toString();
	}

	@Override
	public boolean equals(Object object) {
		if(object instanceof Barcode) {
			Barcode other = (Barcode)object;

			if(other.digits.length != digits.length)
				return false;

			for(int i = 0; i < digits.length; i++)
				if(!digits[i].equals(other.digits[i]))
					return false;

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(digits);
	}
}