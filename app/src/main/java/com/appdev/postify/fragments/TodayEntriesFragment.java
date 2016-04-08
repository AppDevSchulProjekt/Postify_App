package com.appdev.postify.fragments;

import android.support.v4.app.Fragment;
import com.appdev.postify.Controller.DBController;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayEntriesFragment extends EntriesFragment {

    @Override
    public void setupEntryList() {
        adapter.setEntries(DBController.getInstance().readLocalEntries(DBController.TODAY, getContext()));
    }
}
