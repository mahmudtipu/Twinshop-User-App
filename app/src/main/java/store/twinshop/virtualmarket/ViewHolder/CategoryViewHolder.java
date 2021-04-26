package store.twinshop.virtualmarket.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import store.twinshop.virtualmarket.Interface.ItemClickListener;
import store.twinshop.virtualmarket.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView category_name;
    public TextView location_name;
    public ImageView category_image;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        category_image = itemView.findViewById(R.id.categoryImageId);
        category_name = itemView.findViewById(R.id.categoryTextId);
        location_name = itemView.findViewById(R.id.locationId);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}