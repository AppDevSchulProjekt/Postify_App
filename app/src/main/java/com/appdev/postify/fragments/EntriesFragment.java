package com.appdev.postify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev.postify.R;

//Steuerung der drei Ansichten über nur ein Fragment in dem die Liste unterschiedlich gefiltert wird !!!

/**
 * A simple {@link Fragment} subclass.
 */
public class EntriesFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);

        //Später hier die Liste entsprechend filtern:
        TextView textView = (TextView) view.findViewById(R.id.txt_fragment);
        textView.setText("Fragment #" + mPage);

        return view;
    }
}
