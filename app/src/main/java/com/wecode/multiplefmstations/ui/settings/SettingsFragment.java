package com.wecode.multiplefmstations.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.repositories.SettingsRepository;
import com.wecode.multiplefmstations.databinding.FragmentSettingsBinding;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SettingsFragment extends Fragment {
    private SettingsViewModel settingsViewModel;
    private static final String TAG = "SettingsFragment";
    FragmentSettingsBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        SettingsRepository repository = new SettingsRepository(getContext());
        SettingsFactory factory = new SettingsFactory(repository);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings,container, false);
        settingsViewModel = new ViewModelProvider(this, factory).get(SettingsViewModel.class);

        binding.setLifecycleOwner(this);

        binding.setViewModel(settingsViewModel);


        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        settingsViewModel.getFeedbackResponse().observe(getViewLifecycleOwner(), response -> {
            Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        binding=null;
    }
}
