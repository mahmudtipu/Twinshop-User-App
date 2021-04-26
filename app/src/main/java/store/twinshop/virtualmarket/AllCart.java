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

public class AllCart extends AppCompatActivity {
    ListView cartList;
    DatabaseReference cartItem,shopName;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    List<Detail> detailList;

    String uid;
    int c,count;

    TextView cartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cart);

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
        shopName = database.getReference("Shop_Name");

        detailList = new ArrayList<>();

        cartList = (ListView) findViewById(R.id.cartListId);

        cartCount = findViewById(R.id.cartCountId);

        Button buttonPlaceOrder;
        buttonPlaceOrder = findViewById(R.id.btnPlaceOrder);

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCart.this,AllConfirm.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final int[] countPri = new int[1];
        detailList.clear();

        shopName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot det: dataSnapshot.getChildren())
                {
                    c = Integer.parseInt(det.getKey());
                }

                for(int i=0;i<=c;i++)
                {
                    if(i>1)
                    {
                        cartItem.child(uid).child(String.valueOf(i))
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue()!=null)
                                        {
                                            for(DataSnapshot detailSnapshot: dataSnapshot.getChildren()){
                                                Detail detail = detailSnapshot.getValue(Detail.class);

                                                detailList.add(detail);

                                                AllCartDetailList adapter = new AllCartDetailList(AllCart.this, detailList);

                                                cartList.setAdapter(adapter);

                                                int pr = Integer.parseInt(detail.getFoodPrice());
                                                countPri[0] = countPri[0] +pr;
                                            }

                                            count = countPri[0]+30;
                                            cartCount.setText("Total: "+String.valueOf(countPri[0])+"tk");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                }
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
