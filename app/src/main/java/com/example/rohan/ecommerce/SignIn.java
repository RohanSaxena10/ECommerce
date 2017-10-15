package com.example.rohan.ecommerce;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    private TextView mCreateAccount,mForgotPass,skip;
    private EditText mEmailField,mPassField;
    private Button mSignInButton;
    private FirebaseAuth mAuth;
    private String email,password;
    private static String TAG="SignIn";
    private SignInButton Gsignin;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabaseUser;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("Users");
        context = this;

        Gsignin =(SignInButton)findViewById(R.id.gSignin);
        mCreateAccount =(TextView)findViewById(R.id.createAccText);
        mForgotPass =(TextView)findViewById(R.id.forgotPass);

        mEmailField =(EditText)findViewById(R.id.emailField);
        mPassField =(EditText)findViewById(R.id.passField);
        mSignInButton =(Button)findViewById(R.id.signinButton);
        skip =(TextView)findViewById(R.id.tvskip);
        Gsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GooglesignIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePassword();
            }
        });
        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,CreateAccount.class));
            }
        });

       skip.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context,MainActivity.class);
               startActivity(intent);
           }
       });


        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmailField.getText().toString().trim();
                password = mPassField.getText().toString().trim();
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    if (TextUtils.isEmpty(email)){
                        mEmailField.setError("Enter Email");
                    }
                    if(TextUtils.isEmpty(password)){
                        mPassField.setError("Enter Password");
                    }

                }
                else{startSignIn();}

            }
        });


    }

    private void GooglesignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Toast.makeText(SignIn.this, "Success", Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);


            } else {
                Toast.makeText(SignIn.this, "Failed", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SignIn.this,MainActivity.class));


                            Log.d(TAG, "signInWithCredential:success");


                        } else {

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            ;
                        }


                    }
                });
    }

    private void ChangePassword() {
        email = mEmailField.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            mEmailField.setError("Enter Email");
            Toast.makeText(SignIn.this, "Enter Email",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            mEmailField.setError("Enter Email");
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignIn.this, "password reset email sent ",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }
    }

    private void startSignIn() {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(SignIn.this, "Success",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignIn.this,MainActivity.class));





                        } else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            startActivity(new Intent(SignIn.this,MainActivity.class));
        }
        super.onStart();
    }
}
