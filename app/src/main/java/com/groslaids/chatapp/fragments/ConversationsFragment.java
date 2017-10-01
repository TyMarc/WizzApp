package com.groslaids.chatapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groslaids.chatapp.R;

/**
 * Created by marc_ on 2017-10-01.
 */

public class ConversationsFragment extends WizzPagerFragment {

    public ConversationsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.conversations, container, false);

        return v;
    }

    @Override
    public int getPageName() {
        return R.string.tab_conversations;
    }
}
