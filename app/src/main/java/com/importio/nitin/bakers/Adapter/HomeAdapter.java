package com.importio.nitin.bakers.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.importio.nitin.bakers.Database.ReceipeEntry;
import com.importio.nitin.bakers.HomeListClickListener;
import com.importio.nitin.bakers.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private Context mContext;
    private List<ReceipeEntry> mList;
    private HomeListClickListener mListener;

    public HomeAdapter(@NonNull Context context, List<ReceipeEntry> list, HomeListClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_home_item, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mList.get(position).getName());
        setImage(mList.get(position).getId(), holder.image);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void setImage(int id, ImageView image) {
        switch (id) {
            case 1:
                image.setImageResource(R.drawable.nutella_pie);
                break;
            case 2:
                image.setImageResource(R.drawable.brownies);
                break;
            case 3:
                image.setImageResource(R.drawable.yellow_cake);
                break;
            default:
                image.setImageResource(R.drawable.cheese_cake);
                break;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        private HomeListClickListener mListener;

        public MyViewHolder(View itemView, HomeListClickListener listener) {
            super(itemView);
            this.mListener = listener;
            image = itemView.findViewById(R.id.receipe_image);
            name = itemView.findViewById(R.id.receipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClickListItem(getAdapterPosition());
        }
    }
}
