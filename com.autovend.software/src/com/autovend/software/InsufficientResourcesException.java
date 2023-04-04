
package com.autovend.software;

public class InsufficientResourcesException extends Exception{

    private static final long serialVersionUID = 1L;

    public InsufficientResourcesException() {}

    public InsufficientResourcesException(String message) {
        super(message);
    }
}
