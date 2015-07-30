package com.beaconapp.user.navigation.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beaconapp.user.navigation.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String mNavTitles[];
    private int mIcons[];
    private String mName;
    private String mPath;
    private String mEmail;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        int holderId;
        TextView textView, name, email;
        ImageView imageView, profile;

        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            if(ViewType == TYPE_ITEM) {

                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                holderId = 1;
            }
            else{

                name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                holderId = 0;
            }
        }
    }

    public MyAdapter(String Titles[],int Icons[],String Name,String Email, String Profile){

        this.mNavTitles = Titles;
        this.mIcons = Icons;
        this.mName = Name;
        this.mEmail = Email;
        this.mPath = Profile;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new ViewHolder(view, viewType);
        }
        else if (viewType == TYPE_HEADER) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new ViewHolder(view, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

        if(holder.holderId ==1) {
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position -1]);
        }
        else{
            if(mPath.equals("")){
                holder.profile.setImageResource(R.drawable.profile);
            }
            else {
                holder.profile.setImageBitmap(loadImageFromStorage(mPath));
            }
            holder.name.setText(mName);
            holder.email.setText(mEmail);
        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length+1;
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private Bitmap loadImageFromStorage(String path)
    {

        Bitmap bitmap = null;
        try {
            File file=new File(path, "profile.png");
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return  bitmap;
    }
}