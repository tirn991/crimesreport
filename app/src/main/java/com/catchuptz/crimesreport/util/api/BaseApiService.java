package com.catchuptz.crimesreport.util.api;


import com.catchuptz.crimesreport.model.ResponseCase;
import com.catchuptz.crimesreport.model.ResponseCaseAttendance;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("authentication/login")
    Call<ResponseBody> loginRequest(@Field("phone") String phone,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("authentication/registration")
    Call<ResponseBody> registerRequest(@Field("name") String nama,
                                       @Field("email") String email,
                                       @Field("phone") String phone,
                                       @Field("actorType") String actorType,
                                       @Field("password") String password);

    @FormUrlEncoded
    @POST("usercases/case")
    Call<ResponseBody> savecase(@Field("user_id") String user_id,
                                @Field("title") String case_title,
                                @Field("case_details") String case_details,
                                @Field("name") String name,
                                @Field("tribe") String tribe,
                                @Field("religion") String religion,
                                @Field("age") String age,
                                @Field("gender") String gender,
                                @Field("residence") String residence,
                                @Field("phone") String phone);

    @FormUrlEncoded
    @POST("messages/message")
    Call<ResponseBody> savemessage(@Field("message") String message,
                                @Field("sender_id") String sender_id,
                                @Field("sender_name") String sender_name,
                                @Field("email") String email);

    @GET("abouts/about/{id}")
    Call<ResponseBody> getaboutus(@Path("id") String id);

    @FormUrlEncoded
    @POST("usercases/caseattendance")
    Call<ResponseBody> savecaseattendance(@Field("case_id") String case_id,
                                @Field("victim_id") String victim_id,
                                @Field("user_id") String user_id,
                                @Field("attendername") String attendername,
                                @Field("attendertitle") String attendertitle,
                                @Field("content") String content);


    @GET("usercases/case/{user_id}")
    Call<ResponseCase> getUserCases(@Path("user_id") String user_id);

    @GET("authentication/user/{user_id}")
    Call<ResponseBody> getuserdata(@Path("user_id") String user_id);

    @GET("usercases/caseattendance/{case_id}/{victim_id}")
    Call<ResponseCaseAttendance> getUserCasesAttendance(@Path("case_id") String case_id, @Path("victim_id") String victim_id);


}
