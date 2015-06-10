package com.palmyou.fw.exception;

public class ServiceResultException extends RuntimeException {

	/**
	 * 数据错误Exception
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private Object[] errorMessage;
	private String errorResolution;
	private String errorLevle;
	
	@SuppressWarnings("unused")
	private ServiceResultException(){
		super();
	}
	public ServiceResultException(String message){
		super(message);
	}

	@SuppressWarnings("unused")
	private ServiceResultException(Throwable cause) {
        super(cause);
    }
	
	public ServiceResultException(String errorCode,String errorResolution,String errorLevle,Throwable cause,Object... errorMessage){
		this.errorCode = errorCode;
		this.errorLevle = errorLevle;
		this.errorMessage = errorMessage;
		this.errorResolution = errorResolution;
	}
	
	public ServiceResultException(String errorCode,String errorMessage,String errorResolution,String errorLevle){
		this( errorCode, errorMessage, errorResolution, errorLevle,null);
	}
	public ServiceResultException(String errorCode,Object... errorMessage){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public Object[] getErrorMessage() {
		return errorMessage;
	}
	public String getErrorResolution() {
		return errorResolution;
	}
	public String getErrorLevle() {
		return errorLevle;
	}
	
}
