package com.wecode.multiplefmstations.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.preferences.PrefManager;

import static com.wecode.multiplefmstations.ui.settings.SettingsViewModel.SWITCH_KEY;

public class BindingAdapters {
    @BindingAdapter("android:text")
    public static void setNonEmptyText(TextView textView, String text) {
        if (TextUtils.isEmpty(text)) textView.setText("N/A");
        else textView.setText(text);
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        try {
            Glide
                    .with(view.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.placeholder)
                    .into(view);
        } catch (Exception ignored) {
        }
    }

    //called from layout to set visibility of more button
    @BindingAdapter("android:visibility")
    public static void showHideMore(ImageView view, String type) {
        if (type.equalsIgnoreCase("genres") || type.equalsIgnoreCase("country") || type.equalsIgnoreCase("language")) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({"android:checked"})
    public static void checkSwitchStatus(SwitchCompat view, boolean isOnOff) {
        view.setChecked(new PrefManager<Boolean>(view.getContext()).get(SWITCH_KEY, true));
    }


}
