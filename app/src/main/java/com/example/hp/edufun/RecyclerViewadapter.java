package com.example.hp.edufun;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewadapter extends RecyclerView.Adapter<RecyclerViewadapter.MyViewHolder> {
        private ArrayList<Integer> arrayList;
     Context context;
    public RecyclerViewadapter(Context context, ArrayList<Integer> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView textView;
            LinearLayout linearLayout;
            public MyViewHolder(View v) {
                super(v);
                textView = v.findViewById(R.id.image);
                linearLayout = v.findViewById(R.id.parentLayout);
            }
        }


        // Create new views (invoked by the layout manager)
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapterlayout, parent, false);

             MyViewHolder viewHolder = new MyViewHolder(view);
                   return viewHolder;
        }


    // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

       //  holder.textView.setText(""+arrayList.get(position));
            GradientDrawable rectangle = (GradientDrawable) holder.textView.getBackground();
          int StatusColor;
          int a;


                a=     ((Number)arrayList.get(position)).intValue();


            StatusColor = (int)getParkingeColor(a);
            rectangle.setColor( StatusColor);



        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
          if(arrayList==null)
          {
              Toast.makeText(context, "null array", Toast.LENGTH_SHORT).show();
              return 0;
          }
            return arrayList.size();
        }
    private int getParkingeColor(int status) {
        int magnitudeColorResourceId;

        switch (status) {
            case 0:

                magnitudeColorResourceId = R.color.Available;
                break;
            case 1:
                magnitudeColorResourceId = R.color.reserved;
                break;
            case 2:
                magnitudeColorResourceId = R.color.Occupied;
                break;
            case 3:
                magnitudeColorResourceId = R.color.NA;
                break;
            default:
                magnitudeColorResourceId = R.color.errorMess;
                break;

        }

       return ContextCompat.getColor(context, magnitudeColorResourceId);
    }

    }
