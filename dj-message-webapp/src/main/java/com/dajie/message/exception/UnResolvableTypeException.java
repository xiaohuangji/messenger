package com.dajie.message.exception;

public class UnResolvableTypeException extends Exception {

	/**
	 * 
	 */

	private static final long serialVersionUID = 8508484867129840042L;

	public UnResolvableTypeException() {
		super("unknow platform type input");
	}

	public UnResolvableTypeException(int type) {
		super("unknow platform type input : " + type);
	}

}
