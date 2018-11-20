package com.example.rsprenata.bd_mutantes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FormMutanteActivity extends AppCompatActivity {
    private long rowId;
    private EditText editTextNome;
    private ListView listViewHabilidades;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_mutante);

        editTextNome        = (EditText) findViewById(R.id.editTextNome);
        listViewHabilidades = (ListView) findViewById(R.id.listViewHabilidades);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            rowId = extras.getLong("row_id");
            //CARREGAR MUTANTE
        }

        Button saveMutantButton = (Button) findViewById(R.id.saveMutantButton);
        saveMutantButton.setOnClickListener(saveMutantButtonClicked);
    }

    OnClickListener saveMutantButtonClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nome.length() < 1 ) {
                Toast.makeText(this, "Digite um nome !", Toast.LENGTH_SHORT);
            } else if (habilidades.size() < 1) {
                Toast.makeText(this, "Adicione uma habilidade !", Toast.LENGTH_SHORT);
            } else {
                AsyncTask<Object, Object, Object> saveMutantTask = new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        saveMutant();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        finish();
                    }
                };
                saveMutantTask.execute((Object[]) null);
            }
        }
    };

    private void saveMutant() {
        DatabaseConnector databaseConnector = new DatabaseConnector(this);

        Mutante mutante = new Mutante();
        mutante.setNome(editTextNome.getText().toString());

        if (getIntent().getExtras() == null) {//Novo mutante
            databaseConnector.insertMutant(mutante);
        } else {//Editar mutante
            databaseConnector.updateContact(mutante);
        }
    }
}
