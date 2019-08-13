package com.example.googlemap.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.googlemap.ApiResponses.NearByPlaces_Response;
import com.example.googlemap.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class NearByPlaces_Adapter extends RecyclerView.Adapter<NearByPlaces_Adapter.ItemViewHolder> {

    Context context;
    List<NearByPlaces_Response.Result> resultList = new ArrayList<>();

    public NearByPlaces_Adapter(Context context, List<NearByPlaces_Response.Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.near_by_place_item,parent,false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.name.setText(resultList.get(position).getName());
            holder.address.setText(resultList.get(position).getVicinity());

            if (resultList.get(position).getOpeningHours() != null) {
                if (resultList.get(position).getOpeningHours().getOpenNow() == "true") {
                    holder.open_close.setText("OPEN");
                    holder.open_close.setTextColor(Color.GREEN);
                } else if (resultList.get(position).getOpeningHours().getOpenNow() == "false") {
                    holder.open_close.setText("CLOSE");
                    holder.open_close.setTextColor(Color.RED);
                }
            }
            else
            {
                holder.open_close.setVisibility(View.GONE);
            }


            if (resultList.get(position).getRating()!= "null") {
                holder.rating.setText(resultList.get(position).getUserRatingsTotal().toString());
            }

            Glide.with(context).load(resultList.get(position).getIcon()).placeholder(R.drawable.ic_location_on).error(R.drawable.ic_location_on).into(holder.icon);

    }

    @Override
    public int getItemCount() {
        Log.e("adapter","list size: "+resultList.size());
        return resultList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView name,address,open_close,rating;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.nearBy_icon);
            name = itemView.findViewById(R.id.nameNearBy);
            address = itemView.findViewById(R.id.addressNearBy);
            open_close = itemView.findViewById(R.id.open_closeNearBy);
            rating = itemView.findViewById(R.id.ratingNearBy);


        }
    }
}
