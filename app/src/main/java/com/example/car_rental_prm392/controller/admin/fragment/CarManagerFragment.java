package com.example.car_rental_prm392.controller.admin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.adapter.AdminCarAdapter;
import com.example.car_rental_prm392.controller.admin.AdminCreateCarActivity;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Car;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CarManagerFragment extends Fragment {
    RecyclerView listViewCar;
    private AdminCarAdapter carAdapter;
    private ArrayList<Car> listcars;
    private FloatingActionButton btnCreate;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_manager, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DBManager dbManager = new DBManager(getActivity());

        listViewCar = view.findViewById(R.id.admin_car_list);
        searchView = view.findViewById(R.id.admin_car_search);
        btnCreate = view.findViewById(R.id.btn_add_car);

//        Search view for Car name
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

//        Set list car and adapter
        listcars = dbManager.getAllCar();
        carAdapter = new AdminCarAdapter(getActivity(), listcars);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        listViewCar.setLayoutManager(linearLayoutManager);
        listViewCar.setAdapter(carAdapter);


//        Click to create new Car
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminCreateCarActivity.class);
                startActivity(intent);
            }
        });
    }

    private void filterList(String newText) {
        List<Car> filterList = new ArrayList<>();
        for (Car o :
                listcars) {
            if (o.getName().toLowerCase().contains(newText.toLowerCase())) {
                filterList.add(o);
            }
            if (filterList.isEmpty()) {
                Toast.makeText(getActivity(), "No Car", Toast.LENGTH_LONG).show();
            } else {
                carAdapter.setFilteredList(filterList);
            }
        }
    }

}