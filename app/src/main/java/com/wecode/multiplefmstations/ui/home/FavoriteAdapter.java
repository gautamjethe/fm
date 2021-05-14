package com.wecode.multiplefmstations.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.databinding.IndividualFavoritesBinding;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private static final String TAG = "FavoriteAdapter";
    private List<Radio> radioList;
    private HomeListener homeListener;

    public FavoriteAdapter(List<Radio> radioList, HomeListener homeListener) {
        this.radioList = radioList;
        this.homeListener = homeListener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IndividualFavoritesBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_favorites, parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder viewHolder, int position) {
        viewHolder.individualRadioBinding.setData(radioList.get(position));
        viewHolder.individualRadioBinding.getRoot().setOnClickListener(view -> homeListener.onRadioClicked(radioList, position, "favorites"));
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + radioList.size());
        return radioList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addData(List<Radio> radioList) {
        this.radioList = radioList;
        notifyDataSetChanged();
    }

    //view holder for favorites
    static class FavoriteViewHolder extends RecyclerView.ViewHolder {

        private IndividualFavoritesBinding individualRadioBinding;

        public FavoriteViewHolder(IndividualFavoritesBinding individualRadioBinding) {
            super(individualRadioBinding.getRoot());
            this.individualRadioBinding = individualRadioBinding;
        }
    }
}
