package com.catchuptz.crimesreport.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseCaseAttendance{

    @SerializedName("caseitem")
    private ArrayList<CaseAttendance> caseitem;

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    public ArrayList<CaseAttendance> getCaseitem() {
        return caseitem;
    }

    public void setCaseitem(ArrayList<CaseAttendance> caseitem) {
        this.caseitem = caseitem;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return
                "ResponseCaseItem{" +
                        "caseitem = '" + caseitem + '\'' +
                        ",status = '" + status + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}