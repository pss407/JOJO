package com.example.jojo.bangguseok.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jojo.bangguseok.R;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView id;
        protected TextView name;
        protected TextView country;


        public CustomViewHolder(View view) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.textView_list_id);
            this.name = (TextView) view.findViewById(R.id.textView_list_name);
            this.country = (TextView) view.findViewById(R.id.textView_list_country);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.id.setText(mList.get(position).getMember_id());
        viewholder.name.setText(mList.get(position).getMember_name());
        viewholder.country.setText(mList.get(position).getMember_country());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}

