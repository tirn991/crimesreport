package com.catchuptz.crimesreport.model;

import com.google.gson.annotations.SerializedName;


public class CaseItem{

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("id")
    private String case_id;

    @SerializedName("title")
    private String title;

    @SerializedName("case_details")
    private String case_details;

    @SerializedName("name")
    private String name;

    @SerializedName("tribe")
    private String tribe;

    @SerializedName("religion")
    private String religion;

    @SerializedName("age")
    private String age;

    @SerializedName("gender")
    private String gender;

    @SerializedName("residence")
    private String residence;

    @SerializedName("phone")
    private String phone;

    @SerializedName("created")
    private String created;

    @SerializedName("status")
    private String status;

    @SerializedName("file_number")
    private String file_number;

    public String getFile_number() {
        return file_number;
    }


    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public void setFile_number(String file_number) {
        this.file_number = file_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCase_details() {
        return case_details;
    }

    public void setCase_details(String case_details) {
        this.case_details = case_details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTribe() {
        return tribe;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString(){
        return
                "CaseItem{" +
                        "user_id = '" + user_id + '\'' +
                        ",title = '" + title + '\'' +
                        ",case_details = '" + case_details + '\'' +
                        ",name = '" + name + '\'' +
                        ",tribe = '" + tribe + '\'' +
                        ",religion = '" + religion + '\'' +
                        ",age = '" + age + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",residence = '" + residence + '\'' +
                        ",phone = '" + phone+ '\'' +
                        ",created = '" + created+ '\'' +
                        "}";
    }
}