package com.vladhq.mynewapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PastesAdapter extends RecyclerView.Adapter<PastesAdapter.MyViewHolder> {

    private List<Paste> pastesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public PastesAdapter(List<Paste> pastesList) {
        this.pastesList = pastesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paste_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Paste paste = pastesList.get(position);
        holder.title.setText(paste.getTitle());
        holder.year.setText(paste.getYear());
    }

    @Override
    public int getItemCount() {
        return pastesList.size();
    }
}