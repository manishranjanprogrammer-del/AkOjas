package com.ojassoft.astrosage.customexceptions;

public class NoInternetException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NoInternetException() {
        super();
    }

    public NoInternetException(Throwable cause) {
        super(cause);

    }

    public NoInternetException(String message) {
        super(message);

    }

    public NoInternetException(String message, Throwable cause) {
        super(message, cause);

    }

}
