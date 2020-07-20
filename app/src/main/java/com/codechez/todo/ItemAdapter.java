package com.codechez.todo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.context.AttributeContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ItemList> listOfItems;
    private Context context;
    private View recycler_layout;
    private FirebaseFirestore db;
    private static final String TAG = "ItemAdapter";

    public ItemAdapter(List<ItemList> listOfItems, Context ctx) {
        this.listOfItems = listOfItems;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        recycler_layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
        db = FirebaseFirestore.getInstance();
        return new ViewHolder(recycler_layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final int pos = position;

        //get data from firestore
        final String content = listOfItems.get(pos).getTask_content();
        boolean checkbox = listOfItems.get(pos).isChecked();
        String itemId = listOfItems.get(pos).ItemId;

        //set data in the view
        holder.mContent.setText(content);
        holder.mCheckBox.setChecked(checkbox);
        holder.mCheckBox.setTag(itemId);

        holder.mContent.setTag(itemId);

        //strike the text if checkbox is checked
        if(holder.mCheckBox.isChecked()){
            holder.mContent.setPaintFlags(holder.mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.mContent.setPaintFlags(holder.mContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //update checkbox data in db
                String checkboxTag = buttonView.getTag().toString();
                Object tag = buttonView.getTag();

                if(isChecked){
                    db.collection("Items").document(checkboxTag).update("checked", true);
                }else{
                    db.collection("Items").document(checkboxTag).update("checked", false);
                }

                holder.strikeIt(tag);
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
        private View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mContent = itemView.findViewById(R.id.content);
            mCheckBox = itemView.findViewById(R.id.checkBox);
        }

        public void strikeIt(Object tag){

            TextView textView = mView.findViewWithTag(tag);

            if(!textView.getPaint().isStrikeThruText()){
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }
}
