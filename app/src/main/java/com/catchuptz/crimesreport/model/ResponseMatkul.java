package com.catchuptz.crimesreport.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMatkul{

	@SerializedName("semuamatkul")
	private List<SemuamatkulItem> semuamatkul;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	public void setSemuamatkul(List<SemuamatkulItem> semuamatkul){
		this.semuamatkul = semuamatkul;
	}

	public List<SemuamatkulItem> getSemuamatkul(){
		return semuamatkul;
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
			"ResponseMatkul{" + 
			"semuamatkul = '" + semuamatkul + '\'' + 
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}