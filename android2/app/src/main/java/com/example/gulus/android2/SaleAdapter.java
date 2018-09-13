package com.example.gulus.android2;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapterSale";

    private List<SaleModel> mSaleList;
    private Context mContext;
    private View currentDetailedView = null;

    public SaleAdapter(Context context,List<SaleModel> mSaleList) {
        this.mSaleList = mSaleList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fatura, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        DataBaseClass db = new DataBaseClass(mContext);
        Model mdl = db.getProduct(mSaleList.get(position).getProductId());
        holder.quantity.setText(String.valueOf(mSaleList.get(position).getQuantity()));
        holder.totalPrice.setText(String.valueOf(mSaleList.get(position).getQuantity()*mdl.getPrice()));
        holder.name.setText(mdl.getName());
        holder.price.setText(String.valueOf(mdl.getPrice()));



    }

    @Override
    public int getItemCount() { return mSaleList.size(); }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView totalPrice;
        TextView name;
        TextView price;
        TextView quantity;


        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sale_item_name);
            price = itemView.findViewById(R.id.sale_item_price);
            totalPrice = itemView.findViewById(R.id.sale_item_totalPrice);
            quantity = itemView.findViewById(R.id.sale_item_quantity);
        }
    }
}
