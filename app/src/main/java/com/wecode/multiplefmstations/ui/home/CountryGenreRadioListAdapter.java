package com.wecode.multiplefmstations.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.databinding.IndividualRadioDetailsItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CountryGenreRadioListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LottieAnimationView> items;
    private List<Radio> radioLists;
    private HomeListener listener;
    String type;

    public CountryGenreRadioListAdapter(String type, List<Radio> radioArrayList, HomeListener listener) {
        this.type = type;
        this.radioLists = radioArrayList;
        this.listener = listener;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

             IndividualRadioDetailsItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_radio_details_item, parent, false);
            return new DialogRadioListViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Radio radio = radioLists.get(position);
        radio.setType(type);
        DialogRadioListViewHolder viewHolder = (DialogRadioListViewHolder) holder;
        //add all the view for selected item to array
        items.add(viewHolder.individualRadioBinding.animationView);
        viewHolder.individualRadioBinding.setData(radio);
        viewHolder.individualRadioBinding.getRoot().setOnClickListener(v -> {
            removeAnimation();
            viewHolder.individualRadioBinding.animationView.setVisibility(View.VISIBLE);
            listener.onRadioClicked(radioLists, position, type);
        });
    }

    @Override
    public int getItemCount() {
        return radioLists.size();
    }

    //view holder for normal size of image
    static class DialogRadioListViewHolder extends RecyclerView.ViewHolder {

        private IndividualRadioDetailsItemBinding individualRadioBinding;

        public DialogRadioListViewHolder(IndividualRadioDetailsItemBinding individualRadioBinding) {
            super(individualRadioBinding.getRoot());
            this.individualRadioBinding = individualRadioBinding;
        }
    }



    private void removeAnimation() {
        for(LottieAnimationView item : items) {
            item.setVisibility(View.INVISIBLE);
        }
    }

}
