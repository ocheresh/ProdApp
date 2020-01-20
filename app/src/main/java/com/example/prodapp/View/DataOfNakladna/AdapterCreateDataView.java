package com.example.prodapp.View.DataOfNakladna;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prodapp.Model.ProductsData;
import com.example.prodapp.Presenter.DataOfNakladna.DataOfNakladnaPresenter;
import com.example.prodapp.R;
import com.example.prodapp.View.Register.RegisterActivity;
import com.example.prodapp.View.SplashActivity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterCreateDataView extends RecyclerView.Adapter<AdapterCreateDataView.MyHolder>  {

    Context context;
    List<ProductsData> list = new ArrayList<>();
//    List<ProductsData> list1 = new ArrayList<>();
    AdapterCreateDataView.OnItemListener monItemListener;




    public AdapterCreateDataView(Context context, List<ProductsData> list, AdapterCreateDataView.OnItemListener onItemListener)
    {
        this.context = context;
        this.list = list;
//        this.list1 = list;
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

        if (list.get(i).getAddphoto().equals("true"))
        {
            myHolder.imageButton.setImageResource(R.drawable.photo_icon_green);
            myHolder.but_viewphoto.setClickable(true);
            myHolder.but_viewphoto.setVisibility(View.VISIBLE);

        }



        myHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Toast.makeText(context, list.get(i).getKod(), Toast.LENGTH_SHORT).show();

                    list.get(i).setAddphoto("true");
                    DataOfNakladnaView.dbHelper.updateData(String.valueOf(i + 1), list.get(i));

                    Toast.makeText(context, "one", Toast.LENGTH_SHORT).show();

                    if (myHolder == null)
                        Log.i("Holder: ", "holder is null");

                    myHolder.onItemListener.OnCameraItemClick(list.get(i).getKod());
                    myHolder.imageButton.setImageResource(R.drawable.photo_icon_green);
//                    myHolder.textView_photoind.setText("Фото додано");
//                    myHolder.textView_photoind.setTextColor(Color.parseColor("#20e631"));

                    Toast.makeText(context, "two", Toast.LENGTH_SHORT).show();
                    myHolder.but_viewphoto.setClickable(true);
                    myHolder.but_viewphoto.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "three", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e) {
                    Log.i("Error take photo: ", e.getMessage());
                }
            }
        });

        myHolder.but_viewphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.fadein);
                    myHolder.but_viewphoto.startAnimation(animation);
                    myHolder.onItemListener.OnViewPhoto(list.get(i).getKod().replaceAll("\\D", ""));
                }
                catch (Exception e){
                    Log.i("Error view photo: ", e.getMessage());
                }
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
        TextView textView_photoind;
        Button but_viewphoto;
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
            but_viewphoto = itemView.findViewById(R.id.button_viewphoto);
//            textView_photoind = itemView.findViewById(R.id.textPhotoind);
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
        void OnViewPhoto(String kod);
    }

//    public interface OnCameraItemListener
//    {
//        void OnCameraItemClick(int position);
//    }

}