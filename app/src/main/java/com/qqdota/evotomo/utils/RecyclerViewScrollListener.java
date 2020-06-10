package com.qqdota.evotomo.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The class that has the capability to hide or show floating action button
 * depending on the scroll direction that the user is doing
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private FloatingActionButton floatingActionButton;

    /**
     * Class Constructor
     * @param floatingActionButton the floating action button to be manipulated by the recycler
     */
    public RecyclerViewScrollListener(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    /**
     * Recycler view onscrolled method
     * @param recyclerView - the parent recycler view
     * @param dx - the force left or right
     * @param dy - the force top or bottom
     */
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        // Check if the scroll direction is downwards hide the button
        if (dy > 0 && floatingActionButton.isShown())
            floatingActionButton.hide();
        // Otherwise show the button
        else if (dy < 0 && !floatingActionButton.isShown())
            floatingActionButton.show();

        super.onScrolled(recyclerView, dx, dy);
    }
}
