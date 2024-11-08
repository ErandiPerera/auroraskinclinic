package com.erskinclinic.app.DTO;

public class ResponseDTO {

	private int responseId;
	private String responseAlert;
	private Object data;
	
	
	public ResponseDTO(int responseId, String responseAlert, Object data) {
		super();
		this.responseId = responseId;
		this.responseAlert = responseAlert;
		this.data = data;
	}

	public ResponseDTO() {
		super();	
	}

	public int getResponseId() {
		return responseId;
	}
	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}
	public String getResponseAlert() {
		return responseAlert;
	}
	public void setResponseAlert(String responseAlert) {
		this.responseAlert = responseAlert;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	 
	
	
	
}
