package com.example.tracker;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tracker.misc.RequestFactory;
import com.example.tracker.models.Bookmark;
import com.example.tracker.models.BookmarkCriteria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BookmarkActivity extends BaseActivity implements Response.Listener<JSONObject> {
    ListView searchCriteriaList;
    NotificationList bookmarkNotifications;
    TextView notificationEmptyMsg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        setTitle("Loading ...");
        String bookmarkId = getIntent().getStringExtra("bookmarkId");
        searchCriteriaList = findViewById(R.id.searchCriteriaList);
        bookmarkNotifications = findViewById(R.id.bookmarkNotifications);
        notificationEmptyMsg = findViewById(R.id.notificationEmptyMsg);
        JsonObjectRequest request = RequestFactory.makeJsonObjectRequest(
                this,
                Request.Method.GET,
                Constants.API_URL + "/bookmarks/" + bookmarkId,
                null,
                this,
                null
        );
        requestQueue.add(request);
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            Bookmark bookmark = new Bookmark(response);
            System.out.println(response.toString());
            setTitle(Html.fromHtml(bookmark.getName()+" <small><small><small><small>(bookmarked "+ bookmark.getDate()+")</small></small></small></small>"));
            findViewById(R.id.bookmarkContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            initializeCriteriaList(bookmark.getCriteria());
            JSONArray notifications =response.getJSONArray("notifications");
            if(notifications.length() == 0){
                notificationEmptyMsg.setVisibility(View.VISIBLE);
                notificationEmptyMsg.setText("No notifications found !!!");
                return;
            }
            bookmarkNotifications.initializeList(notifications);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initializeCriteriaList(BookmarkCriteria criteria){
        String[] listItems = {
                "Search word: "+criteria.getSearchWord(),
                "Condition: "+criteria.getCondition(),
                "Price: " +
                        (criteria.getMinPrice() == 0 && criteria.getMaxPrice() == 0
                                ? "not specified"
                                : criteria.getMinPrice() + " to " + criteria.getMaxPrice())
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listItems);
        searchCriteriaList.setAdapter(adapter);
    }
}
