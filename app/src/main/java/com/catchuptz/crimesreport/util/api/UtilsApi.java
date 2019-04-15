package com.catchuptz.crimesreport.util.api;
public class UtilsApi {

    public static final String BASE_URL_API = "http://crimes.boimanda.co.tz/api/";

    // Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
