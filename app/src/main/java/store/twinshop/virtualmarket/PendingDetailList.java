package store.twinshop.virtualmarket;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by Tipu on 3/15/2018.
 */

public class PendingDetailList extends ArrayAdapter<PendingDetail> {
    private Activity context;
    private List<PendingDetail> pendingDetailList;
    int item;
    TextView textView3;
    TextView textView1;
    TextView quantity;
    TextView locate;

    public PendingDetailList(Activity context, List<PendingDetail> pendingDetailList){
        super(context,R.layout.pending_list_layout, pendingDetailList);
        this.context = context;
        this.pendingDetailList = pendingDetailList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.pending_list_layout,null,true);

        textView1 = (TextView) listViewItem.findViewById(R.id.foodNameId);
        textView3 = (TextView) listViewItem.findViewById(R.id.priceId);
        quantity = (TextView) listViewItem.findViewById(R.id.quantityId);
        locate = (TextView) listViewItem.findViewById(R.id.fromId);

        PendingDetail detail = pendingDetailList.get(position);

        textView1.setText(detail.getFoodName());
        textView3.setText(detail.getFoodPrice());
        quantity.setText(detail.getFoodQuantities());
        locate.setText(detail.getLocation());

        return listViewItem;
    }
}
