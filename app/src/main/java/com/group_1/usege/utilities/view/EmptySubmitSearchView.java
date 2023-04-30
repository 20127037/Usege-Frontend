package com.group_1.usege.utilities.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.SearchView;

import com.group_1.usege.R;

public class EmptySubmitSearchView extends SearchView {

    SearchView.SearchAutoComplete mSearchSrcTextView;
    OnQueryTextListener listener;

    public EmptySubmitSearchView(Context context) {
        super(context);
    }

    public EmptySubmitSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptySubmitSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override public void setOnQueryTextListener(OnQueryTextListener listener) {
        super.setOnQueryTextListener(listener);
        this.listener = listener;
        mSearchSrcTextView = findViewById(androidx.appcompat.R.id.search_src_text);
        mSearchSrcTextView.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (listener != null) {
                listener.onQueryTextSubmit(getQuery().toString());
            }
            return true;
        });
    }
}