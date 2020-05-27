package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tracker.misc.ProgressDialog;
import com.example.tracker.misc.RequestFactory;
import com.example.tracker.models.Bookmark;
import com.example.tracker.models.BookmarkCriteria;

import org.json.JSONObject;

public class AddBookmarkActivity extends BaseActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    private Spinner conditionDropdown;
    private static String[] options = {"any","new","used"};
    private EditText bookmarkName,searchWord,priceMin,priceMax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bookmark);
        setTitle("Add a bookmark");
        conditionDropdown = findViewById(R.id.spinner);
        bookmarkName = findViewById(R.id.bookmarkName);
        searchWord = findViewById(R.id.searchWord);
        priceMax = findViewById(R.id.priceMax);
        priceMin = findViewById(R.id.priceMin);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        conditionDropdown.setAdapter(adapter);
    }
    protected Bookmark getValues(){
        String searchWord = this.searchWord.getText().toString();
        String condition = conditionDropdown.getSelectedItem().toString();
        BookmarkCriteria criteria = new BookmarkCriteria(searchWord);
        criteria.setCondition(condition);
        try {
            int priceMin = Integer.parseInt(this.priceMin.getText().toString());
            criteria.setMinPrice(priceMin);
        }
        catch(NumberFormatException ignored){
        }

        try{
            int priceMax = Integer.parseInt(this.priceMax.getText().toString());
            criteria.setMaxPrice(priceMax);
        }
        catch(NumberFormatException ignored){
        }

        String name = bookmarkName.getText().toString();
        return new Bookmark(name, criteria);
    }

    public void onSubmit(View view){
        Bookmark bookmark = getValues();
        boolean hasError = validate(bookmark);
        if (!hasError){
            sendRequest(bookmark);
            findViewById(R.id.bookmarkForm).setVisibility(View.GONE);
            ProgressDialog dailog = new ProgressDialog(this,"Adding bookmark...");
            dailog.show();
        }
    }

    public void sendRequest(Bookmark bookmark){
        JsonObjectRequest request = RequestFactory.makeJsonObjectRequest(
                this,
                com.android.volley.Request.Method.POST,
                Constants.API_URL+"/bookmarks",
                bookmark.toJSON(),
                this,
                this
        );
        request.setRetryPolicy(new DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldRetryServerErrors(false);
        requestQueue.add(request);
    }

    public boolean validate(Bookmark values){
        boolean hasError = false;
        if(values.getName().trim().equals("")){
            hasError = true;
            bookmarkName.setError("Name is required");
        }
        BookmarkCriteria criteria = values.getCriteria();
        if(criteria.getSearchWord().trim().equals("")){
            hasError = true;
            searchWord.setError("Search word is required");
        }
        if(values.getCriteria() != null){
            if(criteria.getMinPrice() < 0 ){
                hasError = true;
                priceMin.setError("Minimum price is required");
            }
            if(criteria.getMaxPrice() < 0){
                hasError = true;
                priceMax.setError("Maximum price is required");
            }
            if(criteria.getMaxPrice() < criteria.getMinPrice()){
                hasError = true;
                priceMax.setError("Maximum price is less than minimum price");
            }
        }
        return hasError;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(this,"Bookmark added",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this,"Bookmark added",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
