package store.twinshop.virtualmarket;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import store.twinshop.virtualmarket.Common.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static store.twinshop.virtualmarket.Common.Common.categoryId;

public class CartActivity extends AppCompatActivity {
    ListView cartList;
    DatabaseReference cartItem;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    List<Detail> detailList;

    String uid;

    TextView cartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        /*if(Build.VERSION.SDK_INT>=21)
        {
            Window window;
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        cartItem = database.getReference("Cart_Item");

        detailList = new ArrayList<>();

        cartList = (ListView) findViewById(R.id.cartListId);

        cartCount = findViewById(R.id.cartCountId);

        Button buttonPlaceOrder;
        buttonPlaceOrder = findViewById(R.id.btnPlaceOrder);

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,Confirm.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final int count;
        final int[] countPri = new int[1];

        cartItem.child(uid).child(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        detailList.clear();
                        countPri[0] = 0;

                        for(DataSnapshot detailSnapshot: dataSnapshot.getChildren()){
                            Detail detail = detailSnapshot.getValue(Detail.class);

                            detailList.add(detail);

                            CartDetailList adapter = new CartDetailList(CartActivity.this, detailList);

                            cartList.setAdapter(adapter);

                            int pr = Integer.parseInt(detail.getFoodPrice());
                            countPri[0] = countPri[0] +pr;

                            cartCount.setText("Total: "+String.valueOf(countPri[0])+"tk");
                        }

                        /*cartList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                countPri[0] = 0;

                                Detail detail = detailList.get(position);

                                String foodName = detail.getFoodName();
                                String foodPrice = detail.getFoodPrice();
                                String foodQ = detail.getFoodQuantity();
                                String resN = detail.getRestaurantName();

                                int foodQuan = Integer.parseInt(foodQ);
                                int foodPri = Integer.parseInt(foodPrice);
                                if (foodQuan>0)
                                {
                                    foodPri = foodPri-(foodPri/foodQuan);
                                    foodQuan = foodQuan-1;

                                    if (foodQuan!=0)
                                    {
                                        Detail d = new Detail(foodName, String.valueOf(foodPri), String.valueOf(foodQuan),Common.CategoryIndex);

                                        cartItem.child(String.format("%s",uid)).child(String.format("%s",Common.categoryId)).child(String.format("%s",foodName)).setValue(d);
                                    }
                                    else
                                    {
                                        cartItem.child(uid).child(categoryId).child(detail.foodName).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                    dataSnapshot.getRef().removeValue();

                                                    cartItem.child(uid).child(categoryId).addValueEventListener(new ValueEventListener() {

                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(dataSnapshot.getValue()==null)
                                                            {
                                                                finish();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                }
                                else if(foodQuan<=0)
                                {
                                    cartItem.child(uid).child(categoryId).child(detail.foodName).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                appleSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                view.setSelected(true);
                            }
                        });*/


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
