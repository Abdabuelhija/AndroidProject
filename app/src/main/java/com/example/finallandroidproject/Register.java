package com.example.finallandroidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://database-f5aea-default-rtdb.firebaseio.com/");

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        emailEditText=findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.RegisterBtn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString().trim();
                final String phone = phoneEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    nameEditText.setError("name is required");
                    return;
                }

                else if (TextUtils.isEmpty(phone)) {
                    phoneEditText.setError("Phone number is required");
                    return;
                }
                else if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("email is required");
                    return;
                }
                else if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is required");
                    return;
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phone)){
                                Toast.makeText(Register.this,"Phone number already registered",Toast.LENGTH_LONG).show();
                            }
                            else {
                                User user = new User(name,email, password);
                                databaseReference.child("users").child(phone).setValue(user);
                                Toast.makeText(Register.this,"Registration successful",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Register.this,"Database error",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}