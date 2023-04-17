package com.example.personal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.ExampleViewHolder>{

    private ArrayList<EnlistedItems> items ;
    private SecondAdapter.OnItemClickListener listener1 ;
    public interface OnItemClickListener{
        void onItemClick(int position) ;
    }
    public void setOnItemClickListener(SecondAdapter.OnItemClickListener listener){
        listener1 = listener ;
    }
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView Name ;
        public TextView Remove  ;
        public ExampleViewHolder(View itemView, SecondAdapter.OnItemClickListener listener) {
            super(itemView);
            Name = itemView.findViewById(R.id.ShowName);
            Remove = itemView.findViewById(R.id.Remove) ;
            Remove.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    @Override
    public SecondAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_friends,parent,false);
        SecondAdapter.ExampleViewHolder evh = new SecondAdapter.ExampleViewHolder(V,listener1) ;
        return evh ;
    }
    public SecondAdapter(ArrayList<EnlistedItems> Items){
        items = Items ;
    }
    @Override
    public void onBindViewHolder(SecondAdapter.ExampleViewHolder holder, int position) {
        EnlistedItems currentitem = items.get(position) ;
        holder.Name.setText(currentitem.NAME);
        holder.Remove.setText(currentitem.Remove);
    }
    @Override
    public int getItemCount() {
        return items.size() ;
    }
}
