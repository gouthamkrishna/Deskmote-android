package com.beaconapp.user.navigation.classes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beaconapp.user.navigation.R;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    Context mContext;
    private List<Reminder> reminderList = null;

    public ListViewAdapter(Context context,
                           List<Reminder> reminderList) {
        mContext = context;
        this.reminderList = reminderList;
    }

    public class ViewHolder {
        TextView title, time, date;
    }

    @Override
    public int getCount() {
        return reminderList.size();
    }

    @Override
    public Reminder getItem(int position) {
        return reminderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.activity_list, null);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.date = (TextView) view.findViewById(R.id.date);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(reminderList.get(position).getTag());
        holder.time.setText(reminderList.get(position).getTime());
        holder.date.setText(reminderList.get(position).getDate());
        return view;
    }
}