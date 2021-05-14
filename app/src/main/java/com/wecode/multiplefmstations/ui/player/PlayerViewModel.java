package com.wecode.multiplefmstations.ui.player;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wecode.multiplefmstations.data.network.responses.Feedback;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.data.repositories.PlayerRepository;
import com.wecode.multiplefmstations.radio.RadioManager;
import com.wecode.multiplefmstations.utils.AppUtil;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {
    private static final String TAG = "PlayerViewModel";

    private RadioManager radioManager;

    private PlayerRepository repository;

    public MutableLiveData<Radio> radio = new MutableLiveData<>();
    private MutableLiveData<List<Radio>> radioList = new MutableLiveData<>();
    private MutableLiveData<Integer> position = new MutableLiveData<>();

    public MutableLiveData<Boolean> isNextBtnDisabled = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPrevBtnDisabled = new MutableLiveData<>();


    private LiveData<List<Radio>> favoriteListLiveData;
    private MutableLiveData<Boolean> isInFavorites = new MutableLiveData<>();

    private MutableLiveData<Feedback> reportResponseLiveData=new MutableLiveData<>();

    private boolean isTimerSet = false;
    private MutableLiveData<String> timerText = new MutableLiveData<>();
    Handler handler = new Handler();
    Runnable r = () -> {
        Log.e(TAG, "runnable invoked.");
        isTimerSet = false;
        timerText.setValue("none");
        stopPlayer();
    };

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        radioManager = RadioManager.with(application.getApplicationContext());
        repository = new PlayerRepository(application);
        favoriteListLiveData = repository.getFavoriteList();
        isNextBtnDisabled.setValue(false);
        isPrevBtnDisabled.setValue(false);
        isInFavorites.setValue(false);
    }



    public void setRadio(Radio radio) {
        repository.storeRadioCount(radio.getId());      //increase radio view count
        this.radio.setValue(radio);
        if (getFavoriteListLiveData().getValue() != null) {
            boolean isPresent = false;
            for (Radio fav : getFavoriteListLiveData().getValue()) {
                if (radio.getId() == fav.getId()) isPresent = true;
            }
            isInFavorites.setValue(isPresent);
        }
    }

    public void setRadioList(List<Radio> radioList) {
        this.radioList.setValue(radioList);
    }

    public void setPosition(Integer position) {
        this.position.setValue(position);
    }

    public void playOrPause(Radio radio) {
        radioManager.playOrPause(radio);
    }

    public void play(Radio radio) {
        radioManager.play(radio);
    }

    public boolean isPlaying() {
        return radioManager.isPlaying();
    }

    public void bind() {
        radioManager.bind();
    }

    public void unbind() {
        radioManager.unbind();
    }

    public void onReportClicked(View view,int radioId){
        AppUtil.showReportDialog(view.getContext(), new AppUtil.AlertDialogListener() {
            @Override
            public void onPositive() {
                MutableLiveData<Feedback> flag=repository.reportRadio(radioId);
                flag.observe((LifecycleOwner) view.getContext(), feedback -> reportResponseLiveData.setValue(feedback));

            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void onFavoritesClicked(Radio radio) {
        if (isInFavorites.getValue()) {
            isInFavorites.setValue(false);
            repository.delete(radio.getId());
        } else {
            isInFavorites.setValue(true);
            repository.insert(radio);
        }
    }

    public void onTimerClicked(View view) {

        TimerDialog timerDialog = new TimerDialog(view.getContext(), (time, timeText) -> {
            timerText.setValue(timeText);
            if (time == 0) {
                if (isTimerSet) {
                    isTimerSet = false;
                    handler.removeCallbacksAndMessages(null);
                    Log.e(TAG, "onTimerClicked: remove callback for time 0");
                }
            } else {
                if (isTimerSet) {
                    handler.removeCallbacksAndMessages(null);
                    Log.e(TAG, "onTimerClicked: time was already set, so removed callback");
                }
                isTimerSet = true;
                handler.postDelayed(r, time);
            }

        });
        timerDialog.show();

    }

    public void onShareClicked(View view, String radioName) {
        final String appPackageName = view.getContext().getPackageName();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey! I am listening " + radioName + ". Download the app and listen to different radio stations https://play.google.com/store/apps/details?id="
                + appPackageName);
        view.getContext().startActivity(Intent.createChooser(shareIntent, "Share Via:"));
    }

    public void onPlayClicked(Radio radio) {
        playOrPause(radio);
    }

    public void onInfoClicked(View view, Radio radio) {
        DetailBottomDialog detailFragment = new DetailBottomDialog(radio);
        detailFragment.show(((AppCompatActivity) view.getContext()).getSupportFragmentManager(), detailFragment.getTag());
    }

    public void stopPlayer() {
        radioManager.stopPlayer();
    }

    public void onNextClicked(View view) {
        if (position.getValue() != null && radioList.getValue() != null) {
            if (position.getValue() < (radioList.getValue().size() - 1)) {
                isNextBtnDisabled.setValue(false);
                isPrevBtnDisabled.setValue(false);
                position.setValue(position.getValue() + 1);

                if (position.getValue() == radioList.getValue().size() - 1)
                    isNextBtnDisabled.setValue(true);

                setRadio(radioList.getValue().get(position.getValue()));

                Log.e(TAG, "onNextClicked: position " + position.getValue() + ", FM: " + radio.getValue().getName());

                play(radio.getValue());
            } else isNextBtnDisabled.setValue(true);
        }
    }

    public void onPreviousClicked(View view) {
        if (position.getValue() != null && radioList.getValue() != null) {
            if (position.getValue() > 0) {
                isPrevBtnDisabled.setValue(false);
                isNextBtnDisabled.setValue(false);
                position.setValue(position.getValue() - 1);

                if (position.getValue() == 0) isPrevBtnDisabled.setValue(true);

                setRadio(radioList.getValue().get(position.getValue()));

                Log.e(TAG, "onPrevClicked: position " + position.getValue());

                play(radio.getValue());
            } else isPrevBtnDisabled.setValue(true);
        }
    }

    public MutableLiveData<Radio> getRadio() {
        return radio;
    }

    public MutableLiveData<String> getTimerText() {
        return timerText;
    }

    public MutableLiveData<Boolean> getIsNextBtnDisabled() {
        return isNextBtnDisabled;
    }

    public MutableLiveData<Boolean> getIsPrevBtnDisabled() {
        return isPrevBtnDisabled;
    }

    public MutableLiveData<Boolean> getIsInFavorites() {
        return isInFavorites;
    }

    public LiveData<List<Radio>> getFavoriteListLiveData() {
        return favoriteListLiveData;
    }

    public LiveData<Feedback> getReportResponseLiveData() {
        return reportResponseLiveData;
    }
}
