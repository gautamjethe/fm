package com.wecode.multiplefmstations.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.onesignal.OneSignal;
import com.wecode.multiplefmstations.data.network.responses.Feedback;
import com.wecode.multiplefmstations.data.preferences.PrefManager;
import com.wecode.multiplefmstations.data.repositories.SettingsRepository;
import com.wecode.multiplefmstations.utils.AppUtil;


public class SettingsViewModel extends ViewModel {

    public static String SWITCH_KEY = "SWITCH_KEY";
    SettingsRepository repository;
    MutableLiveData<Feedback> feedbackMutableLiveData;


    public SettingsViewModel(SettingsRepository repository) {
        this.repository = repository;
        feedbackMutableLiveData = new MutableLiveData<>();
    }


    //used to turn on/off notifications
    public void onNotificationClicked(View view) {

        if (new PrefManager<Boolean>(view.getContext()).get(SWITCH_KEY, true)) {
            new PrefManager<Boolean>(view.getContext()).set(SWITCH_KEY, false);
            OneSignal.setSubscription(false);
        } else {
            new PrefManager<Boolean>(view.getContext()).set(SWITCH_KEY, true);
            OneSignal.setSubscription(true);
        }

    }

    //redirect the user to play store
    public void onRateUsClicked(View view) {
        final String appPackageName = view.getContext().getPackageName();
        try {
            view.getContext().startActivity(new Intent(
                    Intent.ACTION_VIEW, Uri
                    .parse("market://details?id="
                            + appPackageName)));
        } catch (android.content.ActivityNotFoundException exception) {
            view.getContext().startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + appPackageName)));
        }

    }

    //open privacy policy
    public void onPrivacyPolicyClicked(View view) {
        String path = "https://payments.google.com/payments/apis-secure/get_legal_document?ldo=0&ldt=privacynotice&ldl=en";
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
        WebView wv = new WebView(view.getContext());
        wv.loadUrl(path);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", (dialog, id) -> dialog.dismiss());

        alert.show();
    }

    //share app
    public void onShareClicked(View view) {
        final String appPackageName = view.getContext().getPackageName();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Download our app and listen to different radio stations https://play.google.com/store/apps/details?id="
                + appPackageName);
        view.getContext().startActivity(Intent.createChooser(shareIntent, "Share Via:"));
    }


    //show feedback form
    public void onFeedbackClicked(View view) {
        AppUtil.showFeedbackDialog(view.getContext(), (feedbackBody, progressFeedback, alertDialog) -> {

            progressFeedback.setVisibility(View.VISIBLE);
            MutableLiveData<Feedback> response = repository.sendFeedback(feedbackBody);
            response.observe((LifecycleOwner) view.getContext(), responses -> {
                alertDialog.dismiss();
                progressFeedback.setVisibility(View.GONE);
                feedbackMutableLiveData.setValue(responses);
            });

        });
    }

    public LiveData<Feedback> getFeedbackResponse() {
        return feedbackMutableLiveData;
    }
}