package com.wecode.multiplefmstations.data.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wecode.multiplefmstations.data.database.FavoriteDao;
import com.wecode.multiplefmstations.data.database.FavoriteDatabase;
import com.wecode.multiplefmstations.data.network.MyApi;
import com.wecode.multiplefmstations.data.network.SafeApiRequest;
import com.wecode.multiplefmstations.data.network.SetupRetrofit;
import com.wecode.multiplefmstations.data.network.responses.Feedback;
import com.wecode.multiplefmstations.data.network.responses.Radio;

import java.util.List;

public class PlayerRepository extends SafeApiRequest {

    private MyApi api;
    private FavoriteDao favoriteDao;
    Context context;

    public PlayerRepository(Application application) {
        this.context = application.getApplicationContext();
        SetupRetrofit setupRetrofit = new SetupRetrofit(application.getApplicationContext());
        this.api = SetupRetrofit.createService(MyApi.class);
        FavoriteDatabase favoriteDatabase = FavoriteDatabase.getInstance(application);
        favoriteDao = favoriteDatabase.favoriteDao();
    }

    /**
     * increase view for individual radio station.
     */
    public void storeRadioCount(Integer radioId) {
        callRetrofit(context, api.storeRadioCount("radio/count/store", radioId));
    }

    /**
     * increase view for individual radio station.
     */
    public MutableLiveData<Feedback> reportRadio(Integer radioId) {
        return callRetrofitObjectResponse(context, api.reportRadio("streaming/issue/report/store", radioId));
    }

    /**
     * insert radio to database i.e add to favorites
     */
    public void insert(Radio favorite) {
        new InsertAsyncTask(favoriteDao).execute(favorite);
    }

    /**
     * delete radio from database i.e remove from favorites
     */
    public void delete(Integer radioId) {
        new DeleteAsyncTask(favoriteDao).execute(radioId);
    }

    /**
     * retrieve all favorite data from database
     */
    public LiveData<List<Radio>> getFavoriteList() {
        return favoriteDao.getFavoriteList();
    }


    /**
     * database task must be done in background.
     * Class made static because it should not contain reference to FavoriteRepository class.
     */

    private static class InsertAsyncTask extends AsyncTask<Radio, Void, Void> {
        FavoriteDao favoriteDao;

        public InsertAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Radio... favorites) {
            favoriteDao.insert(favorites[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        FavoriteDao favoriteDao;

        public DeleteAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Integer... radioIds) {
            favoriteDao.delete(radioIds[0]);
            return null;
        }
    }
}
