package com.example.car_rental_prm392.controller.admin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.adapter.AdminLocationAdapter;
import com.example.car_rental_prm392.controller.admin.AdminCreateLocationActivity;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Location;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LocationManagerFragment extends Fragment {

    RecyclerView listViewLocation;
    private AdminLocationAdapter adminLocationAdapter;
    private ArrayList<Location> listLocations;
    private FloatingActionButton btnCreate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_manager, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DBManager dbManager = new DBManager(getActivity());

//        Declare view
        listViewLocation = view.findViewById(R.id.admin_rv_location);
        btnCreate = view.findViewById(R.id.btn_add_location);

//        Get list Location and set adapter
        listLocations = dbManager.getAllLocation();
        adminLocationAdapter = new AdminLocationAdapter(getActivity(), listLocations);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        listViewLocation.setLayoutManager(linearLayoutManager);
        listViewLocation.setAdapter(adminLocationAdapter);

//      Click to create new Location
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminCreateLocationActivity.class);
                startActivity(intent);
            }
        });

    }

}