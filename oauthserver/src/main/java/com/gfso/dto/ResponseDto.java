package com.gfso.dto;

import java.util.Map;

public class ResponseDto {
	private String status;
	private String errorMessage;
	private Map<String, String> data;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	public void setSuccess() {
		this.status="success";
	}
	public void setFail(String errorMsg) {
		this.status="fail";
		this.errorMessage = errorMsg;
	}
}
