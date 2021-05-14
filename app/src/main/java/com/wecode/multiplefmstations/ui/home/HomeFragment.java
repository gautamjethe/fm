package com.wecode.multiplefmstations.ui.home;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wecode.multiplefmstations.MainActivity;
import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.data.network.responses.RadioList;
import com.wecode.multiplefmstations.data.repositories.HomeRepository;
import com.wecode.multiplefmstations.data.repositories.PlayerRepository;
import com.wecode.multiplefmstations.databinding.FragmentHomeBinding;
import com.wecode.multiplefmstations.utils.CustomProgressBar;

import java.util.List;

public class HomeFragment extends Fragment implements HomeListener {

    private static final String TAG = "HomeFragment";
    FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private HomeListAdapter homeListAdapter;
    private FavoriteAdapter favoriteAdapter;
    private String DIALOG_GENRE_COUNTRY = "GENRE_COUNTRY_DIALOG";
    private String DIALOG_RADIO_LIST = "RADIO_LIST_DIALOG";
    CustomProgressBar customProgressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeRepository repository = new HomeRepository(getContext());
        PlayerRepository playerRepository = new PlayerRepository((Application) getContext().getApplicationContext());
        HomeFactory factory = new HomeFactory(repository, playerRepository);
        customProgressBar = new CustomProgressBar(getContext());

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);

        binding.setViewModel(homeViewModel);

        recyclerView = binding.homeRecyclerView;

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeViewModel.init();

        homeViewModel.getRadioListLiveData().observe(getActivity(), radioList -> {
            if (radioList != null && !radioList.isEmpty()) {
                binding.favoritesText.setVisibility(View.VISIBLE);
                binding.favoritesRecyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.favoritesText.setVisibility(View.GONE);
                binding.favoritesRecyclerView.setVisibility(View.GONE);
            }
            setupFavoriteRecyclerView(radioList);
        });

        homeViewModel.getHomeListItems().observe(getActivity(), this::setupRecyclerView);

    }

    //set adapter for grid view or radio list
    private void setupRecyclerView(List<RadioList> radioList) {
        if (homeListAdapter == null) {
            binding.progressBarHome.setVisibility(View.GONE);
            homeListAdapter = new HomeListAdapter(getContext(), radioList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(homeListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);
        } else homeListAdapter.notifyDataSetChanged();
    }

    //set adapter for favorites list
    private void setupFavoriteRecyclerView(List<Radio> radioList) {
        if (favoriteAdapter == null) {
            favoriteAdapter = new FavoriteAdapter(radioList, this);
            binding.favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            binding.favoritesRecyclerView.setAdapter(favoriteAdapter);
            binding.favoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            binding.favoritesRecyclerView.setNestedScrollingEnabled(true);
        } else{
            favoriteAdapter.addData(radioList);
            //favoriteAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onRadioClicked(List<Radio> radioList, int position, String type) {

        if (radioList.size() == 0) {
            Toast.makeText(getContext(), "No Radio Stations", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type.equalsIgnoreCase("genres") ||
                type.equalsIgnoreCase("country") ||
                type.equalsIgnoreCase("language")) {
            customProgressBar.showProgressBar();
            homeViewModel.initRadioList(type, radioList.get(position).getId().toString());
            homeViewModel.getHomeRadioListItems().observe(getActivity(), this::setRadioList);

        } else {
            //play radio in case of radio clicked
            ((MainActivity) getActivity()).play(radioList, position);
        }
    }

    private void setRadioList(RadioList radioLists) {
        customProgressBar.hideProgressBar();
        HomeDialog dialog = HomeDialog.newInstance(this, radioLists.getData(), "radio", "Radio Stations");
        dialog.setCancelable(true);
        dialog.show(getChildFragmentManager(), DIALOG_RADIO_LIST);
    }


    @Override
    public void onMoreClicked(List<Radio> radioList, int position, String type, String title) {
        HomeDialog dialog = HomeDialog.newInstance(this, radioList, type, title);
        dialog.setCancelable(true);
        dialog.show(getChildFragmentManager(), DIALOG_GENRE_COUNTRY);
    }

}
