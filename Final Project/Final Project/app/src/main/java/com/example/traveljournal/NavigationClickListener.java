package com.example.traveljournal;

import android.view.View;

public interface NavigationClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);

}
