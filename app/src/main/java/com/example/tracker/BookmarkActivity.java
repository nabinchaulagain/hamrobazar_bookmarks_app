package com.example.tracker;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tracker.misc.RequestFactory;
import com.example.tracker.models.Bookmark;
import com.example.tracker.models.BookmarkCriteria;
import org.json.JSONException;
import org.json.JSONObject;


public class BookmarkActivity extends BaseActivity implements Response.Listener<JSONObject> {
    ListView searchCriteriaList;
    NotificationList bookmarkNotifications;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        setTitle("Loading ...");
        String bookmarkId = getIntent().getStringExtra("bookmarkId");
        searchCriteriaList = findViewById(R.id.searchCriteriaList);
        bookmarkNotifications = findViewById(R.id.bookmarkNotifications);
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
            setTitle(Html.fromHtml(bookmark.getName()+" <small><small><small><small>(bookmarked "+ bookmark.getDate()+")</small></small></small></small>"));
            findViewById(R.id.bookmarkContainer).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            initializeCriteriaList(bookmark.getCriteria());
            bookmarkNotifications.initializeList(response.getJSONArray("notifications"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initializeCriteriaList(BookmarkCriteria criteria){
        String listItems[] = {
                "Search word: "+criteria.getSearchWord(),
                "Condition: "+criteria.getCondition(),
                "Price: " +
                        (criteria.getMinPrice() == 0 && criteria.getMaxPrice() == 0
                                ? "not specified"
                                : criteria.getMinPrice() + " to " + criteria.getMaxPrice())
        };
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,listItems);
        searchCriteriaList.setAdapter(adapter);
    }
}
