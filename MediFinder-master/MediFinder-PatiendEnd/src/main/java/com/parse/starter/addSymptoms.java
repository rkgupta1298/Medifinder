package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static com.parse.starter.Emergency.Id;
import static com.parse.starter.Emergency.SymList;

public class addSymptoms extends AppCompatActivity {


    public void goToSymptoms(View view){
        Log.i("addSymptoms","Coming Here");

        startActivity(new Intent(addSymptoms.this,ListActivity.class));

    }

    public void retreiveToken(View view)
    {
        Log.i("Token","Token");
        ApiSpecialist api = new ApiSpecialist();
        api.execute();
        String token = api.token;
        Specialization task = new Specialization();

        Log.i("token",String.valueOf(token));
        task.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptoms);

        Intent intent=getIntent();
        String gender=intent.getStringExtra("Gender");
        Toast.makeText(addSymptoms.this, gender, Toast.LENGTH_SHORT).show();
        SymList.add(intent.getStringExtra("Symptom"));
        Id.put(intent.getStringExtra("Symptom"),intent.getStringExtra("ID"));
        EditText editText=(EditText)findViewById(R.id.symps);
        String z= "";
        Toast.makeText(this,intent.getStringExtra("Symptom"),Toast.LENGTH_LONG).show();
        int i;
        for (i=1;i<SymList.size();i++){
            Log.i("All Sym", String.valueOf(SymList.get(i)));
            Log.i("All Id",String.valueOf(Id.get(String.valueOf(SymList.get(i)))));
            z=z+'\n'+String.valueOf(SymList.get(i));
            editText.setText(z);

        }
    }
}
