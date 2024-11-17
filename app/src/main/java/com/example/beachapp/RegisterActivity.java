package com.example.beachapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.beachapp.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String in_username;
    private String in_email;
    private String in_password;

    private Button buttom;
    private EditText edtUsername;
    private EditText edtEmail;
    private EditText edtPassword;
    public boolean validateCredentials(String email, String password) {
        return email != null && email.contains("@") && password != null && password.length() >= 6;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        edtUsername = findViewById(R.id.editTextUsername);
        edtEmail    = findViewById(R.id.editTextEmail);
        edtPassword = findViewById(R.id.editTextPassword);
        buttom = findViewById(R.id.buttonRegister);
        buttom.setOnClickListener(view -> registerUser());
    }
    private void registerUser() {
        in_username = edtUsername.getText().toString().trim();
        in_email    = edtEmail.getText().toString().trim();
        in_password = edtPassword.getText().toString().trim();
        if(in_username.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(in_email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(in_password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validateCredentials(in_email, in_password)){
            Toast.makeText(getApplicationContext(), "please enter correct email format and password length > 6", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            String userID = databaseReference.push().getKey();
            User user = new User(userID, in_username, in_email, in_password);
            addDatatoFirebase(user);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private void addDatatoFirebase(User user) {
        databaseReference.child(user.getUserID()).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to Register: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
