package com.wecode.multiplefmstations.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.wecode.multiplefmstations.data.network.MyApi;
import com.wecode.multiplefmstations.data.network.SafeApiRequest;
import com.wecode.multiplefmstations.data.network.SetupRetrofit;
import com.wecode.multiplefmstations.data.network.responses.Feedback;
import com.wecode.multiplefmstations.data.network.responses.FeedbackBody;

public class SettingsRepository extends SafeApiRequest {


    private static SettingsRepository instance = null;
    MyApi myApi;
    Context context;

    private static final String TAG = "SettingsRepository";

    MutableLiveData<Feedback> feedbackMutableLiveData=new MutableLiveData<>();

    public SettingsRepository(Context context) {
        SetupRetrofit setupRetrofit = new SetupRetrofit(context);
        this.myApi = SetupRetrofit.createService(MyApi.class);
    }

    public static SettingsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsRepository(context);
        }
        return instance;
    }

    public MutableLiveData<Feedback> sendFeedback(FeedbackBody feedbackBody) {
        Log.e(TAG, "sendFeedback: "+feedbackMutableLiveData.hasObservers()+"  active "+feedbackMutableLiveData.hasActiveObservers() );
        return callRetrofitObjectResponse(context, myApi.sendFeedback("feedback/store", feedbackBody));
    }

}


