package com.example.hamrobookmark.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hamrobookmark.misc.Constants;
import com.example.hamrobookmark.R;
import com.example.hamrobookmark.activities.AddBookmarkActivity;
import com.example.hamrobookmark.activities.BaseActivity;
import com.example.hamrobookmark.adapters.BookmarkAdapter;
import com.example.hamrobookmark.misc.RequestFactory;
import com.example.hamrobookmark.models.Bookmark;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static java.util.Objects.requireNonNull;

public class BookmarksFragment extends Fragment implements Response.Listener<JSONArray>,Response.ErrorListener {
    private RecyclerView bookmarksList;
    private ProgressBar progressBar;
    private TextView bookmarksEmptyMsg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getContext();
        bookmarksList = view.findViewById(R.id.bookmarksList);
        progressBar = view.findViewById(R.id.progressBar);
        FloatingActionButton addBtn = view.findViewById(R.id.floatingActionButton);
        bookmarksEmptyMsg = view.findViewById(R.id.bookmarksEmptyMsg);
        bookmarksList.setLayoutManager(new LinearLayoutManager(context));
        JsonArrayRequest request = RequestFactory.makeJsonArrayRequest(
                context,
                com.android.volley.Request.Method.GET,
                Constants.API_URL + "/bookmarks",
                null,
                this,
                this
        );
        ((BaseActivity) requireNonNull(getActivity())).requestQueue.add(request);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(getItemTouchCallback());
        itemTouchHelper.attachToRecyclerView(bookmarksList);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddBookmark();
            }
        });

    }
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }

    @Override
    public void onResponse(JSONArray response) {
        if(response.length() == 0){
            bookmarksEmptyMsg.setText(R.string.msg_empty_bookmarks);
            bookmarksEmptyMsg.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        ArrayList<Bookmark> bookmarks = new ArrayList<>();
        for(int i = 0 ; i < response.length(); i++){
            try {
                Bookmark bookmark = new Bookmark(response.getJSONObject(i));
                bookmarks.add(bookmark);
            }
            catch(JSONException ex){
                ex.printStackTrace();
            }
        }
        bookmarksList.setAdapter(new BookmarkAdapter(getContext(),bookmarks));
        bookmarksList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        requireNonNull(getActivity()).findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
    }

    private void goToAddBookmark(){
        Intent intent = new Intent(getActivity(), AddBookmarkActivity.class);
        startActivity(intent);
    }

    private ItemTouchHelper.SimpleCallback getItemTouchCallback(){
        return new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(requireNonNull(getContext()), android.R.color.holo_red_light))
                        .addActionIcon(android.R.drawable.ic_menu_delete)
                        .create()
                        .decorate();
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                BookmarkAdapter adapter = (BookmarkAdapter) requireNonNull(bookmarksList.getAdapter());
                int index = viewHolder.getAdapterPosition();
                List list = adapter.getList();
                Bookmark bookmark = (Bookmark) list.get(index);
                list.remove(index);
                bookmarksList.getAdapter().notifyItemRemoved(index);
                ((BaseActivity) requireNonNull(getActivity())).requestQueue.add(RequestFactory.makeJsonObjectRequest(
                        getContext(),
                        com.android.volley.Request.Method.DELETE,
                        Constants.API_URL+"/bookmarks/"+bookmark.getId()
                        ,null,
                        null,
                        null
                ));
            }
        };
    }
}
