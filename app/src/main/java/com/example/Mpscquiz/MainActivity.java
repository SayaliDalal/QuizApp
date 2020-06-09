package com.example.Mpscquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    //variables
    Button rshow, show, getStarted, getStarted2, help;
    EditText edit_password, redit_password,edit_name, edit_email;
    TextView toast;
    String[] Gender = {"Male", "Female"};
    String gender;
    Spinner spinner;

    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        score.logicalm=0;
        score.englishm=0;
        score.historym=0;
        score.geographym=0;
        score.politicm=0;
        score.agriculturem=0;
        score.human_resm=0;
        score.sciencem=0;
        score.economicsm=0;
        score.current_affairm=0;

            help = findViewById(R.id.help);
            show = findViewById(R.id.show);  //Show button in password
            edit_password = findViewById(R.id.password);   //Password EditText
            rshow = findViewById(R.id.rshow);  //Show button in password
            redit_password = findViewById(R.id.rpassword);   //Password EditText
            edit_email = findViewById(R.id.email);   //email EditText
            edit_name = findViewById(R.id.name);   //name EditText

            mAuth = FirebaseAuth.getInstance();
            mDatabase=FirebaseDatabase.getInstance().getReference("User");

            show.setOnClickListener(new showOrHidePassword());//invoking the showOrHidePassword class to show the password
            rshow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rshow.getText().toString() == "SHOW") {
                        redit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        rshow.setText("HIDE");

                    } else {

                        redit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        rshow.setText("SHOW");
                    }
                }
            });
            toast =  findViewById(R.id.toast_help);//toast_help object
            //Spinner for choosing the gender
            spinner = findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner, Gender);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new spinner());
            getStarted = findViewById(R.id.getStarted);
            getStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRegisterUser();
                }
            });

            getStarted2 = findViewById(R.id.getStarted2);
            getStarted2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MainSecond.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            });

        }

    public boolean validate() {
        boolean valid = true;

        String save_name = edit_name.getText().toString();
        String save_email = edit_email.getText().toString();
        String save_password = edit_password.getText().toString();
        String reEnterPassword = redit_password.getText().toString();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        if (save_name.isEmpty()) {
            edit_name.setError("enter user name");
            valid = false;
        } else {
            edit_name.setError(null);
        }
        if (save_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(save_email).matches()) {
            edit_email.setError("enter a valid email address");
            valid = false;
        } else {
            edit_email.setError(null);
        }
        if (save_password.isEmpty() || (save_password.length() < 6|| save_password.matches(pattern))) {
            edit_password.setError(" entered password must contain 6 alphanumeric characters minimum");
            valid = false;
        } else {
            edit_password.setError(null);
        }
        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(save_password))) {
                redit_password.setError("Enter correct password");
                valid = false;
            }
        else{
            redit_password.setError(null);
        }
        return valid;
    }
    private void onRegisterUser() {
        String Name = edit_name.getText().toString();
        String Email = edit_name.getText().toString();
        Log.d("Name",""+Name);
        Log.d("Email",""+Email);
        if (!validate()) {
        }
        else {
            signUp(edit_email.getText().toString().trim(), edit_password.getText().toString().trim());
        }
    }


    private void signUp( String email, String password) {
        final String Name = edit_name.getText().toString();
        final String Email = edit_email.getText().toString();
        final int logicalm = score.logicalm;
        final int englishm = score.englishm;
        final int historym = score.historym;
        final int geographym = score.geographym;
        final int agriculturem = score.agriculturem;
        final int politicm = score.politicm;
        final int human_resm = score.human_resm;
        final int sciencem = score.sciencem;
        final int economicsm = score.economicsm;
        final int current_affairm = score.current_affairm;
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    User user = new User(
                            Name, Email, gender, logicalm, englishm,historym,geographym,agriculturem,
                            politicm,human_resm,sciencem,economicsm,current_affairm
                    );
                    mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, Navigation_Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Already you have an account", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),""+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
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


    //class to show or hide password on button click in main activity
    class showOrHidePassword implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (show.getText().toString() == "SHOW" ) {
                edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                show.setText("HIDE");

            } else {

                edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                show.setText("SHOW");
            }
        }
    }

    //Spinner class to select spinner and also add gender
    class spinner implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            gender = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //When nothing is selected
            Toast.makeText(getApplicationContext(), "Please Enter the gender", Toast.LENGTH_SHORT).show();
        }
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
