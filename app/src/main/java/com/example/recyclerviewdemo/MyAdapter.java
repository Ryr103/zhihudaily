package com.example.recyclerviewdemo;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.List;
import java.util.Map;



public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;
    private List<Map<String, Object>> list;
    private Context context;


    public MyAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View view1 = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        View view2 = LayoutInflater.from(context).inflate(R.layout.item_title,parent,false);

       if(viewType==NORMAL_ITEM) {
           return new ViewHolder_1(view1);
       }else if(viewType==GROUP_ITEM){
           return new ViewHolder_2(view2);
       }
       return null;
    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder_1){
        ((ViewHolder_1) holder).textView.setText(list.get(position).get("hint").toString());
          ((ViewHolder_1) holder).articleTitle.setText(list.get(position).get("title").toString());
            Glide.with(context).load(list.get(position).get("imageUrl").toString()).into(((ViewHolder_1) holder).imageView);
          ((ViewHolder_1) holder).button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,NewsActivity.class);
                Object url = list.get(position).get("url");
                Object id = list.get(position).get("id");
                intent.putExtra("id",id.toString());
                intent.putExtra("url",url.toString());
                context.startActivity(intent);
            }
        });}
        else if(holder instanceof ViewHolder_2){
            ((ViewHolder_2) holder).Date.setText(list.get(position).get("date").toString().substring(4,6)+"月"+list.get(position).get("date").toString().substring(6,8)+"日");
            ((ViewHolder_2) holder).textView.setText(list.get(position).get("hint").toString());
            ((ViewHolder_2) holder).articleTitle.setText(list.get(position).get("title").toString());
            Glide.with(context)
                    .load(list.get(position).get("imageUrl").toString())
                    .into(((ViewHolder_2) holder).imageView);
            ((ViewHolder_2) holder).button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,NewsActivity.class);
                    Object url = list.get(position).get("url");
                    Object id = list.get(position).get("id");
                    intent.putExtra("id",id.toString());
                    intent.putExtra("url",url.toString());
                    context.startActivity(intent);


                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || list.get(position).get("date").toString()!=list.get(position-1).get("date").toString()) {
            return GROUP_ITEM;
        } else {
            return NORMAL_ITEM;
        }
    }



    public  class ViewHolder_1 extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView articleTitle;
        private Button button;
        private ImageView imageView;


        ViewHolder_1(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.Author);
            button = itemView.findViewById(R.id.btn);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            imageView = itemView.findViewById(R.id.picture);
        }
    }

    public class ViewHolder_2 extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView articleTitle;
        private Button button;
        private TextView Date;
        private ImageView imageView;


        ViewHolder_2(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.Author);
            button = itemView.findViewById(R.id.btn);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            Date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.picture);
        }
    }


}
