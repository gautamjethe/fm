package com.wecode.multiplefmstations.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.airbnb.lottie.LottieAnimationView;
import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.FeedbackBody;
import com.wecode.multiplefmstations.data.network.responses.SearchCategories;
import com.wecode.multiplefmstations.data.network.responses.SearchFilter;
import com.wecode.multiplefmstations.databinding.DialogExitBinding;
import com.wecode.multiplefmstations.databinding.DialogFeedbackBinding;
import com.wecode.multiplefmstations.databinding.DialogFilterBinding;
import com.wecode.multiplefmstations.databinding.DialogReportBinding;
import com.wecode.multiplefmstations.ui.search.SpinnerAdapter;

import java.util.HashMap;
import java.util.List;

public class AppUtil {

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    //dialog for exiting application.
    public static void showExitDialog(Context context, boolean isPlaying, AlertDialogListener dialogListener) {
        String message = "";
        String negativeText = "";
        if (isPlaying) {
            message = context.getResources().getString(R.string.exit_dialog_message);
            negativeText = context.getResources().getString(R.string.exit_negative_button);
        } else {
            message = context.getResources().getString(R.string.exit_dialog_message2);
            negativeText = context.getResources().getString(R.string.exit_negative_button2);
        }


        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        DialogExitBinding binding = DataBindingUtil.inflate(LayoutInflater.from(alertDialog.getContext()), R.layout.dialog_exit, null, false);
        alertDialog.setView(binding.getRoot(), 30, 20, 30, 20);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.exitDialogMessage.setText(message);
        binding.btnMinimize.setText(negativeText);

        binding.btnMinimize.setOnClickListener(view -> {
            alertDialog.dismiss();
            if (isPlaying) dialogListener.onCancel();
        });

        binding.btnExit.setOnClickListener(view -> {
            alertDialog.dismiss();
            dialogListener.onPositive();
        });

        alertDialog.show();
    }

    //dialog for reporting radio station.
    public static void showReportDialog(Context context, AlertDialogListener dialogListener) {

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        DialogReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(alertDialog.getContext()), R.layout.dialog_report, null, false);
        alertDialog.setView(binding.getRoot(), 30, 20, 30, 20);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.btnReport.setOnClickListener(view -> {
            alertDialog.dismiss();
            dialogListener.onPositive();
        });

        binding.btnCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }


    //dialog used for search filter
    public static void showSearchFilterDialog(Context context, List<SearchCategories> searchCategoriesList, SearchFilterListener dialogListener) {
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        DialogFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(alertDialog.getContext()), R.layout.dialog_filter, null, false);
        alertDialog.setView(binding.getRoot(), 30, 20, 30, 20);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);

        //used for spinner default first value
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setId(0);
        searchFilter.setImage("http:");
        searchFilter.setName("None");

        HashMap<String, List<SearchFilter>> searchFilterHashMap = new HashMap<>();

        for (SearchCategories categories : searchCategoriesList) {
            searchFilterHashMap.put(categories.getType(), categories.getData());
        }

        SpinnerAdapter adapter = new SpinnerAdapter(context, searchFilterHashMap.get("language"));
        binding.spinnerLanguage.setAdapter(adapter);


        SpinnerAdapter genreAdapter = new SpinnerAdapter(context, searchFilterHashMap.get("genres"));
        binding.spinnerGenre.setAdapter(genreAdapter);


        SpinnerAdapter countryAdapter = new SpinnerAdapter(context, searchFilterHashMap.get("country"));
        binding.spinnerCountry.setAdapter(countryAdapter);


        if(!adapter.getItem(0).getName().equalsIgnoreCase("None")){
            adapter.insert(searchFilter, 0);
            genreAdapter.insert(searchFilter, 0);
            countryAdapter.insert(searchFilter, 0);
        }


        final String[] language = new String[1];
        final String[] genre = new String[1];
        final String[] country = new String[1];


        binding.spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                language[0] =  searchFilterHashMap.get("language").get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genre[0] =  searchFilterHashMap.get("genres").get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country[0] =  searchFilterHashMap.get("country").get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnApply.setOnClickListener(view -> {
            alertDialog.dismiss();
            dialogListener.onApplySearchFilter(language[0], genre[0], country[0]);
        });

        binding.btnCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }


    //dialog used for search filter
    public static void showFeedbackDialog(Context context, FeedbackListener feedbackListener) {
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        DialogFeedbackBinding binding = DataBindingUtil.inflate(LayoutInflater.from(alertDialog.getContext()), R.layout.dialog_feedback, null, false);
        alertDialog.setView(binding.getRoot(), 30, 20, 30, 20);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);

        binding.btnSubmit.setOnClickListener(view -> {

            String email = binding.emailTextField.getEditText().getText().toString().trim();
            String subject = binding.subjectTextField.getEditText().getText().toString().trim();
            String message = binding.messageTextField.getEditText().getText().toString().trim();

            if (!(subject.length() > 0 && message.length() > 0)) {
                Toast.makeText(context, R.string.feedback_warning_msg, Toast.LENGTH_LONG).show();
                return;
            }

            if (email.length() <= 0)
                email = "Email not provided";


            FeedbackBody body = new FeedbackBody();
            body.setEmail(email);
            body.setTitle(subject);
            body.setDescription(message);

            feedbackListener.onSubmitFeedback(body, binding.progressFeedback, alertDialog);

        });

        binding.btnCancel.setOnClickListener(view -> {
            alertDialog.dismiss();

        });

        alertDialog.show();
    }

    public interface FeedbackListener {
        void onSubmitFeedback(FeedbackBody feedbackBody, LottieAnimationView progressFeedback, AlertDialog alertDialog);
    }

    public interface AlertDialogListener {
        void onPositive();

        void onCancel();

    }

    public interface SearchFilterListener {
        void onApplySearchFilter(String language, String genre, String country);
    }

}
