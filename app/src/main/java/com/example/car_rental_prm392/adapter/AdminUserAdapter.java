package com.example.car_rental_prm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car_rental_prm392.R;
import com.example.car_rental_prm392.controller.admin.AdminCarDetailActivity;
import com.example.car_rental_prm392.controller.admin.AdminUserDetailActivity;
import com.example.car_rental_prm392.model.Car;
import com.example.car_rental_prm392.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {
    private Context context;
    private List<User> listUsers;

    public AdminUserAdapter(Context context, List<User> listUsers) {
        this.context = context;
        this.listUsers = listUsers;
    }

    public void setFilteredList(List<User> filteredList) {
        this.listUsers = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = listUsers.get(position);
        if (user == null)
            return;

//        Set information for each user
        holder.tvName.setText(user.getFullName());
        holder.tvEmail.setText(user.getEmail());
        holder.tvPhone.setText(user.getPhoneNumber());
        if (user.getAvatar() != null) {
            byte[] image = user.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.img.setImageBitmap(bitmap);
        }

//      Click to User detail
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byBundle(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listUsers != null)
            return listUsers.size();
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvName, tvEmail, tvPhone;
        private CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.admin_user_img);
            tvName = itemView.findViewById(R.id.admin_user_name);
            tvEmail = itemView.findViewById(R.id.admin_user_email);
            tvPhone = itemView.findViewById(R.id.admin_user_phone);
            cardView = itemView.findViewById(R.id.admin_user_item);
        }
    }

    //    Get User by bundle
    public void byBundle(User user) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(context, AdminUserDetailActivity.class);
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
