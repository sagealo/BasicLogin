package com.garrettshorr.basiclogin;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

public class RestaurantListActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listViewRestaurant;
    private FloatingActionButton buttonNewRestaurant;
    private Menu menuDelete;
    private List<Restaurant> foundRestaurants;
    public static final String EXTRA_RESTAURANT = "The Restaurant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        wireWidgets();
        populateListView();
        setListeners();

        // TODO: login to a different activity somewhere

    }

    private void setListeners() {
        buttonNewRestaurant.setOnClickListener(this);

    }


    private void populateListView() {
        //refactor to only get the items that belong to the user
        //get the current user's objectId (hint: use backendless.UserService
        //make a dataquery and use th advanced object retrieval pattern
        //to find all restaurants whose ownerId matches the user's objectId
        //sample WHERE clause with a String: name = 'joe'

        String ownerId = Backendless.UserService.CurrentUser().getObjectId();
        String whereClause = "ownerId = '" + ownerId + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        Backendless.Data.of(Restaurant.class).find(queryBuilder,
                new AsyncCallback<List<Restaurant>>() {
                    @Override
                    public void handleResponse(List<Restaurant> foundRestaurants) {
                        // the "foundContact" collection now contains instances of the Contact class.
                        // each instance represents an object stored on the server.
                        RestaurantListActivity.this.foundRestaurants = foundRestaurants;
                        RestaurantAdapter adapter = new RestaurantAdapter(
                                RestaurantListActivity.this,
                                R.layout.item_restaurantlist,
                                foundRestaurants, new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {
                                PopupMenu popup = new PopupMenu(RestaurantListActivity.this, v);
                                popup.getMenuInflater().inflate(R.menu.menu_delete, popup.getMenu());

                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.menu_menudelete_delete:
                                                int index = listViewRestaurant.indexOfChild(v);
                                                deleteItem(index);
                                                Toast.makeText(RestaurantListActivity.this, "index: " + index, Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                        return true;
                                    }

                                });
                                popup.show();
                                return true;
                            }
                        });
                        listViewRestaurant.setAdapter(adapter);
                        //take the clicked object and include it in the Intent
                        //in the Restaurant activity

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                    }
                });

        //Backendless.Data.of(Restaurant.class).find(new AsyncCallback<List<Restaurant>>() {
        // @Override
        // public void handleResponse(List<Restaurant> restaurantList) {
        //    RestaurantAdapter adapter = new RestaurantAdapter(
        //            RestaurantListActivity.this,
        //             R.layout.item_restaurantlist,
        //            restaurantList);
        //   listViewRestaurant.setAdapter(adapter);
        //take the clicked object and include it in the Intent
        //in the Restaurant activity
        //  listViewRestaurant.setOnClickListener(new AdapterView.OnItemClickListener());
        //}

        // @Override
        // public void handleFault(BackendlessFault fault) {
        //     Toast.makeText(RestaurantListActivity.this,
        //              fault.getMessage(), Toast.LENGTH_SHORT).show();
        //  }
        //  });
    }

    private void deleteItem(int index) {
        Restaurant restaurant = foundRestaurants.get(index);
        Backendless.Persistence.of(Restaurant.class).remove(restaurant, new AsyncCallback<Long>(){

            @Override
            public void handleResponse(Long response) {
                populateListView();
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }


    private void wireWidgets() {
        listViewRestaurant =
                findViewById(R.id.listview_restaurantlist);
        buttonNewRestaurant =
                findViewById(R.id.floatingactionbutton_restaurantlistactivity_newrestaurant);
        menuDelete =
                findViewById(R.id.menu_menudelete_delete);

    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(RestaurantListActivity.this, RestaurantActivity.class);
        startActivityForResult(i, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1234) {
            populateListView();
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_menudelete_delete:
                onContextItemSelected(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        return true;
    }
*/

}

