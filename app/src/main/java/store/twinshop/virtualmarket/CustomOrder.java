package store.twinshop.virtualmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import store.twinshop.virtualmarket.Common.Common;

public class CustomOrder extends AppCompatActivity {
    EditText homeDelivery;
    String product;

    Button addItemButtom, orderNow;
    DatabaseReference cartItem, orderedItem;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String uid;
    int orderCount=0;

    TextView empty;

    ListView cartList;
    List<Detail> detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_order);

        detailList = new ArrayList<>();
        cartList = findViewById(R.id.listId);

        orderNow = findViewById(R.id.orderId);
        empty = findViewById(R.id.emptyId);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        cartItem = database.getReference("Cart_Item");

        homeDelivery = findViewById(R.id.homeDeliveryId);

        addItemButtom = findViewById(R.id.addItemButtomId);

        cartItem.child(String.format("%s",uid)).child(String.format("%s", "1")).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {
                        orderCount = Integer.parseInt(String.valueOf(dataSnapshot1.getKey()));
                    }

                    addItemButtom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            product = homeDelivery.getText().toString().trim();
                            if(!product.isEmpty())
                            {
                                orderCount++;

                                Detail detail = new Detail(product, String.valueOf("0"),String.valueOf("0"),String.valueOf("Custom Order"),String.valueOf("0"));

                                cartItem.child(String.format("%s",uid)).child(String.format("%s","1")).child(String.format("%s",orderCount)).setValue(detail);

                                homeDelivery.getText().clear();
                            }
                            else
                            {
                                homeDelivery.setError("Invalid Format");
                                homeDelivery.requestFocus();
                            }
                        }
                    });
                }
                else
                {
                    addItemButtom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            product = homeDelivery.getText().toString().trim();

                            if(!product.isEmpty())
                            {
                                Detail detail = new Detail(product, String.valueOf("0"),String.valueOf("0"),String.valueOf("Custom Order"),String.valueOf("0"));

                                cartItem.child(String.format("%s",uid)).child(String.format("%s","1")).child(String.format("%s",orderCount)).setValue(detail);

                                homeDelivery.getText().clear();
                            }
                            else
                            {
                                homeDelivery.setError("Invalid Format");
                                homeDelivery.requestFocus();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomOrder.this,CustomConfirm.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        cartItem.child(uid).child("1")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        detailList.clear();

                        if(dataSnapshot.getValue()!=null)
                        {
                            orderNow.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);

                            for(DataSnapshot detailSnapshot: dataSnapshot.getChildren()){
                                Detail detail = detailSnapshot.getValue(Detail.class);

                                detailList.add(detail);

                                CustomCartDetailList adapter = new CustomCartDetailList(CustomOrder.this, detailList);

                                cartList.setAdapter(adapter);
                            }
                        }
                        else
                        {
                            orderNow.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
