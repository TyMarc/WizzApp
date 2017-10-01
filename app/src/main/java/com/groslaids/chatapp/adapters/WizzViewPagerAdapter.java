package com.groslaids.chatapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.groslaids.chatapp.fragments.ContactsFragment;
import com.groslaids.chatapp.fragments.ConversationsFragment;
import com.groslaids.chatapp.fragments.WizzPagerFragment;

import java.util.ArrayList;

/**
 * Created by marc_ on 2017-10-01.
 */

public class WizzViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private ArrayList<WizzPagerFragment> fragments;

    public WizzViewPagerAdapter(final Context context, final FragmentManager fm) {
        super(fm);
        this.context = context;
        fragments = new ArrayList<>();
        fragments.add(new ConversationsFragment());
        fragments.add(new ContactsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(fragments.get(position).getPageName());
    }
}
