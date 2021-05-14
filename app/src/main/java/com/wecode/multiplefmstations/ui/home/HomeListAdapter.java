package com.wecode.multiplefmstations.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wecode.multiplefmstations.R;
import com.wecode.multiplefmstations.data.network.responses.Radio;
import com.wecode.multiplefmstations.data.network.responses.RadioList;
import com.wecode.multiplefmstations.databinding.IndividualCategoryListBinding;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeCategoryListViewHolder> {

    private Context context;
    private List<RadioList> radioLists;
    private HomeListener listener;
    HomeHorizontalListAdapter homeHorizontalListAdapter;
    RecyclerView recyclerView;

    public HomeListAdapter(Context context, List<RadioList> radioArrayList, HomeListener listener) {
        this.context = context;
        this.radioLists = radioArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeCategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IndividualCategoryListBinding individualCategoryListBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.individual_category_list, parent, false);
        recyclerView = individualCategoryListBinding.homeHorizontalRecyclerView;
        return new HomeCategoryListViewHolder(individualCategoryListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoryListViewHolder homeCategoryListViewHolder, int position) {
        RadioList radioList = radioLists.get(position);
        homeCategoryListViewHolder.individualRadioBinding.setRadioList(radioList);
        setHorizontalListAdapter(radioList.getType(), radioList.getData());

        //click event for more button
        homeCategoryListViewHolder.individualRadioBinding.moreImageView.setOnClickListener(view -> listener.onMoreClicked(radioList.getData(), position, radioLists.get(position).getType(), radioLists.get(position).getTitle()));

    }

    @Override
    public int getItemCount() {
        return radioLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addData(List<RadioList> radioList) {
        this.radioLists.addAll(radioList);
    }

    //home category list view holder
    static class HomeCategoryListViewHolder extends RecyclerView.ViewHolder {

        private IndividualCategoryListBinding individualRadioBinding;

        public HomeCategoryListViewHolder(IndividualCategoryListBinding individualRadioBinding) {
            super(individualRadioBinding.getRoot());
            this.individualRadioBinding = individualRadioBinding;
        }
    }

    //setting horizontal list adapter
    private void setHorizontalListAdapter(String type, List<Radio> radioList) {
        try {
            homeHorizontalListAdapter = new HomeHorizontalListAdapter(type, radioList, listener);
            if (type.equalsIgnoreCase("country")) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(context, HORIZONTAL, false));
            }

            recyclerView.setAdapter(homeHorizontalListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(false);
        } catch (Exception ignored) {

        }
    }


}
