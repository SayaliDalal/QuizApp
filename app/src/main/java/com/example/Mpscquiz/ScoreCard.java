package com.example.Mpscquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ScoreCard extends AppCompatActivity {
    TextView a1, a2, a3, a4, a5, a6, a7, a8, a9, a10;
    public FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase,userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_card);
        a1 =  findViewById(R.id.marathi);
        a2 =  findViewById(R.id.english);
        a3 =  findViewById(R.id.history);
        a4 =  findViewById(R.id.geography);
        a5 =  findViewById(R.id.agriculture);
        a6 =  findViewById(R.id.politics);
        a7 =  findViewById(R.id.human_res);
        a8 =  findViewById(R.id.science);
        a9 =  findViewById(R.id.economy);
        a10 = findViewById(R.id.current_affair);

        mAuth= FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        mDatabase=firebaseDatabase.getReference();
        userref=mDatabase.child("User");
        userref.child(Objects.requireNonNull(mAuth.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if(user.getLogicalm() < score.logicalm)
                    userref.child(mAuth.getUid()).child("logicalm").setValue(score.logicalm);
                else if (user.getEnglishm() < score.englishm)
                    userref.child(mAuth.getUid()).child("englishm").setValue(score.englishm);
                else if (user.getHistorym() < score.historym)
                    userref.child(mAuth.getUid()).child("historym").setValue(score.historym);
                else if(user.getGeographym() < score.geographym)
                    userref.child(mAuth.getUid()).child("geographym").setValue(score.geographym);
                else if (user.getAgriculturem() < score.agriculturem)
                    userref.child(mAuth.getUid()).child("agriculturem").setValue(score.agriculturem);
                else if (user.getPoliticm() < score.politicm)
                    userref.child(mAuth.getUid()).child("politicm").setValue(score.politicm);
                else if (user.getHuman_resm() < score.human_resm)
                    userref.child(mAuth.getUid()).child("human_resm").setValue(score.human_resm);
                else if (user.getSciencem() < score.sciencem)
                    userref.child(mAuth.getUid()).child("sciencem").setValue(score.sciencem);
                else if (user.getEconomicsm() < score.economicsm)
                    userref.child(mAuth.getUid()).child("economicsm").setValue(score.economicsm);
                else if (user.getCurrent_affairm() < score.current_affairm)
                    userref.child(mAuth.getUid()).child("current_affairm").setValue(score.current_affairm);

                a1.setText( String.valueOf(user.getLogicalm()));
                a2.setText( String.valueOf(user.getEnglishm()));
                a3.setText( String.valueOf(user.getHistorym()));
                a4.setText( String.valueOf(user.getGeographym()));
                a5.setText( String.valueOf(user.getAgriculturem()));
                a6.setText( String.valueOf(user.getPoliticm()));
                a7.setText( String.valueOf(user.getHuman_resm()));
                a8.setText( String.valueOf(user.getSciencem()));
                a9.setText( String.valueOf(user.getEconomicsm()));
                a10.setText(String.valueOf( user.getCurrent_affairm()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
