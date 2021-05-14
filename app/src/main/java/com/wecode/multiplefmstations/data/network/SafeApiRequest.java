package com.wecode.multiplefmstations.data.network;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.wecode.multiplefmstations.utils.NoConnectivityDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafeApiRequest {

    public static final String NO_CONNECTIVITY_DIALOG = "no_connectivity_dialog";

    public static <T> MutableLiveData<List<T>> callRetrofit(Context context, Call<List<T>> call) {
        MutableLiveData<List<T>> responseList = new MutableLiveData<>();
        Log.e("inside", call.request().url() + "------");

        call.enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                responseList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                showDialog(context, t);
            }
        });
        return responseList;
    }


    public static <T> MutableLiveData<T> callRetrofitObjectResponse(Context context, Call<T> call) {
        MutableLiveData<T> responseObject = new MutableLiveData<>();

        Log.e("inside", call.request().url() + "------");

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                responseObject.setValue(response.body());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                responseObject.setValue(null);
                showDialog(context, t);
            }
        });

        return responseObject;
    }

    private static void showDialog(Context context, Throwable t) {
        Log.e("inside", t.getMessage());
        if (t instanceof NoConnectivityException) {
            NoConnectivityDialog dialog = NoConnectivityDialog.newInstance();
            dialog.show(((FragmentActivity) context).getSupportFragmentManager(), NO_CONNECTIVITY_DIALOG);
        }
    }
}


