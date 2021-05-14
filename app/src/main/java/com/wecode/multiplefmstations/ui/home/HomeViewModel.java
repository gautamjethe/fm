package com.wecode.multiplefmstations.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.data.network.responses.RadioList;
import com.wecode.multiplefmstations.data.repositories.HomeRepository;
import com.wecode.multiplefmstations.data.repositories.PlayerRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {


    private HomeRepository repository;
    private PlayerRepository playerRepository;

    MutableLiveData<List<RadioList>> homeListMutableData;
    MutableLiveData<RadioList> homeRadioListObjectMutableData;

    LiveData<List<Radio>> radioListLiveData;
    public HomeViewModel(HomeRepository repository, PlayerRepository playerRepository) {
        this.repository = repository;
        this.playerRepository = playerRepository;
    }

    public void init() {
        if (homeListMutableData != null) {
            return;
        }
        homeListMutableData = repository.getHomeItems();
        radioListLiveData = playerRepository.getFavoriteList();
    }


    public LiveData<List<Radio>> getRadioListLiveData() {
        return radioListLiveData;
    }

    public LiveData<List<RadioList>> getHomeListItems() {
        return homeListMutableData;
    }


    //used for fetching country and genre list
    public void initRadioList(String type, String id) {
        homeRadioListObjectMutableData = repository.getHomeRadioListItems(type, id);
    }


    public LiveData<RadioList> getHomeRadioListItems() {
        return homeRadioListObjectMutableData;
    }

}