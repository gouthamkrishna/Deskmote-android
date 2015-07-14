package com.beaconapp.user.navigation.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beaconapp.user.navigation.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Reminder> reminderlist = null;
    private ArrayList<Reminder> arraylist;

    public ListViewAdapter(Context context,
                           List<Reminder> reminderlist) {
        mContext = context;
        this.reminderlist = reminderlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Reminder>();
        this.arraylist.addAll(reminderlist);
    }

    public class ViewHolder {
        TextView title;
        TextView time;
    }

    @Override
    public int getCount() {
        return reminderlist.size();
    }

    @Override
    public Reminder getItem(int position) {
        return reminderlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_list, null);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.time = (TextView) view.findViewById(R.id.time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.title.setText(reminderlist.get(position).getTag());
        holder.time.setText(reminderlist.get(position).getTime());

        return view;
    }
}