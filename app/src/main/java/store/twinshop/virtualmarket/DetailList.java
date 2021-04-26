package store.twinshop.virtualmarket;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import store.twinshop.virtualmarket.Common.Common;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static store.twinshop.virtualmarket.Common.Common.categoryId;

/**
 * Created by Tipu on 3/15/2018.
 */

public class DetailList extends ArrayAdapter<ItemDetail> implements Filterable {
    private Activity context;
    private List<ItemDetail> detailList,tempArray;
    int item;
    TextView textView3;
    TextView textView1;
    ImageView itemImage, bdtImg;
    CustomFilter cs;
    int n=0;


    DatabaseReference cartItem;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FirebaseUser user;

    String uid;
    String[] cha;

    public DetailList(Activity context, List<ItemDetail> detailList){
        super(context,R.layout.list_layout, detailList);
        this.context = context;
        this.detailList = detailList;
        this.tempArray = detailList;
    }

    @Override
    public int getCount() {
        return detailList.size();
    }

    @Override
    public ItemDetail getItem(int position) {
        return detailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        cartItem = database.getReference("Cart_Item");

        Common.i = position;

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        itemImage = (ImageView) listViewItem.findViewById(R.id.itemImageId);
        bdtImg = (ImageView) listViewItem.findViewById(R.id.bdtId);
        textView1 = (TextView) listViewItem.findViewById(R.id.foodNameId);
        textView3 = (TextView) listViewItem.findViewById(R.id.priceId);

        final ItemDetail detail = detailList.get(position);

        textView1.setText(detail.getFoodName());
        Picasso.with(getContext())
                .load(detail.getImageLink())
                .into(itemImage);

        if (detail.getAv().equals("unavailable now"))
        {
            bdtImg.setVisibility(View.GONE);
            textView3.setText(detail.getAv());
        }
        else
        {
            textView3.setText(detail.getPrice());
        }

        return listViewItem;
    }

    @Override
    public Filter getFilter() {
        if(cs==null)
        {
            cs = new CustomFilter();
        }

        return cs;
    }

    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0) {
                constraint = constraint.toString().toUpperCase();

                ArrayList<ItemDetail> filters = new ArrayList<>();

                for (int i = 0; i < tempArray.size(); i++) {
                    if (tempArray.get(i).getFoodName().toUpperCase().contains(constraint)||tempArray.get(i).getPrice().toUpperCase().contains(constraint)||tempArray.get(i).getSearchTag().toUpperCase().contains(constraint)) {
                        ItemDetail itemDetail = new ItemDetail(tempArray.get(i).getFoodName(), tempArray.get(i).getPrice(),tempArray.get(i).getCategoryId(),tempArray.get(i).getImageLink(),tempArray.get(i).getDescription(),tempArray.get(i).getSearchTag(),tempArray.get(i).getWsRate(),tempArray.get(i).getAv());
                        filters.add(itemDetail);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            }

            else
            {
                results.count = tempArray.size();
                results.values = tempArray;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            detailList = (ArrayList<ItemDetail>)results.values;
            notifyDataSetChanged();
        }
    }
}
