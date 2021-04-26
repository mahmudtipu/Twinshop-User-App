package store.twinshop.virtualmarket;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import store.twinshop.virtualmarket.Common.Common;
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

public class Start extends AppCompatActivity implements TextWatcher{

    EditText e1;
    String foodName, foodImage, foodPrice, foodDescription, wsRate, av;

    int i,totalCorrectAnswer, progress,clickGo=0;

    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference food_item;
    ListView listViewFoodItem;

    FirebaseAuth mAuth;
    FirebaseUser user;
    List<ItemDetail> detailList,tempArr;
    List<Detail> cartDetail;
    List<Detail> detailLi;

    String uid;

    Button orderNow;

    FloatingActionButton cartAct;

    DatabaseReference cartItem;

    DetailList adapter;
    ItemDetail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        /*if(Build.VERSION.SDK_INT>=21)
        {
            Window window;
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryAnother));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/

        cartDetail = new ArrayList<>();

        progressBar = findViewById(R.id.progressBarId);

        e1= (EditText) findViewById(R.id.edittextid);
        e1.addTextChangedListener(this);

        //e1.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();

        listViewFoodItem = (ListView) findViewById(R.id.listBuyerViewId);
        food_item = database.getReference("Item");
        detailList = new ArrayList<>();
        tempArr = new ArrayList<>();

        detailLi = new ArrayList<>();

        cartItem = database.getReference("Cart_Item");

        progressBar.setVisibility(View.VISIBLE);
        thread.start();

        cartAct = findViewById(R.id.btnCart);

        cartAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this,CartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        /*final SharedPreferences sharedPreferences = getSharedPreferences(OrderDetails,Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("Set", "");

        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> arrPackageData = gson.fromJson(json, type);
        for(String data:arrPackageData) {
            Cart.setText(json);
        }*/

        //loadQuestions(categoryId);

        orderNow = findViewById(R.id.orderNowId);

        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this,Confirm.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        food_item.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        detailList.clear();

                        for(DataSnapshot detailSnapshot: dataSnapshot.getChildren()){
                            detail = detailSnapshot.getValue(ItemDetail.class);

                            detailList.add(detail);
                        }

                        adapter = new DetailList(Start.this, detailList);

                        listViewFoodItem.setAdapter(adapter);

                        listViewFoodItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                detail = adapter.getItem(position);

                                foodName = detail.getFoodName();
                                foodImage = detail.getImageLink();
                                foodPrice = detail.getPrice();
                                foodDescription = detail.getDescription();
                                wsRate = detail.getWsRate();
                                av = detail.getAv();

                                if(!av.equals("unavailable now"))
                                {
                                    Intent intent = new Intent(Start.this,AddToCart.class);
                                    intent.putExtra("foodName",foodName);
                                    intent.putExtra("foodImage",foodImage);
                                    intent.putExtra("foodPrice",foodPrice);
                                    intent.putExtra("foodDescription",foodDescription);
                                    intent.putExtra("wsRate",wsRate);
                                    startActivity(intent);
                                    view.setSelected(true);
                                }
                            }
                        });
                        progressBar.setVisibility(View.GONE);
                        e1.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public  void doWork()
    {
        for(progress=10; progress<=500;progress=progress+10)
        {
            try {
                Thread.sleep(500);
                progressBar.setProgress(progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*private void loadQuestions(String categoryId) {
        if(Common.questionList.size() > 0)
        {
            Common.questionList.clear();
        }

        food_item.orderByChild("CategoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            Question ques = postSnapshot.getValue(Question.class);
                            Common.questionList.add(ques);

                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        Collections.shuffle(Common.questionList);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        e1.getText().clear();

        cartItem.child(String.format("%s",uid)).child(String.format("%s",Common.categoryId)).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    orderNow.setVisibility(View.VISIBLE);
                    cartAct.setVisibility(View.VISIBLE);
                }
                else
                {
                    orderNow.setVisibility(View.GONE);
                    cartAct.setVisibility(View.GONE);
                    Common.cb = null;
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

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.adapter.getFilter().filter(s);
        //Toast.makeText(Start.this, "F. Okay", Toast.LENGTH_LONG).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
