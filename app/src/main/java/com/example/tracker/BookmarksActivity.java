package com.example.tracker;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tracker.adapters.BookmarkAdapter;
import com.example.tracker.misc.RequestFactory;
import com.example.tracker.models.Bookmark;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class BookmarksActivity extends BottomNavigationActivity implements Response.Listener<JSONArray>,Response.ErrorListener {
    private RecyclerView bookmarksList;
    private ProgressBar progressBar;
    NotificationManager notificationManager;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        super.initialize();
        setTitle("Your bookmarks");
        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        bookmarksList = findViewById(R.id.bookmarksList);
        progressBar = findViewById(R.id.progressBar);
        bookmarksList.setLayoutManager(new LinearLayoutManager(this));
        RequestQueue queue = Volley.newRequestQueue(BookmarksActivity.this);
        JsonArrayRequest request = RequestFactory.makeJsonArrayRequest(
                this,
                com.android.volley.Request.Method.GET,
                Constants.API_URL + "/bookmarks",
                null,
                this,
                this
        );
        queue.add(request);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(getItemTouchCallback());
        itemTouchHelper.attachToRecyclerView(bookmarksList);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }

    @Override
    public void onResponse(JSONArray response) {
        ArrayList<Bookmark> bookmarks = new ArrayList<>();
        for(int i = 0 ; i < response.length(); i++){
            try {
                Bookmark bookmark = new Bookmark(response.getJSONObject(i));
                bookmarks.add(bookmark);
            }
            catch(JSONException ex){
                System.out.println(ex.toString());
            }
        }
        bookmarksList.setAdapter(new BookmarkAdapter(this,bookmarks));
        bookmarksList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
    }
    public void goToAddBookmark(View view){
        Intent intent = new Intent(this,AddBookmarkActivity.class);
        startActivity(intent);
    }

    public ItemTouchHelper.SimpleCallback getItemTouchCallback(){
        ItemTouchHelper.SimpleCallback callback= new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(BookmarksActivity.this, android.R.color.holo_red_light))
                        .addActionIcon(android.R.drawable.ic_menu_delete)
                        .create()
                        .decorate();
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                BookmarkAdapter adapter = (BookmarkAdapter) bookmarksList.getAdapter();
                int index = viewHolder.getAdapterPosition();
                ArrayList<Bookmark> list = adapter.getList();
                Bookmark bookmark = list.get(index);
                list.remove(index);
                bookmarksList.getAdapter().notifyItemRemoved(index);
                RequestQueue queue = Volley.newRequestQueue(BookmarksActivity.this);
                queue.add(RequestFactory.makeJsonObjectRequest(
                        BookmarksActivity.this,
                        com.android.volley.Request.Method.DELETE,
                        Constants.API_URL+"/bookmarks/"+bookmark.getId()
                        ,null,
                        null,
                        null
                ));
            }
        };
        return callback;
    }
}
