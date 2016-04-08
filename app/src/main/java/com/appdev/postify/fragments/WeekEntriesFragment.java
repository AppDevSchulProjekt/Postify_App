package com.appdev.postify.fragments;

import com.appdev.postify.Controller.DBController;

/**
 * Created by Soere on 08.04.2016.
 */
public class WeekEntriesFragment extends EntriesFragment {

    @Override
    public void setupEntryList() {
        adapter.setEntries(DBController.getInstance().readLocalEntries(DBController.WEEK, getContext()));
    }
}
