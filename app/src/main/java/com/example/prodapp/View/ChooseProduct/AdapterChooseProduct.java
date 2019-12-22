package com.example.prodapp.View.ChooseProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prodapp.Model.ProductsData;
import com.example.prodapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterChooseProduct extends RecyclerView.Adapter<AdapterChooseProduct.MyHolder>  implements Filterable {

    Context context;
    static List<ProductsData> list = new ArrayList<>();
    static List<ProductsData> list1 = new ArrayList<>();
    OnItemListener monItemListener;

    public AdapterChooseProduct(Context context, List<ProductsData> list, OnItemListener onItemListener)
    {
        this.context = context;
        this.list = list;
        this.list1 = list;
        this.monItemListener = onItemListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_row,viewGroup,false);

        return new MyHolder(view, monItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

//        myHolder.imageView.setImageResource(list.get(i).getImage());
        myHolder.textView.setText(list.get(i).getKod() + " " + list.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;
        OnItemListener onItemListener;

        public MyHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

//            imageView = itemView.findViewById(R.id.img_view);
            textView = itemView.findViewById(R.id.text_view);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int t = getAdapterPosition();
            t = get_correct_position(t);
            onItemListener.OnItemClick(t);
        }
    }

    private static int get_correct_position(int position)
    {
        int i = 0;
        for (ProductsData data : list1) {
            if (data.getName().equals(list.get(position).getName()))
            {
                return (i);
            }
            i++;
        }

        return (position);
    }

    public interface OnItemListener
    {
        void OnItemClick(int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();

                if (charString.isEmpty()){
                    list = list1;
                }else{

                    List<ProductsData> filterList = new ArrayList<>();

                    for (ProductsData data : list1){

                        if (data.getInfoName().toLowerCase().contains(charString)){
                            filterList.add(data);
                        }
                    }

//                    if (filterList.isEmpty())
//                        list = list1;
//                    else
                    list = filterList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                list = (List<ProductsData>) results.values;
                notifyDataSetChanged();
            }
        };

    }
}
