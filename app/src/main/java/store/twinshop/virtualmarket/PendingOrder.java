package store.twinshop.virtualmarket;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class PendingOrder extends AppCompatActivity {

    ListView pendingList,customPendingList;
    DatabaseReference pendingItem, shopName;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    List<PendingDetail> detailList;
    List<Detail> detailL;
    LinearLayout cardView,cardView2;
    RelativeLayout noPending;

    TextView totalBill, totalId;

    String uid;
    int count,c;

    FloatingActionButton call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        call = findViewById(R.id.btnCall);

        final int[] countPri = new int[1];

        totalBill = findViewById(R.id.totalBillId);
        totalId = findViewById(R.id.totalId);

        cardView = findViewById(R.id.cardId);
        noPending = findViewById(R.id.noPendingId);
        cardView2 = findViewById(R.id.cardId2);

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
        pendingItem = database.getReference("Ordered_Item");
        shopName = database.getReference("Shop_Name");

        detailList = new ArrayList<>();
        detailL = new ArrayList<>();

        pendingList = (ListView) findViewById(R.id.pendingListId);
        customPendingList = (ListView) findViewById(R.id.customPendingId);

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
                        pendingItem.child(uid).child(String.valueOf(i))
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue()!=null)
                                        {
                                            cardView.setVisibility(View.VISIBLE);
                                            noPending.setVisibility(View.GONE);
                                            call.setVisibility(View.VISIBLE);
                                            for(DataSnapshot detailSnapshot: dataSnapshot.getChildren()){
                                                PendingDetail detail = detailSnapshot.getValue(PendingDetail.class);

                                                detailList.add(detail);

                                                PendingDetailList adapter = new PendingDetailList(PendingOrder.this, detailList);

                                                pendingList.setAdapter(adapter);

                                                int pr = Integer.parseInt(detail.getFoodPrice());
                                                countPri[0] = countPri[0] +pr;
                                            }

                                            count = countPri[0]+30;
                                            totalBill.setText("Total: "+String.valueOf(count)+"tk"+"(Delivery charge 30tk)");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                    else if(i<2)
                    {
                        pendingItem.child(uid).child(String.valueOf(i))
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue()!=null)
                                        {
                                            cardView2.setVisibility(View.VISIBLE);
                                            noPending.setVisibility(View.GONE);
                                            call.setVisibility(View.VISIBLE);
                                            for(DataSnapshot detailSnapshot: dataSnapshot.getChildren()){
                                                Detail detail = detailSnapshot.getValue(Detail.class);

                                                detailL.add(detail);

                                                CustomPendDetailList adapter = new CustomPendDetailList(PendingOrder.this, detailL);

                                                customPendingList.setAdapter(adapter);
                                            }
                                            totalId.setText("Total: Products Price + "+" Delivery charge 30tk");
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

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = "tel:"+"+8801768729726";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(s));
                startActivity(intent);
            }
        });
    }
}
