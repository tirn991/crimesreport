package com.catchuptz.crimesreport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDosen{

	@SerializedName("semuadosen")
	private List<SemuadosenItem> semuadosen;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	public void setSemuadosen(List<SemuadosenItem> semuadosen){
		this.semuadosen = semuadosen;
	}

	public List<SemuadosenItem> getSemuadosen(){
		return semuadosen;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ResponseDosen{" + 
			"semuadosen = '" + semuadosen + '\'' + 
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}