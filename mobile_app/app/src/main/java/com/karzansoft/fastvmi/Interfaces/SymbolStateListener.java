package com.karzansoft.fastvmi.Interfaces;

import com.karzansoft.fastvmi.extended.SymbolMarker;

/**
 * Created by Yasir on 3/14/2016.
 */
public interface SymbolStateListener {

    void onSymbolAdded(SymbolMarker symbolMarker);
    void onRemoved(SymbolMarker symbolMarker);
    void onSymbolSelected(SymbolMarker symbolMarker);
}
