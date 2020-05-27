package com.example.tracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tracker.Constants;
import com.example.tracker.R;
import com.example.tracker.misc.RequestFactory;
import com.example.tracker.models.Item;
import com.example.tracker.models.Notification;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList){
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_list_item,parent,false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationHolder holder, final int position) {
        final Notification notification = notificationList.get(position);
        final Item item = notification.getItem();
        if(notification.isNotified()){
            holder.setAsNotified();
        }
        else{
            holder.setAsNotNotified();
        }
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText("Price: "+item.getPrice());
        holder.foundAt.setText("Found: "+notification.getFoundAt());
        holder.notificationHeader.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if ((holder.isExpanded)) {
                    holder.collapse();
                } else {
                    if(!notification.isNotified()){
                        setNotificationReceived(notification.getId());
                        notification.setNotified(true);
                        holder.setAsNotified();
                    }
                    holder.expand();
                }
            }
        });
        holder.viewInBrowserBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                context.startActivity(browserIntent);
            }
        });
    }

    private void setNotificationReceived(String notificationId){
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(RequestFactory.makeJsonObjectRequest(
                context,
                Request.Method.POST,
                Constants.API_URL + "/notifications/"+ notificationId,
                null,
                null,
                null
        ));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public List<Notification> getNotificationList(){
        return this.notificationList;
    }
}

class NotificationHolder extends RecyclerView.ViewHolder{
    TextView itemName,itemPrice,foundAt;
    Button viewInBrowserBtn;
    LinearLayout notificationHeader;
    private LinearLayout collapsable;
    private ImageView arrow;
    boolean isExpanded = false;
    NotificationHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        foundAt = itemView.findViewById(R.id.foundAt);
        viewInBrowserBtn = itemView.findViewById(R.id.viewInBrowserBtn);
        notificationHeader = itemView.findViewById(R.id.notificationHeader);
        collapsable = itemView.findViewById(R.id.collapsable);
        arrow = itemView.findViewById(R.id.arrow);
    }
    void setAsNotified(){
        this.itemView.setAlpha(0.5f);
    }
    void setAsNotNotified(){
        this.itemView.setAlpha(1f);
    }

    void expand(){
        isExpanded = true;
        arrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        collapsable.setVisibility(View.VISIBLE);
    }

    void collapse(){
        isExpanded = false;
        arrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        collapsable.setVisibility(View.GONE);
    }

}
