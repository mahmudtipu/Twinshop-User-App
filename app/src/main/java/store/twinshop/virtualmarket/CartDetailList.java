package store.twinshop.virtualmarket;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import store.twinshop.virtualmarket.Common.Common;

import static store.twinshop.virtualmarket.Common.Common.categoryId;

/**
 * Created by Tipu on 3/15/2018.
 */

public class CartDetailList extends ArrayAdapter<Detail> {
    private Activity context;
    private List<Detail> cartDetailList;
    int item;
    TextView textView3;
    TextView textView1;
    TextView quantity;
    TextView resName;
    TextView negative, positive;
    Detail detail;
    DatabaseReference cartItem;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;

    public CartDetailList(Activity context, List<Detail> cartDetailList){
        super(context,R.layout.cart_list_layout, cartDetailList);
        this.context = context;
        this.cartDetailList = cartDetailList;
    }

    @Override
    public int getCount() {
        return cartDetailList.size();
    }

    @Override
    public Detail getItem(int position) {
        return cartDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.cart_list_layout,null,true);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        cartItem = database.getReference("Cart_Item");

        textView1 = (TextView) listViewItem.findViewById(R.id.foodNameId);
        textView3 = (TextView) listViewItem.findViewById(R.id.priceId);
        quantity = (TextView) listViewItem.findViewById(R.id.quantityId);
        negative = (TextView) listViewItem.findViewById(R.id.netativeId);
        positive = (TextView) listViewItem.findViewById(R.id.positiveId);

        detail = cartDetailList.get(position);

        textView1.setText(detail.getFoodName());
        textView3.setText(detail.getFoodPrice());
        quantity.setText(detail.getFoodQuantity());

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String foodName = cartDetailList.get(position).getFoodName();
                String foodPrice = cartDetailList.get(position).getFoodPrice();
                String foodQ = cartDetailList.get(position).getFoodQuantity();
                String resN = cartDetailList.get(position).getRestaurantName();
                String wsRate = cartDetailList.get(position).getWsRate();

                int foodQuan = Integer.parseInt(foodQ);
                int foodPri = Integer.parseInt(foodPrice);
                int wsPri = Integer.parseInt(wsRate);
                if (foodQuan>0)
                {
                    foodPri = foodPri-(foodPri/foodQuan);
                    wsPri = wsPri-(wsPri/foodQuan);
                    foodQuan = foodQuan-1;

                    if (foodQuan!=0)
                    {
                        Detail d = new Detail(foodName, String.valueOf(foodPri), String.valueOf(foodQuan), Common.CategoryIndex,String.valueOf(wsPri));

                        cartItem.child(String.format("%s",uid)).child(String.format("%s",Common.categoryId)).child(String.format("%s",foodName)).setValue(d);
                    }
                    else
                    {
                        cartItem.child(uid).child(categoryId).child(cartDetailList.get(position).foodName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    dataSnapshot.getRef().removeValue();

                                    cartItem.child(uid).child(categoryId).addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getValue()==null)
                                            {
                                                ((Activity)context).finish();
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
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String foodName = cartDetailList.get(position).getFoodName();
                String foodPrice = cartDetailList.get(position).getFoodPrice();
                String foodQ = cartDetailList.get(position).getFoodQuantity();
                String resN = cartDetailList.get(position).getRestaurantName();
                String wsRate = cartDetailList.get(position).getWsRate();

                int foodQuan = Integer.parseInt(foodQ);
                int foodPri = Integer.parseInt(foodPrice);
                int wsPri = Integer.parseInt(wsRate);
                if (foodQuan>0)
                {
                    foodPri = foodPri+(foodPri/foodQuan);
                    wsPri = wsPri+(wsPri/foodQuan);
                    foodQuan = foodQuan+1;

                    if (foodQuan!=0)
                    {
                        Detail d = new Detail(foodName, String.valueOf(foodPri), String.valueOf(foodQuan), Common.CategoryIndex,String.valueOf(wsPri));

                        cartItem.child(String.format("%s",uid)).child(String.format("%s",Common.categoryId)).child(String.format("%s",foodName)).setValue(d);
                    }

                }
            }
        });

        return listViewItem;
    }
}
