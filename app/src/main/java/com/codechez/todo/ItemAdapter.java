package com.codechez.todo;

import android.content.ClipData;
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

    public ItemAdapter(List<ItemList> listOfItems) {
        this.listOfItems = listOfItems;
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
        holder.mContent.setText(listOfItems.get(pos).getText());
        holder.mCheckBox.setChecked(listOfItems.get(pos).getSelected());

        holder.mCheckBox.setTag(listOfItems.get(pos));

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

    public List<ItemList> getListOfItems(){
        return listOfItems;
    }
}
