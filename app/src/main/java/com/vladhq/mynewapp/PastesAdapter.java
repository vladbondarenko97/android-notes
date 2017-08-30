package com.vladhq.mynewapp;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PastesAdapter extends RecyclerView.Adapter<PastesAdapter.MyViewHolder> {

    private List<Paste> pastesList;


    public class MyViewHolder extends RecyclerView.ViewHolder implements OnCreateContextMenuListener {
        public TextView title, url, data;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            url = (TextView) view.findViewById(R.id.url);
            data = (TextView) view.findViewById(R.id.data);
            view.setOnCreateContextMenuListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Position is " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            title = (TextView) view.findViewById(R.id.title);
            url = (TextView) view.findViewById(R.id.url);
            data = (TextView) view.findViewById(R.id.data);

            menu.setHeaderTitle("Select The Action");
            menu.add("Rename").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Toast.makeText(view.getContext(), "RENAME " + data.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Toast.makeText(view.getContext(), "DELETE", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
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
        holder.url.setText(paste.getUrl());
        holder.data.setText(paste.getData());
    }

    @Override
    public int getItemCount() {
        return pastesList.size();
    }
}