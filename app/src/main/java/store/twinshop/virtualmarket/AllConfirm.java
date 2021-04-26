package store.twinshop.virtualmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import static store.twinshop.virtualmarket.Common.Common.categoryId;

public class AllConfirm extends AppCompatActivity {
    EditText firstName, lastName, email, location;
    TextView mobileNumber,totalId;
    Button confirmOrder;

    DatabaseReference cartItem,profile,orderedItem,shopName;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    List<Detail> detailList;

    String uid,Fname,Lname;

    String firstN, lastN, emailA, mobileN, locationA;
    int c,count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_confirm);

        /*if(Build.VERSION.SDK_INT>=21)
        {
            Window window;
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/

        detailList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        cartItem = database.getReference("Cart_Item");
        profile = database.getReference("Profile");
        orderedItem = database.getReference("Ordered_Item");
        shopName = database.getReference("Shop_Name");

        firstName = findViewById(R.id.firstNameId);
        lastName = findViewById(R.id.lastNameId);
        email = findViewById(R.id.emailId);
        mobileNumber = findViewById(R.id.modileNumberId);
        location = findViewById(R.id.locationId);
        totalId = findViewById(R.id.totalId);

        confirmOrder = findViewById(R.id.confirmOrderId);

        mobileNumber.setText(Common.mobileNumber);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstN = firstName.getText().toString().trim();
                lastN = lastName.getText().toString().trim();
                emailA = email.getText().toString().trim();
                locationA = location.getText().toString().trim();
                mobileN = mobileNumber.getText().toString().trim();

                if(emailA.isEmpty())
                {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }

                if (lastN.isEmpty())
                {
                    lastName.setError("Last Name is required");
                    lastName.requestFocus();
                }

                if(firstN.isEmpty())
                {
                    firstName.setError("First Name is required");
                    firstName.requestFocus();
                }

                if(locationA.isEmpty())
                {
                    location.setError("Location is required");
                    location.requestFocus();
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailA).matches())
                {
                    email.setError("Valid email is required");
                    email.requestFocus();
                    return;
                }

                if(!emailA.isEmpty()&&!lastN.isEmpty()&&!firstN.isEmpty()&&!locationA.isEmpty()&&!mobileN.isEmpty())
                {
                    profile.child(String.format("%s",uid)).child("first_name").setValue(firstN);
                    profile.child(String.format("%s",uid)).child("last_name").setValue(lastN);
                    profile.child(String.format("%s",uid)).child("email").setValue(emailA);
                    profile.child(String.format("%s",uid)).child("mobile_number").setValue(mobileN);
                    profile.child(String.format("%s",uid)).child("location").setValue(locationA);
                    profile.child(String.format("%s",uid)).child("ind").setValue(uid);

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
                                    final int finalI = i;
                                    cartItem.child(uid).child(String.valueOf(i))
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    detailList.clear();

                                                    for(DataSnapshot detailSnapshot: dataSnapshot.getChildren()){
                                                        Detail detail = detailSnapshot.getValue(Detail.class);

                                                        detailList.add(detail);

                                                        CartDetailList adapter = new CartDetailList(AllConfirm.this, detailList);

                                                        //cartList.setAdapter(adapter);

                                                        final String foodNames = detail.getFoodName();
                                                        final String foodPrices = detail.getFoodPrice();
                                                        final String foodQuantities = detail.getFoodQuantity();
                                                        final String locate = detail.getRestaurantName();
                                                        final String wsRates = detail.getWsRate();

                                                        OrderedDetails orderedDetail = new OrderedDetails(foodNames,firstN+" "+lastN,foodQuantities,foodPrices,mobileN,locate,wsRates);

                                                        orderedItem.child(uid).child(String.valueOf(finalI)).child(foodNames).setValue(orderedDetail);

                                                        detailSnapshot.getRef().removeValue();

                                                        Intent intent = new Intent(AllConfirm.this,Done.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                        finish();

                                                        //senEmail();
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
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        profile.child(uid).child("first_name").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    Fname = dataSnapshot.getValue(String.class);

                    firstName.setText(Fname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.child(uid).child("last_name").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    Lname = dataSnapshot.getValue(String.class);

                    lastName.setText(Lname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.child(uid).child("email").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    String emailAdd = dataSnapshot.getValue(String.class);

                    email.setText(emailAdd);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.child(uid).child("mobile_number").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    String m = dataSnapshot.getValue(String.class);

                    mobileNumber.setText(m);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.child(uid).child("location").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    String l = dataSnapshot.getValue(String.class);

                    location.setText(l);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        int count;
        final int[] countPri = new int[1];

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
                        cartItem.child(String.format("%s",uid)).child(String.format("%s",String.valueOf(i))).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null)
                                {

                                    //cartCount.setText(String.valueOf(dataSnapshot.getChildrenCount()));

                                    for(DataSnapshot detailSnapshot: dataSnapshot.getChildren()){
                                        Detail detail = detailSnapshot.getValue(Detail.class);

                                        //CartDetailList adapter = new CartDetailList(Start.this, detailLi);

                                        //cartList.setAdapter(adapter);

                                        //String price = detail.getFoodPrice();

                                        int pr = Integer.parseInt(detail.getFoodPrice());
                                        countPri[0] = countPri[0] +pr;

                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                orderedItem.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null)
                        {
                            totalId.setText(String.valueOf("Total : "+countPri[0])+"tk");
                        }
                        else
                        {
                            countPri[0] = countPri[0]+30;
                            totalId.setText(String.valueOf("Total : "+countPri[0])+"tk"+"(Delivery charge 30tk)");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void senEmail() {
        String mEmail = "apelmahmudtipu@gmail.com";
        String mEmail2 = "sakibrhossain@gmail.com";
        String mSubject = Fname+" "+Lname;
        String mMessage = "Got an order. Go to app for details..";


        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
        JavaMailAPI javaMailAPI2 = new JavaMailAPI(this, mEmail2, mSubject, mMessage);

        javaMailAPI.execute();
        javaMailAPI2.execute();
    }
}
