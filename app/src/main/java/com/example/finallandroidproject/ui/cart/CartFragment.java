package com.example.finallandroidproject.ui.cart;
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
import com.example.finallandroidproject.Watch;
import com.example.finallandroidproject.databinding.FragmentCartBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finallandroidproject.R;
import com.example.finallandroidproject.ui.allwatches.AllWatchesFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private CartViewModel mViewModel;
    private TextView TotalPrice;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCartBinding binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TotalPrice = root.findViewById(R.id.TotalPrice);
        ArrayList<Watch> watchesList=new ArrayList<Watch>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String UserPhone = sharedPreferences.getString("phone", "DefaultPhone");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://database-f5aea-default-rtdb.firebaseio.com/");
        CartFragment.CartAdapter cartadapter = new CartFragment.CartAdapter(MainActivity.cart.watches,MainActivity.cart);
        ListView ls = (ListView) root.findViewById(R.id.list_view_cart_watches);
        ls.setAdapter(cartadapter);
        Button payBtn = root.findViewById(R.id.payBtn);
        TextView TotalPrice =root.findViewById(R.id.TotalPrice);
        TotalPrice.setText(String.valueOf("Total Price : "+MainActivity.cart.price)+"$");
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cart.Empty();
                cartadapter.notifyDataSetChanged();
                databaseReference.child("carts").child(MainActivity.cart.userPhone).removeValue();
            }
        });
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        // TODO: Use the ViewModel
    }

    class CartAdapter extends BaseAdapter {
        ArrayList <Watch> watches=new ArrayList<Watch>();
        Cart cart;
        ArrayList<Integer> imageList = new ArrayList<>();
        CartAdapter(ArrayList<Watch> watches,Cart cart) {
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
            View view1=Linflater.inflate(R.layout.cart_row_view,null);
            TextView txtName=(TextView) view1.findViewById(R.id.txtName);
            TextView txtModel=(TextView) view1.findViewById(R.id.txtModel);
            TextView txtPrice=(TextView) view1.findViewById(R.id.txtPrice);
            TextView txtQuantity=(TextView) view1.findViewById(R.id.txtQuantity);
            ImageView imageView = (ImageView) view1.findViewById(R.id.imageView);
            imageView.setImageResource(imageList.get(watches.get(i).indexIMG));
            Button Save = (Button) view1.findViewById(R.id.Save);
            Button menus = (Button) view1.findViewById(R.id.menus);
            Button plus = (Button) view1.findViewById(R.id.plus);
            txtName.setText(watches.get(i).name);
            txtModel.setText("Model: "+watches.get(i).Model);
            txtPrice.setText("Price: "+String.valueOf(watches.get(i).Price)+"$");
            txtQuantity.setText(String.valueOf(cart.quantities.get(i)));
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://database-f5aea-default-rtdb.firebaseio.com/");
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (watches.get(i).quantity >0) {
                        cart.quantities.set(i, cart.quantities.get(i) + 1);
                        txtQuantity.setText(String.valueOf(cart.quantities.get(i)));
                        watches.get(i).quantity--;
                        cart.price+=watches.get(i).Price;
                        notifyDataSetChanged();
                    }
                }
            });
            menus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cart.quantities.get(i) > 0) {
                        cart.quantities.set(i, cart.quantities.get(i) - 1);
                        txtQuantity.setText(String.valueOf(cart.quantities.get(i)));
                        watches.get(i).quantity++;
                        cart.price-=watches.get(i).Price;
                        notifyDataSetChanged();
                    }
                }
            });

            Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://database-f5aea-default-rtdb.firebaseio.com/");
                    Watch currentWatch = watches.get(i);
                    if (cart.quantities.get(i) <= 0) {
                        cart.DeleteWatch(currentWatch);
                        cart.quantities.remove(i);
                    }
                    databaseReference.child("carts").child(cart.userPhone).setValue(cart);
                    DatabaseReference watchesReference = databaseReference.child("watches");
                    watchesReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if(snapshot.child("name").getValue(String.class).equals(currentWatch.name)) {
                                    watchesReference.child(snapshot.getKey()).setValue(currentWatch);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                    TotalPrice.setText("Total Price : "+String.valueOf(MainActivity.cart.price)+"$");
                    notifyDataSetChanged();
                }
            });



            return  view1;
        }

    }
}