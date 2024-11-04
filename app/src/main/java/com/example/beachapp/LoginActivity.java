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

public class LoginActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String in_username;
    private String in_email;
    private String in_password;

    private Button login_buttom;
    private Button registerJump_button;
    private EditText edtUsername;
    private EditText edtEmail;
    private EditText edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        edtEmail    = findViewById(R.id.editTextEmail);
        edtPassword = findViewById(R.id.editTextPassword);
        login_buttom = findViewById(R.id.buttonLogin);
        login_buttom.setOnClickListener(view -> loginUser());
        registerJump_button=findViewById(R.id.buttonRegisterJump);
        registerJump_button.setOnClickListener(view->registerJump());
    }
    private void loginUser() {
         in_email    = edtEmail.getText().toString().trim();
         in_password = edtPassword.getText().toString().trim();
        if(in_email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(in_password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.orderByChild("email").equalTo(in_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot curSnapShot : snapshot.getChildren()){
                        User curUser=curSnapShot.getValue(User.class);
                        if(curUser!=null){
                            if(curUser.getPassword().equals(in_password)){
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", curUser.getUserID());
                                startActivity(intent);
                                finish();
                                return;
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Email not registered,email received"+ in_email, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        }
        public void registerJump(){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
}
