package com.example.personal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ExampleViewHolder>{

    private ArrayList<EnlistedItems> items ;

    private OnItemClickListener listener1 ;

    public interface OnItemClickListener{
        void onItemClick(int position) ;
        void onDeclineclick(int position) ;
        void onAcceptclick(int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        listener1 = listener ;
    }
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView Name ;
        public TextView Accept ;
        public TextView Decline  ;
        public ExampleViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            Name = itemView.findViewById(R.id.Friend_Name);
            Accept = itemView.findViewById(R.id.Accept) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });
            Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onAcceptclick(position);
                        }
                    }
                }
            });

            Decline = itemView.findViewById(R.id.Decline) ;
            Decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeclineclick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_addrow,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(V,listener1) ;
        return evh ;
    }
    public MyAdapter(ArrayList<EnlistedItems> Items){
        items = Items ;
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        EnlistedItems currentitem = items.get(position) ;
        holder.Name.setText(currentitem.NAME);
        holder.Accept.setText(currentitem.Accept);
        holder.Decline.setText(currentitem.Decline);
    }

    @Override
    public int getItemCount() {
        return items.size() ;
    }
}
