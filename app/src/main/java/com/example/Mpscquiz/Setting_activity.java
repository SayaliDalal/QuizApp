package com.example.Mpscquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Setting_activity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase,userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        mAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        mDatabase=firebaseDatabase.getReference();
        userref=mDatabase.child("User");

        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userref.child(mAuth.getUid()).child("logicalm").setValue(0);
                userref.child(mAuth.getUid()).child("englishm").setValue(0);
                userref.child(mAuth.getUid()).child("historym").setValue(0);
                userref.child(mAuth.getUid()).child("geographym").setValue(0);
                userref.child(mAuth.getUid()).child("agriculturem").setValue(0);
                userref.child(mAuth.getUid()).child("politicm").setValue(0);
                userref.child(mAuth.getUid()).child("human_resm").setValue(0);
                userref.child(mAuth.getUid()).child("sciencem").setValue(0);
                userref.child(mAuth.getUid()).child("economicsm").setValue(0);
                userref.child(mAuth.getUid()).child("current_affairm").setValue(0);

                Snackbar.make(v,"Highscore Reseted Successfully",Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}

