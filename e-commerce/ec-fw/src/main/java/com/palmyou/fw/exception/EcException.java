package com.palmyou.fw.exception;

public class EcException extends RuntimeException {

	private static final long serialVersionUID = 7159400011394885590L;

	public EcException() {
		super();
	}

	public EcException(String message, Throwable cause) {
		super(message, cause);
	}

	public EcException(String message) {
		super(message);
	}

	public EcException(Throwable cause) {
		super(cause);
	}
}