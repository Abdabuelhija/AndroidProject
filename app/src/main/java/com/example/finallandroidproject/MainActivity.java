package com.example.finallandroidproject;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://database-f5aea-default-rtdb.firebaseio.com/");
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    public static Cart cart;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.LoginBtn);
        registerButton=findViewById(R.id.RegisterBtn);

        sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    phoneEditText.setError("Phone number is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Password is required");
                    return;
                }
                else
                {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phone)){
                                String getpassword=snapshot.child(phone).child("password").getValue(String.class);
                                if(getpassword.equals(password)){
                                    String userName=snapshot.child(phone).child("name").getValue(String.class);
                                    editor.putString("username", userName);
                                    editor.putString("phone", phone);
                                    editor.commit();
                                    Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this,Navbar.class));
                                }
                                else
                                    Toast.makeText(MainActivity.this,"Wrong password",Toast.LENGTH_LONG).show();

                            }
                            else {
                                Toast.makeText(MainActivity.this,"Wrong phone",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String UserPhone = sharedPreferences.getString("phone", "DefaultPhone");
        databaseReference.child("carts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(UserPhone)){
                    cart = snapshot.child(UserPhone).getValue(Cart.class);
                }
                else {
                    cart = new Cart(UserPhone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
