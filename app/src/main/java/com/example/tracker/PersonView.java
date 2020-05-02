package com.example.tracker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class PersonView extends LinearLayout {
    TextView personName,personAge;
    public PersonView(Context context) {
        super(context);
    }

    public PersonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView(context,attrs);
    }

    public PersonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context,attrs);
        initializeView(context,attrs);
    }

    public PersonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeView(context,attrs);
    }
    private void initializeView(Context context,AttributeSet set){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_person,this,true);
        personName = this.findViewById(R.id.personName);
        personAge = this.findViewById(R.id.personAge);
        TypedArray typedArray = context.obtainStyledAttributes(set,R.styleable.PersonView);
        String name = typedArray.getString(R.styleable.PersonView_name);
        int age = typedArray.getInt(R.styleable.PersonView_age,-1);
        personName.setText(name);
        personAge.setText(""+age);
    }
}