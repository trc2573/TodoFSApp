package com.kgec.trc.todofsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText ettext;
    private CheckBox cbpriority;
    private CalendarView calview;
    private Button btnsave;
    private SimpleDateFormat sdf=new SimpleDateFormat("MMM d,yyyy");
    private Date targetDate=new java.util.Date();

    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ettext=findViewById(R.id.ettodo);
        cbpriority=findViewById(R.id.cbpriority);
        calview=findViewById(R.id.calview);
        calview.setMinDate(new java.util.Date().getTime());
        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Log.i("CALENDAR", "onSelectedDayChange: "+i+","+i1+","+i2);
                Calendar c=Calendar.getInstance();
                c.set(i,i1,i2);
                targetDate=c.getTime();
            }
        });
        btnsave=findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("FIRESTORE", "onClick: ");
                HashMap<String,Object> data=new HashMap<>();
                data.put("todotext",ettext.getText().toString().trim());
                data.put("cdate",new Timestamp(new java.util.Date()));
                data.put("targetdate",new Timestamp(targetDate));
                data.put("priority", cbpriority.isChecked());

                CollectionReference collection=firestore.collection("todocollection");
                Log.i("FIRESTORE", "onClick: collection reference created ");
                Task<DocumentReference> task=collection.add(data);
                Log.i("FIRESTORE","onClick: data added...");
                Task<DocumentReference> task1=task.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("FIRESTORE", "onSuccess: "+documentReference.getId());


                    }
                });
                Task<DocumentReference> task2=task1.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("FIRESTORE", "onFailure: "+e.getMessage());
                    }
                });


            }
        });
    }


}
