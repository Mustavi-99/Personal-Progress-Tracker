    package com.example.personal;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;

    public class MyThirdAdapter extends RecyclerView.Adapter<MyThirdAdapter.ExampleViewHolder>{

    private ArrayList<EnlistedItems> items ;

    private OnItemClickListener listener1 ;

        public interface OnItemClickListener{
            void onItemclick(int position) ;
            void onDoneclick(int position) ;
            void onCheckclick(int position) ;
        }

            public void setOnItemClickListener(MyThirdAdapter.OnItemClickListener listener){
                listener1 = listener ;
            }
        public static class ExampleViewHolder extends RecyclerView.ViewHolder {
            public TextView ewhf_t ;
            public TextView Done ;
            public TextView Check  ;
            public ExampleViewHolder(View itemView, MyThirdAdapter.OnItemClickListener listener) {
                super(itemView);
                ewhf_t = itemView.findViewById(R.id.ewfh_textview);
                Done = itemView.findViewById(R.id.Done) ;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener!=null){
                            int position = getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION){
                                listener.onItemclick(position);
                            }
                        }
                    }
                });
                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener!=null){
                            int position = getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION){
                                listener.onDoneclick(position);
                            }
                        }
                    }
                });
                Check = itemView.findViewById(R.id.Check) ;
                Check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener!=null){
                            int position = getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION){
                                listener.onCheckclick(position);
                            }
                        }
                    }
                });
            }
        }

            @Override
            public MyThirdAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_addewfh,parent,false);
                MyThirdAdapter.ExampleViewHolder evh = new MyThirdAdapter.ExampleViewHolder(V,listener1) ;
                return evh ;
            }

            public MyThirdAdapter(ArrayList<EnlistedItems> Items){
                items = Items ;
            }

            @Override
            public void onBindViewHolder(MyThirdAdapter.ExampleViewHolder holder, int position) {
                EnlistedItems currentitem = items.get(position) ;
                holder.ewhf_t.setText(currentitem.TEXT);
                holder.Done.setText(currentitem.Done);
                holder.Check.setText(currentitem.Check);
            }
            @Override
            public int getItemCount() {
                return items.size() ;
            }
        }
