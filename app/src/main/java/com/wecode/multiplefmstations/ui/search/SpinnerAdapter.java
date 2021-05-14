package com.wecode.multiplefmstations.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.SearchFilter;
import com.wecode.multiplefmstations.databinding.CountrySpinnerRowBinding;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<SearchFilter> {

    public SpinnerAdapter(Context context, List<SearchFilter> searchFilterList) {
        super(context, 0, searchFilterList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        CountrySpinnerRowBinding countrySpinnerRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.country_spinner_row, parent,false);
        countrySpinnerRowBinding.setData(getItem(position));


        return countrySpinnerRowBinding.getRoot();
    }


}