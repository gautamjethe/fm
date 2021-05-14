package com.wecode.multiplefmstations.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.databinding.IndividualCountryGenreMoreItemBinding;
import com.wecode.multiplefmstations.databinding.IndividualGenreItemMoreBinding;

import java.util.List;

public class HomeDialogListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Radio> radioLists;
    private HomeListener listener;
    String type;

    public HomeDialogListAdapter(String type, List<Radio> radioArrayList, HomeListener listener) {
        this.type = type;
        this.radioLists = radioArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (type.equalsIgnoreCase("genres")) {
            IndividualGenreItemMoreBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_genre_item_more, parent, false);
            return new DialogGenreListViewHolder(binding);
        } else {
            IndividualCountryGenreMoreItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_country_genre_more_item, parent, false);
            return new DialogListViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Radio radio = radioLists.get(position);
        radio.setType(type);

        if (type.equalsIgnoreCase("genres")) {
            DialogGenreListViewHolder viewHolder = (DialogGenreListViewHolder) holder;
            viewHolder.individualGenreItemMoreBinding.setData(radio);
            viewHolder.individualGenreItemMoreBinding.getRoot().setOnClickListener(view -> listener.onRadioClicked(radioLists, position, type));
        } else {
            DialogListViewHolder viewHolder = (DialogListViewHolder) holder;
            if(type.equalsIgnoreCase("language"))
                viewHolder.individualRadioBinding.icon.setBackgroundResource(R.drawable.ic_language_line);
            else if(type.equalsIgnoreCase("country"))
                viewHolder.individualRadioBinding.icon.setBackgroundResource(R.drawable.ic_map_pin);
            viewHolder.individualRadioBinding.setData(radio);
            viewHolder.individualRadioBinding.getRoot().setOnClickListener(view -> listener.onRadioClicked(radioLists, position, type));
        }
    }

    @Override
    public int getItemCount() {
        return radioLists.size();
    }

    //view holder for normal size of image
    static class DialogListViewHolder extends RecyclerView.ViewHolder {

        private IndividualCountryGenreMoreItemBinding individualRadioBinding;

        public DialogListViewHolder(IndividualCountryGenreMoreItemBinding individualRadioBinding) {
            super(individualRadioBinding.getRoot());
            this.individualRadioBinding = individualRadioBinding;
        }
    }


    //view holder for normal size of image
    static class DialogGenreListViewHolder extends RecyclerView.ViewHolder {

        private IndividualGenreItemMoreBinding individualGenreItemMoreBinding;

        public DialogGenreListViewHolder(IndividualGenreItemMoreBinding individualGenreItemMoreBinding) {
            super(individualGenreItemMoreBinding.getRoot());
            this.individualGenreItemMoreBinding = individualGenreItemMoreBinding;
        }
    }



}
