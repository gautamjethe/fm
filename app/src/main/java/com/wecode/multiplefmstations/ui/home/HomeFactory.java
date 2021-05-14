package com.wecode.multiplefmstations.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wecode.multiplefmstations.data.repositories.HomeRepository;
import com.wecode.multiplefmstations.data.repositories.PlayerRepository;

class HomeFactory extends ViewModelProvider.NewInstanceFactory {
    private HomeRepository repository;
    private PlayerRepository playerRepository;

    public HomeFactory(HomeRepository repository, PlayerRepository playerRepository) {
        this.repository = repository;
        this.playerRepository = playerRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(repository,playerRepository);
    }
}
