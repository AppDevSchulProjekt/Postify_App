package com.appdev.postify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdev.postify.R;
import com.appdev.postify.model.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soeren on 21.03.2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private List<Entry> entries;
    private LayoutInflater inflater;
    private int tabPosition;
    private Context context;

    public RecyclerAdapter(Context context){
        this.context = context;
        this.entries = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Entry currentEntry = entries.get(position);
        holder.setData(currentEntry, position);
    }

    @Override
    public int getItemCount() {
        return this.entries.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView dateTextView;
        TextView weightTextView;
        int position;
        Entry currentEntry;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.img_mail);
            dateTextView = (TextView) itemView.findViewById(R.id.txt_title);
            weightTextView = (TextView) itemView.findViewById(R.id.txt_description);
        }

        public void setData(Entry currentEntry, int position) {
            this.currentEntry = currentEntry;
            this.position = position;

            imageView.setImageResource(R.drawable.ic_email_outline_black_48dp);
            dateTextView.setText(currentEntry.getFormattedTime(tabPosition, context.getString(R.string.cap_today), context.getString(R.string.cap_yesterday)));
            weightTextView.setText(currentEntry.getWeight().toString() + " " + context.getString(R.string.weight_gramm));
        }
    }

    public void setEntries(ArrayList<Entry> entries){
        this.entries = entries;
    }

    public void setTabPosition(int tabPosition){
        this.tabPosition = tabPosition;
    }
}