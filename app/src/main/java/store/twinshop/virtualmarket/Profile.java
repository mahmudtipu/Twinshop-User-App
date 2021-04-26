package store.twinshop.virtualmarket;

import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class Profile extends AppCompatActivity {

    EditText firstName, lastName, email, location;
    TextView mobileNumber;
    Button confirm;

    DatabaseReference cartItem,profile,orderedItem;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    List<Detail> detailList;

    String uid;

    String firstN, lastN, emailA, mobileN, locationA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        profile = database.getReference("Profile");

        firstName = findViewById(R.id.firstNameId);
        lastName = findViewById(R.id.lastNameId);
        email = findViewById(R.id.emailId);
        mobileNumber = findViewById(R.id.modileNumberId);
        location = findViewById(R.id.locationId);

        confirm = findViewById(R.id.confirmOrderId);

        mobileNumber.setText(Common.mobileNumber);

        confirm.setOnClickListener(new View.OnClickListener() {
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

                    finish();
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
                    String Fname = dataSnapshot.getValue(String.class);

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
                    String Lname = dataSnapshot.getValue(String.class);

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
}
