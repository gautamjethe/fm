package com.wecode.multiplefmstations.ui.search;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wecode.multiplefmstations.data.network.responses.RadioList;
import com.wecode.multiplefmstations.data.network.responses.SearchCategories;
import com.wecode.multiplefmstations.data.repositories.SearchRadioRepository;
import com.wecode.multiplefmstations.utils.AppUtil;

import java.util.List;

public class SearchViewModel extends ViewModel {


    private SearchRadioRepository repository;
    MutableLiveData<RadioList> mutableLiveData;
    MutableLiveData<List<SearchCategories>> searchCategoriesMutableLiveData;
    MutableLiveData<String> searchFilterString;
    private MutableLiveData<String> searchString;

    public LiveData<String> getText() {
        return searchString;
    }

    public SearchViewModel(SearchRadioRepository repository) {
        this.repository = repository;
        searchString = new MutableLiveData<>();
        searchFilterString = new MutableLiveData<>();
    }

    public void init() {
        if (mutableLiveData != null) {
            return;
        }
        mutableLiveData = repository.discoverAllRadio();
        searchCategoriesMutableLiveData = repository.getAllSearchFilterCategory();
    }

    public LiveData<RadioList> getDiscoverAll() {
        return mutableLiveData;
    }

    public MutableLiveData<String> getAllSearchFilterCategory() {
        return searchFilterString;
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchString.setValue(s.toString());
    }

    public void onFilterClicked(View view) {

        try{
            AppUtil.showSearchFilterDialog(view.getContext(),searchCategoriesMutableLiveData.getValue(), (String language, String genre, String country) -> {
                searchFilterString.setValue(language+"-"+genre+"-"+country);
            });
        }catch(Exception e){}

    }

}