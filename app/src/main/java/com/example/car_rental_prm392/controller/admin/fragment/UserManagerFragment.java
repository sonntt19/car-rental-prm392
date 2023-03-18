package com.example.car_rental_prm392.controller.admin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.adapter.AdminUserAdapter;
import com.example.car_rental_prm392.dao.DBManager;
import com.example.car_rental_prm392.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserManagerFragment extends Fragment {
    RecyclerView listViewUser;
    private AdminUserAdapter userAdapter;
    private ArrayList<User> listUsers;
    private FloatingActionButton btnCreate;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_manager,container,false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DBManager dbManager = new DBManager(getActivity());
        listViewUser  = view.findViewById(R.id.admin_user_list);
        searchView = view.findViewById(R.id.admin_user_search);
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

        listUsers = dbManager.getAllUser();
        userAdapter = new AdminUserAdapter(getActivity(), listUsers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        listViewUser.setLayoutManager(linearLayoutManager);
        listViewUser.setAdapter(userAdapter);


    }

    private void filterList(String newText) {
        List<User> filterList = new ArrayList<>();
        for (User o:
                listUsers) {
            if (o.getFullName().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(o);
            }
            if (filterList.isEmpty()){
            }else {
                userAdapter.setFilteredList(filterList);
            }
        }
    }
}