package com.example.hp.edufun;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;


public class ParkingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location location;
    Double lat, lon;
    private String Direction_request_url = "https://maps.googleapis.com/maps/api/directions/json?parameters";
    private final String api_key = "AIzaSyBmKeDr7OYaMhNwgWmFkhLCy-0bpRlCUwI";
    protected LatLng start;
    protected LatLng end;


    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};


    private final int requestcode = 123;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FloatingActionButton btnp1, btnp2;
    private static final DocumentReference mdoc = com.google.firebase.firestore.FirebaseFirestore.
            getInstance().document("Parking/p1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ToggleButton toggle = (ToggleButton) findViewById(R.id.info);
        btnp1 = (FloatingActionButton) findViewById(R.id.btnP1);
        btnp2 = (FloatingActionButton) findViewById(R.id.btnP2);
        final ConstraintLayout main_content = (ConstraintLayout)findViewById(R.id.main_content);
        ImageButton half_expand_button = (ImageButton) findViewById(R.id.half_exposed_button);

        NestedScrollView nestedScrollView = (NestedScrollView)findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView);
        CoordinatorLayout.LayoutParams layoutParams =  (CoordinatorLayout.LayoutParams) main_content.getLayoutParams();


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                half_expand_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (newState){
                            case BottomSheetBehavior.STATE_COLLAPSED:
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                                break;
                             case BottomSheetBehavior.STATE_HALF_EXPANDED:
                                 bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                 half_expand_button.setVisibility(View.GONE);
                                 break;

                        }
                    }
                });
                if(newState==BottomSheetBehavior.STATE_EXPANDED) {
                    half_expand_button.setVisibility(View.GONE);

                }
                else
                    half_expand_button.setVisibility(View.VISIBLE);
                if(newState==BottomSheetBehavior.STATE_HALF_EXPANDED){
                    layoutParams.dodgeInsetEdges=Gravity.BOTTOM;

                }
                else {
                    layoutParams.dodgeInsetEdges=Gravity.BOTTOM;
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

 /*              if(slideOffset>bottomSheetBehavior.getHalfExpandedRatio())
                {
                 layoutParams.dodgeInsetEdges= Gravity.NO_GRAVITY;
                }
               else{
                   layoutParams.dodgeInsetEdges = Gravity.BOTTOM;
               }
*/
                if(slideOffset==1.0)
              {
                  layoutParams.dodgeInsetEdges = Gravity.NO_GRAVITY;
              }

            }
        });


        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.information);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    linearLayout.setVisibility(View.VISIBLE);

                } else {
                    // The toggle is disabled
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.setRetainInstance(true);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        final LatLng p1 = new LatLng(26.842590, 75.563640);
        final LatLng p2 = new LatLng(26.841198, 75.5647414);
        //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        final CameraPosition cameraPositionP1 = CameraPosition.builder().target(p1).zoom(20).bearing(-20).build();
        final CameraPosition cameraPositionP2 = CameraPosition.builder().target(p2).zoom(20).bearing(-20).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionP1));


// Add an overlay to the map, retaining a handle to the GroundOverlay object.


        btnp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionP1));
            }
        });


        btnp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionP2));

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            requestLocationPermissions();
            // Show rationale and request permission.
        }
        GradientDrawable drawable = (GradientDrawable) getDrawable(R.drawable.rectangle);
        Bitmap bitmap = drawableToBitmap(drawable);
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromBitmap(bitmap)).bearing(-20)
                .position(p1, dptopx((float) 1.8288), dptopx((float) 3.6576)).clickable(true);
        mMap.addGroundOverlay(groundOverlayOptions);

        mMap.setOnGroundOverlayClickListener(new GoogleMap.OnGroundOverlayClickListener() {
            @Override
            public void onGroundOverlayClick(GroundOverlay groundOverlay) {
                LatLng latLng = groundOverlay.getPosition();
                lat = latLng.latitude;
                lon = latLng.longitude;

                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(p1).zoom(18).build()));

                if (ContextCompat.checkSelfPermission(ParkingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            ParkingActivity.this.location = task.getResult();

                            Double lat1 = location.getLatitude();
                            Double lon1 = location.getLongitude();
                            start = new LatLng(lat1, lon1);
                            end = new LatLng(lat, lon);
                         /*  Routing routing = new Routing.Builder().key(api_key)
                            .travelMode(AbstractRouting.TravelMode.DRIVING)
                            .withListener(ParkingActivity.this)
                            .alternativeRoutes(false)
                            .waypoints(start, end)
                            .build();
                    routing.execute();*/


                            Uri baseUri = Uri.parse(Direction_request_url);
                            Uri.Builder builder = baseUri.buildUpon();
                            builder.appendQueryParameter("origin", lat1 + "," + lon1);
                            builder.appendQueryParameter("destination", lat + "," + lon);
                            builder.appendQueryParameter("key", api_key);
                            Direction_request_url = builder.toString();
                            JsonViewModel.doAction(Direction_request_url);

                          /* JsonViewModel model = ViewModelProviders.of(ParkingActivity.this).get(JsonViewModel.class);

                            model.getData().observe(ParkingActivity.this, new Observer<PolylineOptions>() {
                                @Override
                                public void onChanged(PolylineOptions data) {
                                    // update UI
                                 Polyline polyline =   mMap.addPolyline(data);
                              
                                }
                            });*/

                        }
                    });

                }
            }
        });


    }

    private float dptopx(float dp) {

        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }


    private void requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ParkingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ParkingActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestcode);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(ParkingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestcode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ParkingActivity.this.requestcode) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    mMap.setMyLocationEnabled(true);
                    // for Activity#requestPermissions for more details.
                    return;
                }


            } else {
                requestLocationPermissions();
                // Permission was denied. Display an error message.
            }
        }
    }



    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(2000, 2000, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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

        return ContextCompat.getColor(this, magnitudeColorResourceId);
    }


}
