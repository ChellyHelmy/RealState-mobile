package com.poly.realstate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    private Context context;
    private List<House> houseList;

    public HouseAdapter(Context context, List<House> houseList) {
        this.context = context;
        this.houseList = houseList;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_house, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        House house = houseList.get(position);

        // Set data to views
        holder.imgHouse.setImageResource(house.getImageRes());
        holder.tvTitle.setText(house.getTitle());
        holder.tvPrice.setText(house.getPrice());
        holder.tvAddress.setText(house.getAddress());

        // Click listener to open HouseDetailsActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HouseDetailsActivity.class);
            intent.putExtra("imageRes", house.getImageRes());
            intent.putExtra("title", house.getTitle());
            intent.putExtra("price", house.getPrice());
            intent.putExtra("address", house.getAddress());
            intent.putExtra("area", house.getArea());
            intent.putExtra("rooms", house.getRooms());
            intent.putExtra("description", house.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    static class HouseViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHouse;
        TextView tvTitle, tvPrice, tvAddress;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHouse = itemView.findViewById(R.id.imgHouse);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvHousePrice);
            tvAddress = itemView.findViewById(R.id.tvHouseAddress);
        }
    }
}
