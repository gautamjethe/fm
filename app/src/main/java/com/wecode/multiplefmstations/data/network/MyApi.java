package com.wecode.multiplefmstations.data.network;

import com.wecode.multiplefmstations.data.network.responses.Feedback;
import com.wecode.multiplefmstations.data.network.responses.FeedbackBody;
import com.wecode.multiplefmstations.data.network.responses.RadioList;
import com.wecode.multiplefmstations.data.network.responses.SearchCategories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApi {

    @GET("{path}")
    Call<RadioList>getRadioListObject(@Path("path") String path);

    @GET("{path}")
    Call<List<RadioList>> getRadioList(@Path("path") String path);


    //used for fetching radio list using country/genre
    @GET("{path}")
    Call<RadioList> getHomeRadioList(@Path("path") String path, @Query("type") String type, @Query("id") String id);


    //used to store the count for number of times radio played
    @GET("{path}")
    Call<List<RadioList>> storeRadioCount(@Path(value = "path",encoded = true) String path, @Query("id") Integer radioId);

    @POST("{path}")
    Call<Feedback> sendFeedback(@Path(value = "path",encoded = true) String path, @Body FeedbackBody body);

    //reporting radio
    @POST("{path}")
    @FormUrlEncoded
    Call<Feedback> reportRadio(@Path(value = "path",encoded = true) String path, @Field("radio_list_id") int radioId);

    @GET("{path}")
    Call<List<SearchCategories>> getAllSearchFilterCategory(@Path("path") String path);
}
