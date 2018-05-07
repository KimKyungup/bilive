package com.scribble.scribble.ui.base.base_fragment;

import com.scribble.scribble.ui.base.base_activity.BasePresenter;

public interface BaseFragmentPresenter extends BasePresenter {
    void onViewCreated();

    void onDestroyView();
}
