package com.garrettshorr.basiclogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class RestaurantActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextName;
    private EditText editTextCuisine;
    private EditText editTextAddress;
    private EditText editTextWebsiteLink;
    private RatingBar ratingBarRating;
    private SeekBar seekBarPrice;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        wireWidgets();
        setListeners();
        prefillFields();
    }

    private void prefillFields() {
        Intent restaurantIntent = getIntent();
        Restaurant restaurant =
                restaurantIntent.getParcelableExtra(RestaurantListActivity.EXTRA_RESTAURANT);
        if(restaurant!=null)
        {
            editTextName.setText(restaurant.getName());
            editTextWebsiteLink.setText(restaurant.getWebsiteLink());
            editTextAddress.setText(restaurant.getAddress());
            editTextCuisine.setText(restaurant.getCuisine());
            ratingBarRating.setRating((float)restaurant.getRating());
            seekBarPrice.setProgress(restaurant.getPrice() - 1);

        }
    }

    private void setListeners() {
        buttonSave.setOnClickListener(this);
    }


    private void wireWidgets() {
        editTextName = findViewById(R.id.edittext_restaurantactivity_name);
        editTextCuisine = findViewById(R.id.edittext_restaurantactivity_cuisine);
        editTextAddress = findViewById(R.id.edittext_restaurantactivity_address);
        editTextWebsiteLink = findViewById(R.id.edittext_restaurantactivity_website);
        seekBarPrice = findViewById(R.id.seekbar_rastaurantactivity_price);
        ratingBarRating = findViewById(R.id.ratingbar_restaurantactivity_rating);
        buttonSave = findViewById(R.id.button_restaurantactivity_save);
    }

    @Override
    public void onClick(View v) {
        Restaurant r = new Restaurant();
        r.setName( editTextName.getText().toString() );
        r.setCuisine( editTextCuisine.getText().toString() );
        r.setAddress( editTextAddress.getText().toString() );
        r.setWebsiteLink( editTextWebsiteLink.getText().toString() );
        r.setPrice(seekBarPrice.getProgress());
        r.setRating(ratingBarRating.getRating());

        // save object synchronously
        //Restaurant savedRestaurant = Backendless.Data.save( r );

        // save object asynchronously
        Backendless.Data.save( r, new AsyncCallback<Restaurant>() {
            public void handleResponse( Restaurant response )
            {
                // new Restaurant instance has been saved
                //Intent i = new Intent(RestaurantActivity.this, RestaurantListActivity.class);
                //startActivity(i);
                setResult(RESULT_OK);
                finish();
            }

            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()

            }
        });

    }
}
