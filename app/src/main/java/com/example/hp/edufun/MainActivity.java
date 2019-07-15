package com.example.hp.edufun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    List<AuthUI.IdpConfig> providers;
    Button btnP1,btnP2;

    private static final int RC_SIGN_IN = 123;
    FloatingActionButton signout;

    final String User  ="USER";



    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        signout = (FloatingActionButton) findViewById(R.id.signout);
        btnP1 = (Button) findViewById(R.id.btnP1);
        btnP2 = (Button)findViewById(R.id.btnP2);
        Paper.init(this);
        FirebaseUser user = Paper.book().read(User);

            //init providers

            providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),

                    new AuthUI.IdpConfig.GoogleBuilder().build());
        if(user!=null) {
            updateui();
        }
        else {
            showSignInOptions();
        }
     signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                signout.setEnabled(false);
                                showSignInOptions();
                                Paper.book().delete(User);
                            }
                        });

            }
        });



    }

    /*  public void savequote(View view)
       {
           String quote = e1.getText().toString();
           String author = e2.getText().toString();

           Map<String,Object> dataTosave = new HashMap<String, Object>();
           dataTosave.put("quote",quote);
           dataTosave.put("author",author);
           mdoc.set(dataTosave);

       }
       */
    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers).setLogo(R.drawable.logo)
                        .setTheme(R.style.Mytheme)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
               FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();
                Paper.book().write(User,user);


                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                // update  ui...
                updateui();

            }
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }
     public void updateui()
    {
        ConstraintLayout relativeLayout = (ConstraintLayout) findViewById(R.id.constraint);
        relativeLayout.setVisibility(View.VISIBLE);
        btnP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ParkingActivity.class);
                startActivity(intent);
            }
        });
    }
}

