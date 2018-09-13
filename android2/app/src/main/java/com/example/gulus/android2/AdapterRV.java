package com.example.gulus.android2;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class AdapterRV extends RecyclerView.Adapter<AdapterRV.ViewHolder> {
    private List<Model> mTexts;
    private Context mContext;
    AdapterListener adapterListener;
    public AdapterRV(Context context, List<Model> texts,AdapterListener adapterListener){
        this.mTexts = texts;
        this.mContext = context;
        this.adapterListener=adapterListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.idP.setText(String.valueOf(mTexts.get(position).getId()));
        holder.nameP.setText(mTexts.get(position).getName());
        holder.priceP.setText(String.valueOf(mTexts.get(position).getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterListener!=null)
                    adapterListener.onItemClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTexts.size();
    }

    public void setListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
      TextView idP;
      TextView nameP;
      TextView priceP;
      ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            idP = itemView.findViewById(R.id.idP);
            nameP = itemView.findViewById(R.id.nameP);
            priceP = itemView.findViewById(R.id.priceP);
            parentLayout = itemView.findViewById(R.id.parentLayout);

        }
    }
    public Model getItemFromPosition(int position){
        return mTexts.get(position);
    }
}