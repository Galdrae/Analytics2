package com.example.deosfriend.design;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by L335A15 on 3/25/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private LayoutInflater inflator;
    private Context context;

    List<Information> data = Collections.emptyList();

    public RecyclerAdapter(Context context, List<Information> data){
        inflator = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.recyclerview_row, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            //icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ( getPosition() == 0 ){
                String sortBy = "Incomplete";
                Intent myIntent = new Intent(v.getContext(), ListView_Database.class);
                myIntent.putExtra("SortBy", sortBy);
                v.getContext().startActivity(myIntent);
            }
            if ( getPosition() == 1 ){
                String sortBy = "Completed";
                Intent myIntent = new Intent(v.getContext(), ListView_Database.class);
                myIntent.putExtra("SortBy", sortBy);
                v.getContext().startActivity(myIntent);
            }
            if ( getPosition() == 2 ){
                Intent myIntent = new Intent(v.getContext(), ListView_Database.class);
                v.getContext().startActivity(myIntent);
            }
            if ( getPosition() == 3 ){
                String sortBy = "Export";
                Intent myIntent = new Intent(v.getContext(), ListView_Database.class);
                myIntent.putExtra("SortBy", sortBy);
                v.getContext().startActivity(myIntent);
            }
        }

/*        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Item clicked at " +getPosition(), Toast.LENGTH_LONG).show();
            if ( getPosition() == 0 ){
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(myIntent);
            }
        }*/
    }
}
