package com.example.rsprenata.bd_mutantes;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PesquisarActivity extends AppCompatActivity {
    private Spinner spinnerPesquisar;
    private ListView listViewMutantes;
    private ArrayAdapter<String> mutantesAdapter;
    private List<String> nomeMutantes;
    private EditText editTextPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);

        spinnerPesquisar = (Spinner) findViewById(R.id.spinnerPesquisar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pesquisar_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPesquisar.setAdapter(adapter);

        nomeMutantes = new ArrayList<String>();

        listViewMutantes = (ListView)findViewById(R.id.listViewMutantes);
        mutantesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomeMutantes);
        listViewMutantes.setAdapter(mutantesAdapter);

        editTextPesquisa = (EditText) findViewById(R.id.editTextPesquisa);

    }

    public void pesquisar (View v) {
        String pesquisarPor = spinnerPesquisar.getSelectedItem().toString();
        String pesquisa = editTextPesquisa.getText().toString();

        if (pesquisa.length() > 0) {
            new LoadMutantsTask().execute(pesquisarPor, pesquisa);
        } else {
            Toast.makeText(this, "Digite a pesquisa", Toast.LENGTH_SHORT).show();
        }
    }

    private class LoadMutantsTask extends AsyncTask<String, Object, List<String>> {
        DatabaseConnector databaseConnector = new DatabaseConnector(PesquisarActivity.this);

        @Override
        protected List<String> doInBackground(String... params) {
            databaseConnector.open();

            if ("Nome".equals(params[0]))
                return databaseConnector.getNomeMutantesByName(params[1]);
            else
                return databaseConnector.getNomeMutantesByHabilidade(params[1]);
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            if (result.size() == 0)
                Toast.makeText(PesquisarActivity.this, "Nenhum mutante encontrado", Toast.LENGTH_SHORT).show();

            nomeMutantes = result;
            mutantesAdapter.clear();
            mutantesAdapter.addAll(nomeMutantes);
            //mutantesAdapter.notifyDataSetChanged();

            databaseConnector.close();
        }
    }
}
