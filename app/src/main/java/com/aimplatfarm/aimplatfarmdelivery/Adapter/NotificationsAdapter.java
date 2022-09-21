package com.aimplatfarm.aimplatfarmdelivery.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.aimplatfarm.aimplatfarmdelivery.Models.UserNotification;
import com.aimplatfarm.aimplatfarmdelivery.R;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotifiewHolder> {

    private Activity context;
    private List<UserNotification> notifications;

    public NotificationsAdapter(Activity context) {
        this.context = context;
    }

    public void setNotifications(List<UserNotification> notifications) {
        this.notifications = notifications;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotifiewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiewHolder holder, int position) {
        holder.title.setText(notifications.get(position).getTitle());
        holder.description.setText(notifications.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotifiewHolder extends RecyclerView.ViewHolder{

        private TextView title, description;

        public NotifiewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.notif_title);
            description = itemView.findViewById(R.id.notif_desc);
        }
    }
}
