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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        //set Tag
        holder.mCheckBox.setTag(itemId);
        holder.mContent.setTag(itemId);
        holder.delete.setTag(itemId);

        //strike the text if checkbox is checked
//        if(holder.mCheckBox.isChecked()){
//            holder.mContent.setPaintFlags(holder.mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        }else{
//            holder.mContent.setPaintFlags(holder.mContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//        }

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //update checkbox data in db
                String checkboxTag = buttonView.getTag().toString();

                if(isChecked){
                    db.collection("Items").document(checkboxTag).update("checked", true);
                }else{
                    db.collection("Items").document(checkboxTag).update("checked", false);
                }
            }
        });

        //TODO deleting the items when delete button is clicked
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String deleteTag = v.getTag().toString();
                db.collection("Items").document(deleteTag).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            db.collection("Items").document(deleteTag).delete();
                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }

    public void removeItem(int position) {
        listOfItems.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mContent;
        private CheckBox mCheckBox;
        private View mView;
        private ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mContent = itemView.findViewById(R.id.content);
            mCheckBox = itemView.findViewById(R.id.checkBox);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
