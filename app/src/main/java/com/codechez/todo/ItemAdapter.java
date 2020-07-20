package com.codechez.todo;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemList> listOfItems;
    private Context context;

    public ItemAdapter(List<ItemList> listOfItems, Context ctx) {
        this.listOfItems = listOfItems;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;

        //get data from firestore
        String content = listOfItems.get(pos).getTask_content();
        boolean checkbox = listOfItems.get(pos).isChecked();
        String itemId = listOfItems.get(pos).ItemId;

        //set data in the view
        holder.mContent.setText(content);
        holder.mCheckBox.setChecked(checkbox);

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO add onclick listener on checkbox (strikethru) & updating checkbox in firebase
                // if(!text.getPaint().isStrikeThruText()){
                //   text.setPaintFlags(text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG)
                // }else{
                //   text.setPaintFlags(text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG))
                // }
            }
        });


        //TODO deleting the items when delete button is clicked

    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mContent;
        private CheckBox mCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.content);
            mCheckBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
