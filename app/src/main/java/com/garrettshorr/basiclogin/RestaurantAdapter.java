package com.garrettshorr.basiclogin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    private Context context;
    private int resource;
    private List<Restaurant> restaurantList;
    private View.OnLongClickListener longClickListener;

    public RestaurantAdapter(@NonNull Context context, int resource, @NonNull List<Restaurant> restaurantList, View.OnLongClickListener longClickListener) {
        super(context, resource, restaurantList);
        this.context = context;
        this.resource = resource;
        this.restaurantList = restaurantList;
        this.longClickListener = longClickListener;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent){
        //just like we did with fragments
        //inflate a layout, wire widgets, insert data, return the layout
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_restaurantlist,
                    parent, false);
        }
        convertView.setOnLongClickListener(longClickListener);
        TextView textViewName =
                convertView.findViewById(R.id.textview_restaurantitem_name);
        TextView textViewPrice =
                convertView.findViewById(R.id.textview_restaurantitem_price);
        RatingBar ratingBar =
                convertView.findViewById(R.id.ratingbar_restrauntitem_rating);

        Restaurant currentRestaurant = restaurantList.get(position);
        textViewName.setText(currentRestaurant.getName());

        //get the right number of $ signs
        String price = "";
        for (int i = 0; i <  currentRestaurant.getPrice(); i++) {
            price += "$";
        }
        textViewPrice.setText(price);

        ratingBar.setRating((float)currentRestaurant.getRating());

        return convertView;
    }
}
