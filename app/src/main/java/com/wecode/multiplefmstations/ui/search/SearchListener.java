package com.wecode.multiplefmstations.ui.search;

import com.wecode.multiplefmstations.data.network.responses.Radio;

import java.util.List;

public interface SearchListener {

    void onRadioClicked(List<Radio> radioList, int position);
}
