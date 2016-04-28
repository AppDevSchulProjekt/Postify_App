package com.appdev.postify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.postify.Controller.DBController;
import com.appdev.postify.R;
import com.appdev.postify.adapter.RecyclerAdapter;

/**
 * Created by Soeren on 08.04.2016.
 * Base Class of the Fragments for the specific Views of the Entries
 */
public abstract class EntriesFragment extends Fragment {
    public static RecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public EntriesFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                try {
                    DBController.getInstance().readExternalEntries(getContext(), swipeRefreshLayout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);

        setupRecyclerView(view);
        return view;
    }

    /**
     * Sets the adapter with the Entries to show and the Layout for the RecyclerView
     * @param view
     */
    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        adapter = new RecyclerAdapter(getContext());
        setupEntryList();

        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    /**
     * This Method is used to load the Items(Entries) in the RecyclerView
     */
    public abstract void setupEntryList();
}
