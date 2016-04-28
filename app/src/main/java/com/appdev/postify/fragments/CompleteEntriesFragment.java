package com.appdev.postify.fragments;

import com.appdev.postify.Controller.DBController;

/**
 * Created by Soeren on 08.04.2016.
 */
public class CompleteEntriesFragment extends EntriesFragment {

    @Override
    public void setupEntryList() {
        adapter.setTabPosition(3);
        adapter.setEntries(DBController.getInstance().readLocalEntries(DBController.ALL, getContext()));
    }
}
