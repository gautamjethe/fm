package com.wecode.multiplefmstations.ui.home;

import com.wecode.multiplefmstations.data.network.responses.Radio;

import java.util.List;

public interface HomeListener {
    void onRadioClicked(List<Radio> radioList, int position, String type);
    void onMoreClicked(List<Radio> radioList, int position, String type, String title);
}
