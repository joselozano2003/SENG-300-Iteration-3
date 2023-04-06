package com.autovend.devices;

/**
 * Represents a simple device (like, say, a tube or just a physical connection)
 * that moves things between other devices. This channel is bidirectional.
 * 
 * @param <T>
 *            The type of the things to move.
 */
final class BidirectionalChannel<T> {
	private FlowThroughEmitter<T> source;
	private Acceptor<T> sink;

	/**
	 * Constructs a new channel whose input is connected to the indicated source and
	 * whose output is connected to the indicated sink.
	 * 
	 * @param source
	 *            The device at the output end of the channel.
	 * @param sink
	 *            The device at the output end of the channel.
	 */
	BidirectionalChannel(FlowThroughEmitter<T> source, Acceptor<T> sink) {
		this.source = source;
		this.sink = sink;
	}

	/**
	 * Moves the indicated thing to the source. This method should be called by
	 * the sink device, and not by an external application.
	 * 
	 * @param thing
	 *            The thing to transport via the channel.
	 * @throws OverloadException
	 *             if the sink has no space for the bill.
	 * @throws DisabledException
	 *             if the sink is currently disabled.
	 */
	void eject(T thing) throws OverloadException, DisabledException {
		source.emit(thing);
	}

	/**
	 * Moves the indicated bill to the sink. This method should be called by the
	 * source device, and not by an external application.
	 * 
	 * @param bill
	 *            The bill to transport via the channel.
	 * @throws OverloadException
	 *             if the sink has no space for the bill.
	 * @throws DisabledException
	 *             if the sink is currently disabled.
	 */
	void deliver(T bill) throws OverloadException, DisabledException {
		sink.accept(bill);
	}

	/**
	 * Returns whether the sink has space for at least one more bill.
	 * 
	 * @return true if the sink can accept a bill; false otherwise.
	 */
	boolean hasSpace() {
		return sink.hasSpace();
	}
}
