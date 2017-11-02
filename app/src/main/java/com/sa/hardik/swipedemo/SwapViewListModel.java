package com.sa.hardik.swipedemo;

import android.view.View;

/**
 * Created by hardik.chauhan on 02/11/17.
 */

public class SwapViewListModel {

    private View srcView, destView;

    SwapViewListModel(View srcView, View destView) {
        this.srcView = srcView;
        this.destView = destView;
    }

    public View getSrcView() {
        return srcView;
    }

    public View getDestView() {
        return destView;
    }
}
