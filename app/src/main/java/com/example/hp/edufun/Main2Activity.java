package com.example.hp.edufun;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    private static final DocumentReference mdoc = com.google.firebase.firestore.FirebaseFirestore.
            getInstance().document("Parking/p1");
    RecyclerViewadapter adapter, adapter2;
    RecyclerView recyclerView, recyclerView2;
    ArrayList<Integer> list, list2;
    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

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

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycleview2);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        putdata2();
        updateui();

    }

    public void updateui(){



        mdoc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    Toast.makeText(Main2Activity.this, "fetch complete", Toast.LENGTH_SHORT).show();
                    list = (ArrayList<Integer>) documentSnapshot.get("parking1");
                    list2 = (ArrayList<Integer>)documentSnapshot.get("parking2");
                    adapter = new RecyclerViewadapter(Main2Activity.this,list);
                    adapter2 = new RecyclerViewadapter(Main2Activity.this,list2);
                    recyclerView.setAdapter(adapter);
                    recyclerView2.setAdapter(adapter2);

                }
            }
        });

        RecyclerViewMargin decoration = new RecyclerViewMargin(dptopx(40), 7);
        RecyclerViewMargin decoration2 = new RecyclerViewMargin(dptopx(40), 12);




        recyclerView.addItemDecoration(decoration);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Main2Activity.this,7);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);


        recyclerView2.addItemDecoration(decoration2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(Main2Activity.this,12);
        gridLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView2.setLayoutManager(gridLayoutManager2);



        recyclerView.addOnItemTouchListener(new Main2Activity.RecyclerTouchListener(this,
                recyclerView, new Main2Activity.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Toast.makeText(Main2Activity.this, "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.relative);

                LayoutInflater layoutInflater = (LayoutInflater)Main2Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popupwindow,null);
                FloatingActionButton fab = (FloatingActionButton) customView.findViewById(R.id.close);

                final PopupWindow popupWindow = new PopupWindow(customView, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(android.R.style.DeviceDefault_Light_ButtonBar_AlertDialog);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });


                popupWindow.showAtLocation(linearLayout, Gravity.CENTER,0,0);




                RadioGroup radioGroup = (RadioGroup)  customView.findViewById(R.id.radiogroup);
                int    a=     ((Number)list.get(position)).intValue();
                switch (a) {
                    case 0:

                        radioGroup.check(R.id.available);
                        break;
                    case 1:
                        radioGroup.check(R.id.reserved);
                        break;
                    default:
                        radioGroup.check(R.id.NA);
                        break;
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId)
                        {
                            case R.id.available:
                                list.set(position,0);
                                putdata(list);

                                popupWindow.dismiss();
                                break;

                            case R.id.reserved:
                                list.set(position,1);
                                putdata(list);
                                popupWindow.dismiss();
                                break;

                            case R.id.NA:
                                list.set(position,3);
                                putdata(list);
                                popupWindow.dismiss();
                                break;
                            default:
                                list.set(position,1);
                                putdata(list);
                                popupWindow.dismiss();
                                break;

                        }

                    }
                });

            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(Main2Activity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));



    }
    private int dptopx(int dp)
    {

        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return (int)px;
    }
    public void putdata(ArrayList<Integer> p)
    {



        Map<String,Object> data = new HashMap();
        ArrayList<Integer> parking1 = p;

        data.put("parking1",parking1);
        mdoc.set(data, SetOptions.merge());
    }
    public void putdata2()
    {



        Map<String,Object> data = new HashMap();
        ArrayList<Integer> parking2 = new ArrayList<Integer>();
        for(int i=0;i<144;i++)
        {
            parking2.add(i%4);
        }

        data.put("parking2",parking2);
        mdoc.set(data, SetOptions.merge());
    }



}
