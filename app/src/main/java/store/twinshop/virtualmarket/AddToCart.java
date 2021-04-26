package store.twinshop.virtualmarket;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import store.twinshop.virtualmarket.Common.Common;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static store.twinshop.virtualmarket.Common.Common.categoryId;

public class AddToCart extends AppCompatActivity {
    TextView foodItem, itemPrice, foodDes;
    ImageView itemImage;
    String foodName, foodImage, foodPrice, foodDescription, wsRate;
    String q="1";
    int foodPri,wsPri;

    DatabaseReference cartItem, orderedItem;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String uid;
    int lastCo;

    Button btnCart;

    ElegantNumberButton Quantitybutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        /*if(Build.VERSION.SDK_INT>=21)
        {
            Window window;
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryAnother));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        cartItem = database.getReference("Cart_Item");

        orderedItem = database.getReference("Ordered_Item");

        final ArrayList<String> arrPackage;
        arrPackage = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        foodName = bundle.getString("foodName");
        foodImage = bundle.getString("foodImage");
        foodPrice = bundle.getString("foodPrice");
        foodDescription = bundle.getString("foodDescription");
        wsRate = bundle.getString("wsRate");

        foodPri = Integer.parseInt(foodPrice);
        wsPri = Integer.parseInt(wsRate);

        foodItem = findViewById(R.id.foodTextId);
        itemImage = findViewById(R.id.foodImageId);
        itemPrice = findViewById(R.id.itemPriceId);
        foodDes = findViewById(R.id.food_description);
        btnCart = findViewById(R.id.btnCart);

        foodDes.setText(foodDescription);

        Picasso.with(AddToCart.this)
                .load(foodImage)
                .into(itemImage);

        foodItem.setText(foodName);
        itemPrice.setText(foodPrice);

        Quantitybutton = findViewById(R.id.itemQuantityId);

        Quantitybutton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                q = Quantitybutton.getNumber();
                //Toast.makeText(AddToCart.this,foodPri, Toast.LENGTH_SHORT).show();
                QuantityButtonTask();
            }
        });

        orderedItem.child(uid).child(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    for(DataSnapshot detailSnapshot: dataSnapshot.getChildren())
                    {
                        OrderedDetails det = detailSnapshot.getValue(OrderedDetails.class);
                        if(String.valueOf(det.getMobileNumber()).equals("rec"))
                        {
                            btnCart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(AddToCart.this, "Your order is recieved", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            btnCart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Detail detail = new Detail(foodName, String.valueOf(foodPri),q,Common.CategoryIndex,String.valueOf(wsPri));

                                    cartItem.child(String.format("%s",uid)).child(String.format("%s",Common.categoryId)).child(String.format("%s",foodName)).setValue(detail);

                                    finish();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                }
                            });
                        }
                    }
                }
                else
                {
                    btnCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Detail detail = new Detail(foodName, String.valueOf(foodPri),q,Common.CategoryIndex,String.valueOf(wsPri));

                            cartItem.child(String.format("%s",uid)).child(String.format("%s",Common.categoryId)).child(String.format("%s",foodName)).setValue(detail);

                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void QuantityButtonTask() {
        foodPri = Integer.parseInt(q)* Integer.parseInt(foodPrice);
        wsPri = Integer.parseInt(q)* Integer.parseInt(wsRate);
        itemPrice.setText(String.valueOf(foodPri));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
