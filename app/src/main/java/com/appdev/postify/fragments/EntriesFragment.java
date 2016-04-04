package com.appdev.postify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev.postify.Controller.DBController;
import com.appdev.postify.R;
import com.appdev.postify.adapter.RecyclerAdapter;
import com.appdev.postify.model.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//Steuerung der drei Ansichten über nur ein Fragment in dem die Liste unterschiedlich gefiltert wird !!!

/**
 * A simple {@link Fragment} subclass.
 */
public class EntriesFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    DBController dbController;

    public static EntriesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        EntriesFragment fragment = new EntriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        dbController = DBController.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);

        //Später hier die Liste entsprechend filtern (case mPage):

        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        Log.d("MeineNachricht2", "setup: "+String.valueOf(mPage));

        List<Entry> entries;

        switch (mPage){
            case 1:
                entries = dbController.readLocalEntries(DBController.TODAY, getContext());
                break;
            case 2:
                entries = dbController.readLocalEntries(DBController.WEEK, getContext());
                break;
            case 3:
                entries = dbController.readLocalEntries(DBController.ALL, getContext());
                break;
            default:
                entries = new ArrayList<Entry>();
                break;
        }

        RecyclerAdapter adapter = new RecyclerAdapter(getContext(), entries);
        adapter.setCurrentTab(mPage);

        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }
}
