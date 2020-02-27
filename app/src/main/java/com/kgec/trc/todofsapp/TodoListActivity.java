package com.kgec.trc.todofsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth auth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        firestore.collection("todocollection")
                .orderBy("cdate")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.i("FIRESTORE", "onEvent: "+e.getMessage());
                        }else{
                            List<DocumentSnapshot> list=snapshots.getDocuments();
                            for(DocumentSnapshot snapshot:list){
                                String todotext=(String)snapshot.get("todotext");
                                Log.i("FIRESTORE", "onEvent: "+todotext);
                            }
                        }
                    }
                });
    }
}
