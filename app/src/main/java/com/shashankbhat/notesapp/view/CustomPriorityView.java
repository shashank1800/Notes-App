package com.shashankbhat.notesapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;

import com.shashankbhat.notesapp.R;

/**
 * Created by SHASHANK BHAT on 22-Jul-20.
 */
public class CustomPriorityView extends AppCompatImageView {


    public CustomPriorityView(Context context) {
        super(context);
        Log.i("Custom","c");
        init();
    }

    public CustomPriorityView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        Log.i("Custom","c a");
        init();

    }

    public CustomPriorityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray customPriorityView = context.obtainStyledAttributes(attrs, R.styleable.CustomPriorityView);

        try{
            int attr = customPriorityView.getIndex(0);
            if(attr == R.styleable.CustomPriorityView_priority) {
                int priority = customPriorityView.getInt(attr, 1);
                setPriority(priority);
            }
        }finally {
            customPriorityView.recycle();
        }
    }

    public void setPriority(int priority){

        switch (priority){
            case 1:
                setImageResource(R.drawable.ic_star_green);
                break;
            case 2:
                setImageResource(R.drawable.ic_star_yellow);
                break;
            case 3:
                setImageResource(R.drawable.ic_star_red);
                break;
        }
    }

    private void init(){
        setPriority(1);
    }
}
