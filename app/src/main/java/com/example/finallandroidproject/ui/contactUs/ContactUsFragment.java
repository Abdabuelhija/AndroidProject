package com.example.finallandroidproject.ui.contactUs;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finallandroidproject.Message;
import com.example.finallandroidproject.R;
import com.example.finallandroidproject.databinding.FragmentContactUsBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactUsFragment extends Fragment {

    private ContactUsViewModel mViewModel;
    private EditText editTextMessage;
    private Button buttonSend;
    private String userPhone;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://database-f5aea-default-rtdb.firebaseio.com/");

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_contact_us,container,false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        userPhone = sharedPreferences.getString("phone", "DefaultPhone");

        editTextMessage = root.findViewById(R.id.editTextMessage);
        buttonSend = root.findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);
        // TODO: Use the ViewModel
    }

    private void sendMessage() {
        String message = editTextMessage.getText().toString();

        if(!message.isEmpty()) {
            Message msg = new Message(userPhone, message);
            databaseReference.child("messages").child(userPhone).setValue(msg);

            editTextMessage.setText("");
            Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Please write a message", Toast.LENGTH_SHORT).show();
        }
    }

}
