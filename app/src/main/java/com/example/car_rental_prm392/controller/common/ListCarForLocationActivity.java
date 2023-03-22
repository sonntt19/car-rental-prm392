package com.example.car_rental_prm392.controller.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.adapter.HomeCarAdapter;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;

import java.util.ArrayList;
import java.util.List;

public class ListCarForLocationActivity extends AppCompatActivity {
    RecyclerView listViewCar;
    private HomeCarAdapter carAdapter;
    private ArrayList<Car> listcars;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_car_for_location);
        DBManager dbManager = new DBManager(this);

//        Search
        searchView = findViewById(R.id.list_car_search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        //        List Car
        listViewCar = findViewById(R.id.list_car_location);
        listcars = dbManager.getAllCarAvailableForLocation(getIntent().getIntExtra("locationId", 0));
        carAdapter = new HomeCarAdapter(this, listcars);

        LinearLayoutManager linearLayoutManagerCar = new LinearLayoutManager(this);
        listViewCar.setLayoutManager(linearLayoutManagerCar);
        listViewCar.setAdapter(carAdapter);
    }

    private void filterList(String newText) {
        List<Car> filterList = new ArrayList<>();
        for (Car o :
                listcars) {
            if (o.getName().toLowerCase().contains(newText.toLowerCase())) {
                filterList.add(o);
            }
            if (filterList.isEmpty()) {
                carAdapter.setFilteredList(filterList);
                Toast.makeText(this, "No Car", Toast.LENGTH_LONG).show();
            } else {
                carAdapter.setFilteredList(filterList);
            }
        }
    }
}