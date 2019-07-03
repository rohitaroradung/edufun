package com.example.hp.edufun;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewMargin extends RecyclerView.ItemDecoration {
    private final int columns;
    private int margin;
    private int column;

    /**
     * constructor
     * @param margin desirable margin size in px between the views in the recyclerView
     * @param columns number of columns of the RecyclerView
     */
    public RecyclerViewMargin(@IntRange(from=0)int margin , @IntRange(from=0) int columns ) {
        this.margin = margin;
        this.columns=columns;
        column=0;
    }

    /**
     * Set different margins for the items inside the recyclerView: no top margin for the first row
     * and no left margin for the first column.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        //int position = parent.getChildLayoutPosition(view);
        int position = parent.getChildAdapterPosition ( view);
        //set right margin to all
       // outRect.right = margin;
        //set bottom margin to
     if(position%(columns*2)==0)
     {
         outRect.right=margin;
     }
     else
     {
         outRect.right=2;
     }


    }

}
