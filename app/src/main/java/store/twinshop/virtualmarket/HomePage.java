package store.twinshop.virtualmarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import store.twinshop.virtualmarket.Common.Common;

public class HomePage extends AppCompatActivity implements View.OnClickListener{
    static final int Request_Code = 100;
    int lol=0, red=0;

    ImageView profilePhotoId,btnCart;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    TextView signOut,coinId;

    TextView myProfile,privacy,terms,shareApp, userMobile, userName, pending;

    DatabaseReference profile,cartItem;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String uid, Fname, Lname;

    Button customButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        customButton = findViewById(R.id.customId);
        customButton.setOnClickListener(this);

        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(this);

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
        profile = database.getReference("Profile");
        cartItem = database.getReference("Cart_Item");

        userMobile = findViewById(R.id.userMobileId);
        userName = findViewById(R.id.userNameId);

        profilePhotoId = findViewById(R.id.profilePhotoId);

        myProfile = findViewById(R.id.myProfileId);
        myProfile.setOnClickListener(this);

        terms = findViewById(R.id.termsId);
        terms.setOnClickListener(this);

        privacy = findViewById(R.id.privacyId);
        privacy.setOnClickListener(this);

        shareApp = findViewById(R.id.shareId);
        shareApp.setOnClickListener(this);

        pending = findViewById(R.id.pendingOrderId);
        pending.setOnClickListener(this);

        if(mAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,Login.class));
        }
        else
        {
            setDefaultFragment();
            setUpToolbar();
        }

        signOut = findViewById(R.id.signOutId);
        signOut.setOnClickListener(this);

        if(mAuth.getCurrentUser()!=null)
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    // Id of the provider (ex: google.com)
                    String providerId = profile.getProviderId();

                    // UID specific to the provider
                    String uid = profile.getUid();

                    // Name, email address, and profile photo Url
                    String name = profile.getDisplayName();
                    String mobile = profile.getPhoneNumber();
                    Uri photoUrl = profile.getPhotoUrl();

                    Common.mobileNumber = profile.getPhoneNumber();

                    Picasso.with(HomePage.this).load(R.drawable.man).resize(150,150).into(profilePhotoId);

                    userMobile.setText(mobile);
                }
            }
        }
    }

    private void setUpToolbar()
    {
        drawerLayout = findViewById(R.id.drawerLayoutId);
        toolbar = findViewById(R.id.toolBarId);

        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutId,CategoryFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

        if(v==btnCart)
        {
            Intent intent = new Intent(HomePage.this,AllCart.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if(v==customButton)
        {
            Intent intent = new Intent(HomePage.this,CustomOrder.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if(v==signOut)
        {
            mAuth.signOut();
            finish();
            startActivity(new Intent(this,Login.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        if(v==privacy)
        {
            String url = "https://sites.google.com/view/twinshop-privacy-policy";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }

        if(v==terms)
        {
            String url = "https://sites.google.com/view/twinshop-terms-and-conditions";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }

        if(v==myProfile)
        {
            Intent intent = new Intent(HomePage.this,Profile.class);
            startActivity(intent);
        }

        if(v==shareApp)
        {
            onInviteClicked();
        }

        if(v==pending)
        {
            Intent intent = new Intent(HomePage.this,PendingOrder.class);
            startActivity(intent);
        }
    }

    private void onInviteClicked()
    {

        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=store.twinshop.virtualmarket");
        intent.setType("text/plain");
        startActivityForResult(intent,Request_Code);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        cartItem.child(String.format("%s",uid)).child(String.format("%s",Common.categoryId)).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    btnCart.setVisibility(View.VISIBLE);
                }
                else
                {
                    btnCart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile.child(uid).child("first_name").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    Fname = dataSnapshot.getValue(String.class);
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
                    userName.setText(Fname+" "+Lname);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
