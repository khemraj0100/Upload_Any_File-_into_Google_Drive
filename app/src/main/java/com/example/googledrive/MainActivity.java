package com.example.googledrive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    DriveServiceHelper driveServiceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestSignIn();

    }

    private void requestSignIn() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
        startActivityForResult(googleSignInClient.getSignInIntent(), 400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 400:
                if (resultCode == RESULT_OK) {
                    handleSignInIntent(data);
                }
                break;
        }
    }

    private void handleSignInIntent(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {

                        GoogleAccountCredential googleAccountCredential = GoogleAccountCredential
                                .usingOAuth2(MainActivity.this, Collections.singleton(DriveScopes.DRIVE_FILE));

                        googleAccountCredential.setSelectedAccount(googleSignInAccount.getAccount());
                        Drive googleDriveService = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                googleAccountCredential)
                                .setApplicationName("My Drive")
                                .build();

                        driveServiceHelper =new DriveServiceHelper(googleDriveService);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void UpdatePdfFile(View view){

        ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Upload to Google Drive..");
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();
//        String filePath="Phone Storage/Music/EngineeringPhysics.pdf";


        String filePath="/storage/emulated/0/Music/e.pdf";

        Log.d("dsfdews","frg===="+filePath);

        driveServiceHelper.createFilePDF(filePath).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("dsfdews","frg===="+filePath);

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Uploaded SuccessFull ",Toast.LENGTH_LONG).show();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("dsfdews","frg===="+filePath);

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"check you google drive api ",Toast.LENGTH_LONG).show();


                    }
                });


    }
}