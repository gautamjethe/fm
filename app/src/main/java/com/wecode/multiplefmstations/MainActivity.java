package com.wecode.multiplefmstations;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.databinding.ActivityMainBinding;
import com.wecode.multiplefmstations.radio.PlaybackStatus;
import com.wecode.multiplefmstations.ui.player.PlayerViewModel;
import com.wecode.multiplefmstations.utils.AdsUtil;
import com.wecode.multiplefmstations.utils.AppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        SlidingUpPanelLayout.PanelSlideListener,
        View.OnClickListener {

    private static final String TAG = "MainActivity";
    NavController navController;

    ActivityMainBinding mainBinding;

    PlayerViewModel viewModel;
    ObjectAnimator rotateAnimator;
    ObjectAnimator rotateAnimator2;

    private InterstitialAd mInterstitialAd = new InterstitialAd(this);

    boolean exitFlag = false;
    boolean minimizeFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        MobileAds.initialize(this, initializationStatus -> {
        });

        AdsUtil.loadBannerAd(this, mainBinding.adLayout);
        AdsUtil.loadInterstitialAd(this, mInterstitialAd);

        //hide player on first run
        mainBinding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        mainBinding.slidingLayout.addPanelSlideListener(this);
        mainBinding.slidingLayout.getChildAt(1).setOnClickListener(null);

        //2 rotate animator for radio logo, for COLLAPSED and EXPANDED state
        rotateAnimator = getRotateAnimator(mainBinding.playerLayout.radioLogoImageView);
        rotateAnimator2 = getRotateAnimator(mainBinding.playerLayout.minRadioLogoImageView);

        viewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        observeValues();

        mainBinding.setPlayerViewModel(viewModel);

        mainBinding.navView.setOnNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_home, R.id.navigation_settings)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        mainBinding.navView.getMenu().getItem(1).setChecked(true);

        mainBinding.cardView.setOnClickListener(this);
        mainBinding.playerLayout.btnMinimize.setOnClickListener(this);
        mainBinding.playerLayout.btnClose.setOnClickListener(this);

    }

    public void observeValues() {
        viewModel.getRadio().observe(this, radio -> mainBinding.playerLayout.setRadio(radio));

        viewModel.getFavoriteListLiveData().observe(this, favorites -> {
            for (Radio fav : favorites) {
                Log.e(TAG, "observeValues: " + fav.getName());
            }
        });

        viewModel.getIsInFavorites().observe(this, isInFavorites -> {
            if (isInFavorites)
                mainBinding.playerLayout.favoritesButton.setImageResource(R.drawable.ic_heart_f);
            else mainBinding.playerLayout.favoritesButton.setImageResource(R.drawable.ic_heart);
        });

        viewModel.getIsNextBtnDisabled().observe(this, isNextBtnDisabled -> {
            if (!isNextBtnDisabled)
                mainBinding.playerLayout.nextButton.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
            else
                mainBinding.playerLayout.nextButton.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDarkShadeDisabled), android.graphics.PorterDuff.Mode.SRC_IN);
        });

        viewModel.getIsPrevBtnDisabled().observe(this, isPrevBtnDisabled -> {
            if (!isPrevBtnDisabled)
                mainBinding.playerLayout.previousButton.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
            else
                mainBinding.playerLayout.previousButton.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDarkShadeDisabled), android.graphics.PorterDuff.Mode.SRC_IN);
        });

        viewModel.getTimerText().observe(this, timerText -> {
            if (!timerText.equalsIgnoreCase("none")) {
                mainBinding.playerLayout.timerText.setVisibility(View.VISIBLE);
                mainBinding.playerLayout.timerText.setText(timerText);
            } else {
                mainBinding.playerLayout.timerText.setVisibility(View.GONE);
            }
        });

        viewModel.getReportResponseLiveData().observe(this, response -> {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onClick(View view) {
        int resId = view.getId();

        if (resId == mainBinding.cardView.getId()) {
            loadInterstitialAd();
            mainBinding.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBlue));
            navController.navigate(R.id.navigation_home);
            mainBinding.navView.getMenu().getItem(1).setChecked(true);
        }

        if (resId == mainBinding.playerLayout.btnMinimize.getId())
            mainBinding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        if (resId == mainBinding.playerLayout.btnClose.getId()) {
            mainBinding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            viewModel.stopPlayer();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_home) {
            mainBinding.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBlue)); // will change the background color of the card view to green
            navController.navigate(R.id.navigation_home);

        } else if (id == R.id.navigation_search) {
            mainBinding.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            navController.navigate(R.id.navigation_search);
        } else if (id == R.id.navigation_settings) {
            mainBinding.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorGrey));
            navController.navigate(R.id.navigation_settings);
        }

        loadInterstitialAd();


        return true;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

        if (slideOffset > 0) {
            //changing opacity of view while sliding.
            float alpha = 1 - slideOffset;
            mainBinding.navView.setAlpha(alpha);
            mainBinding.cardView.setAlpha(alpha);
            mainBinding.adLayout.setAlpha(alpha);
            mainBinding.playerLayout.playerContent.setAlpha(slideOffset);
            mainBinding.playerLayout.minimizedLayout.setAlpha(alpha);

            //slide bottomNav up or down
            mainBinding.navView.setTranslationY(mainBinding.cardView.getHeight() * slideOffset);
            mainBinding.cardView.setTranslationY(mainBinding.cardView.getHeight() * slideOffset);
        }


    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            AppUtil.setMargins(mainBinding.holder, 0, 0, 0, 0);
            mainBinding.playerLayout.minimizedLayout.setVisibility(View.GONE);
            mainBinding.adLayout.setVisibility(View.GONE);
        }
        if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            AppUtil.setMargins(mainBinding.holder, 0, 0, 0, 260);
            mainBinding.playerLayout.minimizedLayout.setVisibility(View.VISIBLE);
            mainBinding.adLayout.setVisibility(View.VISIBLE);
        }

        if (newState == SlidingUpPanelLayout.PanelState.HIDDEN)
            AppUtil.setMargins(mainBinding.holder, 0, 0, 0, 0);


    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.bind();
    }

    @Subscribe
    public void onEvent(String status) {

        switch (status) {
            case PlaybackStatus.PLAYING:
                startRotation();
                break;

            case PlaybackStatus.LOADING:

                // loading

                break;


            case PlaybackStatus.ERROR:
                stopRotation();
                Toast.makeText(this, "Error loading radio.", Toast.LENGTH_SHORT).show();
                break;

            case PlaybackStatus.PAUSED:
                stopRotation();
                break;

        }

        if (status.equals(PlaybackStatus.PLAYING)) {
            mainBinding.playerLayout.playStopImageView.setImageResource(R.drawable.ic_stop);
            mainBinding.playerLayout.minBtnPlay.setImageResource(R.drawable.ic_stop);
        } else {
            mainBinding.playerLayout.playStopImageView.setImageResource(R.drawable.ic_play);
            mainBinding.playerLayout.minBtnPlay.setImageResource(R.drawable.ic_play);
        }

        if (status.equals(PlaybackStatus.LOADING)) {
            mainBinding.playerLayout.progressCircular.setVisibility(View.VISIBLE);
            mainBinding.playerLayout.miniProgressCircular.setVisibility(View.VISIBLE);
        } else {
            mainBinding.playerLayout.progressCircular.setVisibility(View.INVISIBLE);
            mainBinding.playerLayout.miniProgressCircular.setVisibility(View.INVISIBLE);
        }


    }

    private ObjectAnimator getRotateAnimator(ImageView imageView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, View.ROTATION, 0f, 360f)
                .setDuration(8000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    private void startRotation() {
        rotateAnimator.start();
        rotateAnimator2.start();
    }

    private void stopRotation() {
        rotateAnimator.pause();
        rotateAnimator2.pause();
    }

    public void play(List<Radio> radioList, int position) {

        Radio radio = radioList.get(position);
        viewModel.setPosition(position);
        viewModel.setRadioList(radioList);
        viewModel.setRadio(radio);

        if (mainBinding.slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
            mainBinding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        viewModel.play(radio);
    }

    public void hidePlayer() {
        mainBinding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void onBackPressed() {

        if (exitFlag) {
            exitFlag = false;
            if (navController.popBackStack()) {
                finish();
            }else finishAffinity();
        } else if (minimizeFlag) {
            minimizeFlag = false;
            moveTaskToBack(true);
        } else {

            AppUtil.showExitDialog(MainActivity.this, viewModel.isPlaying(), new AppUtil.AlertDialogListener() {
                @Override
                public void onPositive() {
                    exitFlag = true;
                    onBackPressed();
                }

                @Override
                public void onCancel() {
                    minimizeFlag = true;
                    onBackPressed();
                }
            });
        }

    }

    //for loading InterstitialAd
    public void loadInterstitialAd() {
        AdsUtil.ADS_LOAD_COUNT += 1;


        if (AdsUtil.ADS_LOAD_COUNT == AdsUtil.SHOW_ADS_WHEN_TAB_COUNT)
            AdsUtil.showInterstitialAd(mInterstitialAd);

    }

}
