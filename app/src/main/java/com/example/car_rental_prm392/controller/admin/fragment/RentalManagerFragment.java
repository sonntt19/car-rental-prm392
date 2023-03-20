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
import com.example.car_rental_prm392.adapter.HistoryRentalAdapter;
import com.example.car_rental_prm392.controller.admin.AdminCreateLocationActivity;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.Location;
import com.example.car_rental_prm392.model.Rental;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RentalManagerFragment extends Fragment {

    RecyclerView listViewRental;
    private HistoryRentalAdapter historyRentalAdapter;
    private ArrayList<Rental> listRentals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rental_manager, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DBManager dbManager = new DBManager(getActivity());

        listViewRental = view.findViewById(R.id.history_rv_rental);

//        Get list Rental and set adapter
        listRentals = dbManager.getAllRental();
        historyRentalAdapter = new HistoryRentalAdapter(getActivity(), listRentals);
        LinearLayoutManager linearLayoutManagerLocation = new LinearLayoutManager(getActivity());
        listViewRental.setLayoutManager(linearLayoutManagerLocation);
        listViewRental.setAdapter(historyRentalAdapter);

    }

}