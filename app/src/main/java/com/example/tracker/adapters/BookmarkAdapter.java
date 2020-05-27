package com.example.tracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker.BookmarkActivity;
import com.example.tracker.R;
import com.example.tracker.models.Bookmark;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkHolder> {
    private ArrayList<Bookmark> bookmarks;
    private Context context;

    public BookmarkAdapter(Context context,ArrayList<Bookmark> bookmarks){
        super();
        this.context = context;
        this.bookmarks = bookmarks;
    }

    @NonNull
    @Override
    public BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.bookmark_list_item,parent,false);
        return new BookmarkHolder(view);
    }

    @Override
    public int getItemCount() {
        return this.bookmarks.size();
    }

    public ArrayList getList(){
        return this.bookmarks;

    }    @Override
    public void onBindViewHolder(@NonNull BookmarkHolder holder, int position) {
        final Bookmark bookmark = bookmarks.get(position);
        holder.bookmarkTitle.setText(bookmark.getName());
        holder.bookmarkDate.setText(bookmark.getDate());
        holder.searchWord.setText(bookmark.getCriteria().getSearchWord());
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookmarkActivity.class);
                intent.putExtra("bookmarkId",bookmark.getId());
                context.startActivity(intent);
            }
        });
    }
}

class BookmarkHolder extends RecyclerView.ViewHolder{
    TextView bookmarkTitle,bookmarkDate,searchWord;
    BookmarkHolder(@NonNull View itemView) {
        super(itemView);
        bookmarkTitle = itemView.findViewById(R.id.bookmarkTitle);
        bookmarkDate = itemView.findViewById(R.id.bookmarkDate);
        searchWord = itemView.findViewById(R.id.searchWord);
    }
}
