package com.codechez.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    private Button addNewBtn;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<ItemList> itemLists;
    private FirebaseFirestore firebaseFirestore;
    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNewBtn = findViewById(R.id.new_item);
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskAlert(v);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();
        itemLists = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(itemLists, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        registration.remove();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Getting data from Firestore
        Query query = firebaseFirestore.collection("Items");

        registration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    for (DocumentChange doc: value.getDocumentChanges()){
                        if(doc.getType() == DocumentChange.Type.ADDED){

                            String itemId = doc.getDocument().getId();

                            ItemList items = doc.getDocument().toObject(ItemList.class).withId(itemId);
                            itemLists.add(items);

                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void showAddTaskAlert(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a new task");

        final View alert_layout = getLayoutInflater().inflate(R.layout.alert_layout, null);
        builder.setView(alert_layout);

        builder.setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Adding data to Firestore

                EditText alertTaskContent = alert_layout.findViewById(R.id.alert_task_content);
                String taskContent = alertTaskContent.getText().toString();

                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("task_content", taskContent);
                itemMap.put("checked", false);

                firebaseFirestore.collection("Items").add(itemMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            String err = task.getException().getMessage();
                            Toast.makeText(MainActivity.this, "Error: " + err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
