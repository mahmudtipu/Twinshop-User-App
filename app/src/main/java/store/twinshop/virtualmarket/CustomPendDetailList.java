package store.twinshop.virtualmarket;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * Created by Tipu on 3/15/2018.
 */

public class CustomPendDetailList extends ArrayAdapter<Detail> {
    private Activity context;
    private List<Detail> cartDetailList;
    int orderCount;
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
    ImageView cancel;

    public CustomPendDetailList(Activity context, List<Detail> cartDetailList){
        super(context,R.layout.custom_pending_list_layout, cartDetailList);
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

        View listViewItem = inflater.inflate(R.layout.custom_pending_list_layout,null,true);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        cartItem = database.getReference("Cart_Item");

        textView1 = (TextView) listViewItem.findViewById(R.id.foodNameId);

        detail = cartDetailList.get(position);

        textView1.setText(detail.getFoodName());

        return listViewItem;
    }
}
