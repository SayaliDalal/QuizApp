package com.example.Mpscquiz;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class activity_profile1 extends AppCompatActivity {
    TextView nav_header_nam,nav_header_emal;
    ImageView nav_header_imag;
    Button logout;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase,userref;

    String nav_header_gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        logout=findViewById(R.id.logout);
        nav_header_nam =   findViewById(R.id.nav_header_name2);
        nav_header_emal =  findViewById(R.id.nav_header_email2);
        nav_header_imag =  findViewById(R.id.nav_header_image2);

        // Fetch the data from realtime database
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase=firebaseDatabase.getReference();
        userref=mDatabase.child("User");
        userref.child(Objects.requireNonNull(mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                nav_header_nam.setText(user.getName());
                nav_header_emal.setText(user.getEmail());
                nav_header_gender=user.getGender();
                if (nav_header_gender.equals("Male")) {
                    nav_header_imag.setImageResource(R.drawable.man);
                } else {
                    nav_header_imag.setImageResource(R.drawable.female);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_profile1.this);
                builder.setTitle("Confirmation Popup!").setMessage("Do you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Logout Successful" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainSecond.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert1=builder.create();
                alert1.show();

            }
        });


    }
}

