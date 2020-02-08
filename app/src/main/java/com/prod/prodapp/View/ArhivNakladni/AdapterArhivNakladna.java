package com.prod.prodapp.View.ArhivNakladni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prod.prodapp.R;
import com.prod.prodapp.View.DataOfNakladna.DataOfNakladnaView;

import java.util.ArrayList;
import java.util.List;

public class AdapterArhivNakladna extends RecyclerView.Adapter<AdapterArhivNakladna.MyHolder> {

    static Context context;
    static List<String> list = new ArrayList<>();
    AdapterArhivNakladna.OnItemListener monItemListener;

    public AdapterArhivNakladna(Context context, List<String> list, AdapterArhivNakladna.OnItemListener onItemListener)
    {
        this.context = context;
        this.list = list;
        this.monItemListener = onItemListener;
    }

    @NonNull
    @Override
    public AdapterArhivNakladna.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_row_arhiv,viewGroup,false);

        return new AdapterArhivNakladna.MyHolder(view, monItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterArhivNakladna.MyHolder myHolder, int i) {

//        myHolder.imageView.setImageResource(list.get(i).getImage());
        String [] temp = null;
        if (i < list.size())
            temp = list.get(i).split("\\+");
        if (temp != null && temp.length > 3) {
            myHolder.textView.setText(temp[0]);
            myHolder.textNakladnaName.setText(temp[1]);
            myHolder.textDataNakladnaName.setText(temp[2]);
            myHolder.textDataCreateName.setText(temp[3]);
            if (temp.length == 5) {
                myHolder.checkedTextView.setChecked(true);
                myHolder.checkedTextView.setText("Файл надісланий");
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView textView;
        TextView textNakladnaName;
        TextView textDataNakladnaName;
        TextView textDataCreateName;
        CheckedTextView checkedTextView;
        Button butshowfile;
        AdapterArhivNakladna.OnItemListener onItemListener;

        public MyHolder(@NonNull View itemView, AdapterArhivNakladna.OnItemListener onItemListener) {
            super(itemView);

//            imageView = itemView.findViewById(R.id.img_view);
            textView = itemView.findViewById(R.id.textDogovirName);
            textNakladnaName = itemView.findViewById(R.id.textNakladnaName);
            textDataNakladnaName = itemView.findViewById(R.id.textDataNakladnaName);
            textDataCreateName = itemView.findViewById(R.id.textDataCreateName);
            checkedTextView = itemView.findViewById(R.id.checkedTextView);
            butshowfile = itemView.findViewById(R.id.showarkhivbutton);

            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);

            butshowfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.fadein);
                    butshowfile.startAnimation(animation);
                    int t = getAdapterPosition();
                    String [] temp = null;
                    temp = list.get(t).split("\\+");
                    if (temp.length >= 4) {
                        String result = temp[0] + "+" + temp[1] + "+" + temp[2] + "+" + temp[3];
                        onItemListener.OnItemClick(t, result);

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
//            int t = getAdapterPosition();
            onItemListener.OnItemClickDelete(list.get(getAdapterPosition()));
        }
    }

    public interface OnItemListener
    {
        void OnItemClick(int position, String path);
        void OnItemClickDelete(String path);
    }
}
