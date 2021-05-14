package com.wecode.multiplefmstations.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wecode.multiplefmstations.MainActivity;
import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.data.repositories.SearchRadioRepository;
import com.wecode.multiplefmstations.databinding.FragmentSearchBinding;
import com.wecode.multiplefmstations.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment implements SearchListener {

    FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;
    private RecyclerView recyclerView;
    private SearchAdapter settingsAdapter;
    List<Radio> radioList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SearchRadioRepository repository = new SearchRadioRepository(getContext());
        SearchFactory factory = new SearchFactory(repository);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        searchViewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewModel(searchViewModel);

        recyclerView = binding.radioListRecyclerView;

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchViewModel.init();
        searchViewModel.getDiscoverAll().observe(getViewLifecycleOwner(), response -> {
            setupRecyclerView(response.getData());
        });

        //search category filter observer
        searchViewModel.getAllSearchFilterCategory().observe(getViewLifecycleOwner(), response -> {

            ArrayList<String> filterList = new ArrayList<>(Arrays.asList(response.split("-")));

            List<Radio> filteredRadioList = new ArrayList<>(this.radioList);
            for (Radio radio : this.radioList) {
                if (!radio.getLanguage().toLowerCase().contains(filterList.get(0).toLowerCase()) && !filterList.get(0).equalsIgnoreCase("None")) {
                    filteredRadioList.remove(radio);
                }

                if (!radio.getGenres().toLowerCase().contains(filterList.get(1).toLowerCase()) && !filterList.get(1).equalsIgnoreCase("None")) {
                    filteredRadioList.remove(radio);
                }

                if (!radio.getCountry().toLowerCase().contains(filterList.get(2).toLowerCase())&& !filterList.get(2).equalsIgnoreCase("None")) {
                    filteredRadioList.remove(radio);
                }
            }

            if(filteredRadioList.size() <= 0){
                binding.noResultLinearLayout.setVisibility(View.VISIBLE);
            }else{
                binding.noResultLinearLayout.setVisibility(View.GONE);
            }

            settingsAdapter.setFilteredList(filteredRadioList);

        });

        //search text observer
        searchViewModel.getText().observe(getActivity(), searchString -> {
            List<Radio> filteredRadioList = new ArrayList<>(this.radioList);
            for (Radio radio : this.radioList) {
                if (!radio.getName().toLowerCase().contains(searchString.toLowerCase())) {
                    filteredRadioList.remove(radio);
                }
            }

            settingsAdapter.setFilteredList(filteredRadioList);

        });

    }

    //set adapter for grid view or radio list
    private void setupRecyclerView(List<Radio> radioList) {
        binding.progressBarSearch.setVisibility(View.GONE);
        if (settingsAdapter == null) {
            this.radioList = radioList;
            settingsAdapter = new SearchAdapter(radioList, this);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 10, true));

            recyclerView.setAdapter(settingsAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);
        } else {
            settingsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRadioClicked(List<Radio> radioList, int position) {

        ((MainActivity)getActivity()).play(radioList, position);
    }

}
