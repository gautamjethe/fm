package com.wecode.multiplefmstations.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.databinding.BottomSheetDetailBinding;

public class DetailBottomDialog extends BottomSheetDialogFragment {
    private BottomSheetDetailBinding binding;
    private Radio radio;

    public DetailBottomDialog(Radio radio) {
        this.radio = radio;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.bottom_sheet_detail, null, false);
        binding.setData(radio);
        return binding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding=null;
    }
}
