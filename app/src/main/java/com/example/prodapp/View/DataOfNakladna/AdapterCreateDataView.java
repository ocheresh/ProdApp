package com.example.prodapp.View.DataOfNakladna;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prodapp.Model.ProductsData;
import com.example.prodapp.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterCreateDataView extends RecyclerView.Adapter<AdapterCreateDataView.MyHolder>  {

    Context context;
    List<ProductsData> list = new ArrayList<>();
    List<ProductsData> list1 = new ArrayList<>();
    AdapterCreateDataView.OnItemListener monItemListener;




    public AdapterCreateDataView(Context context, List<ProductsData> list, AdapterCreateDataView.OnItemListener onItemListener)
    {
        this.context = context;
        this.list = list;
        this.list1 = list;
        this.monItemListener = onItemListener;
    }

    @NonNull
    @Override
    public AdapterCreateDataView.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_row_price,viewGroup,false);

        return new AdapterCreateDataView.MyHolder(view, monItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterCreateDataView.MyHolder myHolder, final int i) {

        myHolder.textView_number.setText((i + 1) + ".");
        myHolder.textView.setText(list.get(i).getKod() + "  " + list.get(i).getName());
        myHolder.textView_price.setText(String.valueOf(list.get(i).getPrice()) + " грн.");
        if (list.get(i).getKilbkistb() > 0)
        {
            myHolder.textView_kilkis.setText(String.valueOf(list.get(i).getKilbkistb()) + " " + list.get(i).getEdYch());
            myHolder.textView_sum.setText(new DecimalFormat("#0.00").format(list.get(i).getKilbkistb() * list.get(i).getPrice()) + " грн.");

        }

        myHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    Toast.makeText(context, list.get(i).getKod(), Toast.LENGTH_SHORT).show();
                    myHolder.onItemListener.OnCameraItemClick(list.get(i).getKod());
                }
                catch (Exception e) {}
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;
        TextView textView_price;
        TextView textView_kilkis;
        TextView textView_number;
        TextView textView_sum;
        ImageButton imageButton;

        AdapterCreateDataView.OnItemListener onItemListener;

        public MyHolder(@NonNull View itemView, AdapterCreateDataView.OnItemListener onItemListener) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView_price = itemView.findViewById(R.id.text_view_price);
            textView_kilkis = itemView.findViewById(R.id.text_view_kilbks);
            textView_number = itemView.findViewById(R.id.text_view_number);
            textView_sum = itemView.findViewById(R.id.text_view_suma);
            imageButton = itemView.findViewById(R.id.imageButton);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onItemListener.OnItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener
    {
        void OnItemClick(int position);
        void OnCameraItemClick(String kod);
    }

//    public interface OnCameraItemListener
//    {
//        void OnCameraItemClick(int position);
//    }

}