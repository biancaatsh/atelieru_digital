package com.example.traveljournal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class NavigationTouchListener implements RecyclerView.OnItemTouchListener{


    private NavigationClickListener clicklistener;
    private GestureDetector gestureDetector;

    public NavigationTouchListener(Context context, final RecyclerView recycleView, final NavigationClickListener clicklistener) {
        // the listener that is useed to notify when a context click occurs
        this.clicklistener = clicklistener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

           //Notified when a tap occurs with the up MotionEvent that triggered it.
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            //Called when the user long-presses on this list tile.
            @Override
            public void onLongPress(MotionEvent e) {
                View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clicklistener != null) {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
            clicklistener.onClick(child, rv.getChildAdapterPosition(child));
        }

        return false;
    }

    //
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
