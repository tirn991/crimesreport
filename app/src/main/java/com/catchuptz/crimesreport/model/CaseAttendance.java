package com.catchuptz.crimesreport.model;

import com.google.gson.annotations.SerializedName;


public class CaseAttendance{

    @SerializedName("sn")
    private String sn;

    @SerializedName("case_id")
    private String case_id;

    @SerializedName("victim_id")
    private String victim_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("attendername")
    private String attendername;

    @SerializedName("attendertitle")
    private String attendertitle;

    @SerializedName("content")
    private String content;

    @SerializedName("status")
    private String status;

    @SerializedName("created")
    private String created;

    @SerializedName("modified")
    private String modified;


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getVictim_id() {
        return victim_id;
    }

    public void setVictim_id(String victim_id) {
        this.victim_id = victim_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAttendername() {
        return attendername;
    }

    public void setAttendername(String attendername) {
        this.attendername = attendername;
    }

    public String getAttendertitle() {
        return attendertitle;
    }

    public void setAttendertitle(String attendertitle) {
        this.attendertitle = attendertitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public String toString(){
        return
                "CaseItem{" +
                        "sn = '" + sn + '\'' +
                        ",case_id = '" + case_id + '\'' +
                        ",victim_id = '" + victim_id + '\'' +
                        ",user_id = '" + user_id + '\'' +
                        ",attendername = '" + attendername + '\'' +
                        ",attendertitle = '" + attendertitle + '\'' +
                        ",content = '" + content + '\'' +
                        ",status = '" + status + '\'' +
                        ",modified = '" + modified+ '\'' +
                        ",created = '" + created+ '\'' +
                        "}";
    }
}