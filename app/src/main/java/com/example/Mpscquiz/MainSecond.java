package com.example.Mpscquiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class MainSecond extends AppCompatActivity {
    Button show2,  login, signup;
    EditText  edit_email2, edit_password2;
    TextView  forget;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase,userref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_second);

        edit_email2= findViewById(R.id.email2);
        edit_password2 = findViewById(R.id.password2);
        show2 = findViewById(R.id.show2);
        show2.setOnClickListener(new showOrHidePassword2());
        forget = findViewById(R.id.forget);
        login = findViewById(R.id.login);
        signup= findViewById(R.id.btn_new_user);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        mDatabase=firebaseDatabase.getReference();
        userref=mDatabase.child("User");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }

        });
        try {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLogInUser();
                }
            });
        } catch (Exception e) {
            Toast.makeText(MainSecond.this, "Warning", Toast.LENGTH_SHORT).show();
        }

    }
    private String getUserEmail() {
        return edit_email2.getText().toString().trim();
    }

    private String getUserPassword() {
        return edit_password2.getText().toString().trim();
    }

    public boolean validate() {
        boolean valid = true;
        String email = edit_email2.getText().toString();
        String password = edit_password2.getText().toString();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email2.setError("enter a valid email address");
            valid = false;
        } else {
            edit_email2.setError(null);
        }

        if (password.isEmpty() || (password.length()<6 || password.matches(pattern))) {
            edit_password2.setError("entered password must contain minimum 6 alphanumeric characters");
            valid = false;
        } else {
            edit_password2.setError(null);
        }

        return valid;
    }

    private void onLogInUser() {
        if (!validate()) {
        }
        else
        {
            logIn(getUserEmail(), getUserPassword());
        }
    }

    private void logIn(String email, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(MainSecond.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    userref.child(Objects.requireNonNull(mAuth.getUid())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user=dataSnapshot.getValue(User.class);
                             score.logicalm=user.getLogicalm();
                             score.englishm=user.getEnglishm();
                             score.historym=user.getHistorym();
                             score.geographym=user.getGeographym();
                             score.agriculturem=user.getAgriculturem();
                             score.politicm=user.getPoliticm();
                             score.human_resm=user.getHuman_resm();
                             score.sciencem=user.getSciencem();
                             score.economicsm=user.getEconomicsm();
                             score.current_affairm=user.getCurrent_affairm();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                          Toast.makeText(getApplicationContext(),databaseError.getCode(),Toast.LENGTH_SHORT).show();
                        }
                });
                    goToMainActivity();
                }else {
                    Toast.makeText(getApplicationContext(), "Login Failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    edit_password2.setText("");
                    edit_password2.requestFocus();
                }
            }
        });
    }

    private void goToMainActivity() {

        Toast.makeText(getApplicationContext(), "Login Successful" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainSecond.this, Navigation_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    //Used to show the help by triggering a toast
    public void showHelp(View view) {

        Toast toast_help = new Toast(getApplicationContext());
        toast_help.setGravity(Gravity.CENTER, 0, 0);
        toast_help.setDuration(Toast.LENGTH_LONG);
        LayoutInflater inflater = getLayoutInflater();
        View appear = inflater.inflate(R.layout.toast_help, (ViewGroup) findViewById(R.id.scrollview));
        toast_help.setView(appear);
        toast_help.show();

    }
    //Used to add some time so that user cannot directly press and exity out of the activity
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 4000);

    }
    //class to show or hide password on button click in main activity second
    class showOrHidePassword2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (show2.getText().toString() == "SHOW") {
                edit_password2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                show2.setText("HIDE");

            } else {

                edit_password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                show2.setText("SHOW");
            }
        }
    }
    public void showDialog(View view) {
       //Forgot password code
        AlertDialog.Builder alertDialog;//Create a dialog object
        alertDialog = new AlertDialog.Builder(MainSecond.this);
        //EditText to show up in the AlertDialog so that the user can enter the email address
        final EditText editTextDialog = new EditText(MainSecond.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextDialog.setLayoutParams(layoutParams);
        editTextDialog.setText(edit_email2.getText().toString());
        editTextDialog.setHint("Email");
        //Adding EditText to Dialog Box
        alertDialog.setView(editTextDialog);
        alertDialog.setTitle("Enter Email");
        alertDialog.setPositiveButton("AGREE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email_dialog = editTextDialog.getText().toString();
                mAuth.sendPasswordResetEmail(email_dialog);
                Toast.makeText(getApplicationContext(),"Check your email for password reset",Toast.LENGTH_LONG).show();
                }
        });
        alertDialog.setNegativeButton("DISAGREE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //When the Disagree button is pressed
                dialog.cancel();
            }
        });

        //Showing up the alert dialog box
        alertDialog.show();
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
