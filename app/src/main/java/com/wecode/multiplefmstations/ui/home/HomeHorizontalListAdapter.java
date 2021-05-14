package com.wecode.multiplefmstations.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.databinding.IndividualCountryItemBinding;
import com.wecode.multiplefmstations.databinding.IndividualGenreItemUpdatedBinding;
import com.wecode.multiplefmstations.databinding.IndividualLanguageItemBinding;
import com.wecode.multiplefmstations.databinding.IndividualRadioBindingImpl;
import com.wecode.multiplefmstations.databinding.IndividualRadioLargeBinding;

import java.util.List;

public class HomeHorizontalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Radio> radioLists;
    private HomeListener listener;
    String type;

    private static final String TAG = "HomeHorizontalListAdapt";

    public HomeHorizontalListAdapter(String type, List<Radio> radioArrayList, HomeListener listener) {
        this.type = type;
        this.radioLists = radioArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type.equalsIgnoreCase("recently_added")) {
            IndividualRadioBindingImpl binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_radio, parent, false);
            return new HomeHorizontalListViewHolder(binding);
        } else if (type.equalsIgnoreCase("mostly_played")) {
            IndividualRadioLargeBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_radio_large, parent, false);
            return new HomeHorizontalListViewHolderSecond(binding);
        } else if (type.equalsIgnoreCase("genres")) {
            IndividualGenreItemUpdatedBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_genre_item_updated, parent, false);
            return new GenreListUpdatedViewHolder(binding);
        } else if (type.equalsIgnoreCase("country")) {
            IndividualCountryItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_country_item, parent, false);
            return new CountryListViewHolder(binding);
        } else if (type.equalsIgnoreCase("language")) {
            IndividualLanguageItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_language_item, parent, false);
            return new LanguageHorizontalViewHolder(binding);
        } else {
            IndividualRadioBindingImpl binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_radio, parent, false);
            return new HomeHorizontalListViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Radio radio = radioLists.get(position);

        if (type.equalsIgnoreCase("recently_added")) {
            HomeHorizontalListViewHolder viewHolder = (HomeHorizontalListViewHolder) holder;
            viewHolder.individualRadioBinding.setData(radio);
            viewHolder.individualRadioBinding.getRoot().setOnClickListener(view -> listener.onRadioClicked(radioLists, position, type));
        } else if (type.equalsIgnoreCase("mostly_played")) {
            HomeHorizontalListViewHolderSecond viewHolder = (HomeHorizontalListViewHolderSecond) holder;
            viewHolder.individualRadioLargeBinding.setData(radio);
            viewHolder.individualRadioLargeBinding.getRoot().setOnClickListener(view -> listener.onRadioClicked(radioLists, position, type));
        } else if (type.equalsIgnoreCase("genres")) {
            GenreListUpdatedViewHolder viewHolder = (GenreListUpdatedViewHolder) holder;
            viewHolder.individualGenreItemUpdatedBinding.setData(radio);
            viewHolder.individualGenreItemUpdatedBinding.getRoot().setOnClickListener(view -> listener.onRadioClicked(radioLists, position, type));

        } else if (type.equalsIgnoreCase("country")) {

            CountryListViewHolder viewHolder = (CountryListViewHolder) holder;
            viewHolder.individualGenreItemBinding.setData(radio);
            viewHolder.individualGenreItemBinding.getRoot().setOnClickListener(view -> listener.onRadioClicked(radioLists, position, type));

        } else if (type.equalsIgnoreCase("language")) {
            LanguageHorizontalViewHolder viewHolder = (LanguageHorizontalViewHolder) holder;
            viewHolder.individualLanguageItemBinding.setData(radio);
            viewHolder.individualLanguageItemBinding.getRoot().setOnClickListener(view -> listener.onRadioClicked(radioLists, position, type));
        }

    }

    @Override
    public int getItemCount() {

        if (radioLists.size() > 5 &&
                (type.equalsIgnoreCase("genres") ||
                        type.equalsIgnoreCase("country") ||
                        type.equalsIgnoreCase("language"))
        ) {
            return 5;
        }

        return radioLists.size();
    }

    //view holder for normal size of image
    static class HomeHorizontalListViewHolder extends RecyclerView.ViewHolder {

        private IndividualRadioBindingImpl individualRadioBinding;

        public HomeHorizontalListViewHolder(IndividualRadioBindingImpl individualRadioBinding) {
            super(individualRadioBinding.getRoot());
            this.individualRadioBinding = individualRadioBinding;
        }
    }

    //view holder for large image
    static class HomeHorizontalListViewHolderSecond extends RecyclerView.ViewHolder {

        private IndividualRadioLargeBinding individualRadioLargeBinding;

        public HomeHorizontalListViewHolderSecond(IndividualRadioLargeBinding individualRadioLargeBinding) {
            super(individualRadioLargeBinding.getRoot());
            this.individualRadioLargeBinding = individualRadioLargeBinding;
        }

    }

    //view holder for country
    static class CountryListViewHolder extends RecyclerView.ViewHolder {

        private IndividualCountryItemBinding individualGenreItemBinding;

        public CountryListViewHolder(IndividualCountryItemBinding individualGenreItemBinding) {
            super(individualGenreItemBinding.getRoot());
            this.individualGenreItemBinding = individualGenreItemBinding;
        }

    }

    //view holder for country
    static class GenreListUpdatedViewHolder extends RecyclerView.ViewHolder {

        private IndividualGenreItemUpdatedBinding individualGenreItemUpdatedBinding;

        public GenreListUpdatedViewHolder(IndividualGenreItemUpdatedBinding individualGenreItemUpdatedBinding) {
            super(individualGenreItemUpdatedBinding.getRoot());
            this.individualGenreItemUpdatedBinding = individualGenreItemUpdatedBinding;
        }

    }

    //view holder for language
    static class LanguageHorizontalViewHolder extends RecyclerView.ViewHolder {

        private IndividualLanguageItemBinding individualLanguageItemBinding;

        public LanguageHorizontalViewHolder(IndividualLanguageItemBinding individualLanguageItemBinding) {
            super(individualLanguageItemBinding.getRoot());
            this.individualLanguageItemBinding = individualLanguageItemBinding;
        }

    }

}
