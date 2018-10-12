package com.importio.nitin.bakers.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.importio.nitin.bakers.Database.ReceipeEntry;
import com.importio.nitin.bakers.R;

import java.util.List;

public class HomeAdapter extends ArrayAdapter<ReceipeEntry> {
    private Context mContext;
    private List<ReceipeEntry> mList;

    public HomeAdapter(@NonNull Context context, List<ReceipeEntry> list) {
        super(context, R.layout.list_home_item, list);
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_home_item, parent, false);
        TextView name = convertView.findViewById(R.id.receipe_name);
        ImageView image = convertView.findViewById(R.id.receipe_image);

        name.setText(mList.get(position).getName());
        setImage(mList.get(position).getId(), image);
        return convertView;
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

    @Override
    public int getCount() {
        return mList.size();
    }
}
