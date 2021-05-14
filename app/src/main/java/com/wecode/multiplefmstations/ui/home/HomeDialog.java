package com.wecode.multiplefmstations.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.utils.AdsUtil;

import java.util.List;

public class HomeDialog extends DialogFragment {

    private View rootView;
    private HomeListener listener;
    List<Radio> radioList;
    String type;
    String title;


    public static HomeDialog newInstance(HomeListener listener, List<Radio> radioList, String type, String title) {
        HomeDialog dialog = new HomeDialog();
        dialog.listener = listener;
        dialog.radioList = radioList;
        dialog.type = type;
        dialog.title = title;
        return dialog;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initViews();
        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen)
                .setView(rootView)
                .setCancelable(false)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return alertDialog;
    }

    private void initViews() {

        rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_genre_country, null, false);

        ((TextView) rootView.findViewById(R.id.textView)).setText(title);
        rootView.findViewById(R.id.backBtn).setOnClickListener(view -> dismiss());
        RecyclerView recyclerView = rootView.findViewById(R.id.dialogRecyclerView);
        LinearLayout adLayoutContainer  = rootView.findViewById(R.id.adLayoutDialog);
        AdsUtil.loadBannerAd(getContext(), adLayoutContainer);


        initRecyclerView(recyclerView);

    }

    //set adapter for country and genres
    private void initRecyclerView(RecyclerView recyclerView) {

        if (type.equalsIgnoreCase("country") || type.equalsIgnoreCase("genres") || type.equalsIgnoreCase("language")) {
            HomeDialogListAdapter homeListAdapter = new HomeDialogListAdapter(type, radioList, listener);
            recyclerView.setAdapter(homeListAdapter);
        } else {
            CountryGenreRadioListAdapter countryGenreRadioListAdapter = new CountryGenreRadioListAdapter(type, radioList, listener);
            recyclerView.setAdapter(countryGenreRadioListAdapter);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(true);
    }


}
