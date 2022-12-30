package com.example.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LoadMoreListener extends RecyclerView.OnScrollListener {
    private GridLayoutManager gridLayoutManager;

    public LoadMoreListener(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = gridLayoutManager.getChildCount();
        int totalItemCount = gridLayoutManager.getItemCount();
        int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

        Log.d("visibleItemCount: ",String.valueOf(visibleItemCount));
        Log.d("totalItemCount: ", String.valueOf(totalItemCount));
        Log.d("firstVisibleItem: ", String.valueOf(firstVisibleItem));


//        if(isLoading() || isLastItem()){
//            return;
//        }
//        if(firstVisibleItem >= 0 && (visibleItemCount + firstVisibleItem) >= totalItemCount){
//            loadMoreItem();
//        }
    }

    public abstract void loadMoreItem();
    public abstract boolean isLoading();
    public abstract boolean isLastItem();
}
