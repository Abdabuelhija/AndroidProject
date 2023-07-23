package com.example.finallandroidproject.ui.allwatches;
import static android.content.ContentValues.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finallandroidproject.Cart;
import com.example.finallandroidproject.MainActivity;
import com.example.finallandroidproject.Navbar;
import com.example.finallandroidproject.databinding.FragmentAllWatchesBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import com.example.finallandroidproject.Watch;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finallandroidproject.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AllWatchesFragment extends Fragment {

    private AllWatchesViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAllWatchesBinding binding = FragmentAllWatchesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ArrayList<Watch> watchesList=new ArrayList<Watch>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String UserPhone = sharedPreferences.getString("phone", "DefaultPhone");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://database-f5aea-default-rtdb.firebaseio.com/");
        databaseReference.child("watches").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String Model = dataSnapshot.child("Model").getValue(String.class);
                    Integer price = dataSnapshot.child("Price").getValue(Integer.class);
                    Integer quantity = dataSnapshot.child("quantity").getValue(Integer.class);
                    Integer indexIMG = dataSnapshot.child("indexIMG").getValue(Integer.class);
                    watchesList.add(new Watch(name,Model,price,quantity,indexIMG));
                }
                MyAdapter myadapter = new MyAdapter(watchesList,MainActivity.cart);
                ListView ls = (ListView) root.findViewById(R.id.list_view_watches);
                ls.setAdapter(myadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        MyAdapter myadapter = new MyAdapter(watchesList,MainActivity.cart);
        ListView ls = (ListView) root.findViewById(R.id.list_view_watches);
        ls.setAdapter(myadapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AllWatchesViewModel.class);
        // TODO: Use the ViewModel
    }

    public class MyAdapter extends BaseAdapter {
        ArrayList <Watch> watches=new ArrayList<Watch>();
        ArrayList<Integer> imageList = new ArrayList<>();
        Cart cart;

        MyAdapter(ArrayList<Watch> watches,Cart cart) {
            this.watches = watches;
            this.cart=cart;
            this.imageList = new ArrayList<>();
            imageList.add(R.drawable.rolex1);
            imageList.add(R.drawable.rolex2);
            imageList.add(R.drawable.rolex3);
            imageList.add(R.drawable.armani1);
            imageList.add(R.drawable.armani2);
            imageList.add(R.drawable.armani3);
            imageList.add(R.drawable.tessot1);
            imageList.add(R.drawable.tessot2);
            imageList.add(R.drawable.tessot3);
        }

        @Override
        public int getCount(){
            return watches.size();
        }

        @Override
        public Watch getItem(int position) {
            return watches.get(position);
        }

        @Override
        public long getItemId(int position){
            return  position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater Linflater=getLayoutInflater();
            View view1=Linflater.inflate(R.layout.row_view,null);
            TextView txtName=(TextView) view1.findViewById(R.id.txtName);
            TextView txtModel=(TextView) view1.findViewById(R.id.txtModel);
            TextView txtPrice=(TextView) view1.findViewById(R.id.txtPrice);
            ImageView imageView = (ImageView) view1.findViewById(R.id.imageView);
            imageView.setImageResource(imageList.get(i));
            Button btnAddToCart = (Button) view1.findViewById(R.id.button);
            txtName.setText(watches.get(i).name);
            txtModel.setText("Model: "+watches.get(i).Model);
            txtPrice.setText("Price: "+String.valueOf(watches.get(i).Price)+"$");
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(watches.get(i).quantity>0) {
                        cart.AddWatch(watches.get(i));
                        cart.quantities.add(1);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://database-f5aea-default-rtdb.firebaseio.com/");
                        databaseReference.child("carts").child(cart.userPhone).setValue(cart);
                        DatabaseReference watchesReference = databaseReference.child("watches");
                        watchesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if(snapshot.child("name").getValue(String.class).equals(watches.get(i).name)) {
                                        watchesReference.child(snapshot.getKey()).setValue(watches.get(i));
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle possible errors.
                            }
                        });
                    }
                    else{
                        Log.d(TAG, "onClick: error not in the mlai");
                    }
                }
            });
            return  view1;
        }

    }

}