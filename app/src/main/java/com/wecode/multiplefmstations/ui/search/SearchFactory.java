package com.wecode.multiplefmstations.ui.search;



import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wecode.multiplefmstations.data.repositories.SearchRadioRepository;

class SearchFactory extends ViewModelProvider.NewInstanceFactory {
    private SearchRadioRepository repository;

    public SearchFactory(SearchRadioRepository repository) {
        this.repository = repository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(repository);
    }
}
