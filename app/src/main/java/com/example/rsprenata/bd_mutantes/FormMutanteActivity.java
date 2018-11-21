package com.example.rsprenata.bd_mutantes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FormMutanteActivity extends AppCompatActivity {
    private Integer rowId;
    private EditText editTextNome;
    private ListView listViewHabilidades;
    private List<String> habilidades = new ArrayList<String>();
    private Mutante mutante;
    private HabilidadesAdapter habilidadesAdapter;
    public static final int REQUEST_ADICAO_HABILIDADE = 0;
    public static final int RESPONSE_ADICAO_HABILIDADE = 1;
    public static final int RESPONSE_EDITAR_HABILIDADE = 2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_mutante);

        editTextNome        = (EditText) findViewById(R.id.editTextNome);
        listViewHabilidades = (ListView) findViewById(R.id.listViewHabilidades);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            rowId = extras.getInt("row_id");

            new LoadMutantTask().execute(rowId);

            habilidadesAdapter = new HabilidadesAdapter(listViewHabilidades.getContext(), habilidades) {
                @Override
                public void editarHabilidade(String habilidade, Integer habilidadePosition) {
                    Intent intent = new Intent(getApplicationContext(), FormHabilidadeActivity.class);
                    intent.putExtra("operacao", "editar");
                    intent.putExtra("habilidade", habilidade);
                    intent.putExtra("habilidadePosition", habilidadePosition);
                    startActivityForResult(intent, REQUEST_ADICAO_HABILIDADE);
                }
            };
            listViewHabilidades.setAdapter(habilidadesAdapter);

        }

        Button saveMutantButton = (Button) findViewById(R.id.saveMutantButton);
        saveMutantButton.setOnClickListener(saveMutantButtonClicked);
    }



    private class LoadMutantTask extends AsyncTask<Integer, Object, Mutante> {
        DatabaseConnector databaseConnector = new DatabaseConnector(FormMutanteActivity.this);

        @Override
        protected Mutante doInBackground(Integer... params) {
            databaseConnector.open();

            return databaseConnector.carregarMutante(params[0]);
        }

        @Override
        protected void onPostExecute(Mutante result) {
            super.onPostExecute(result);

            editTextNome.setText(result.getNome());
            habilidades = result.getHabilidades();
            habilidadesAdapter.novosDados(habilidades);

            databaseConnector.close();
        }
    }

    View.OnClickListener saveMutantButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nome = editTextNome.getText().toString();
            if (nome.length() < 1 ) {
                Toast.makeText(FormMutanteActivity.this, "Digite um nome !", Toast.LENGTH_SHORT).show();
            } else if (habilidades.size() < 1) {
                Toast.makeText(FormMutanteActivity.this, "Adicione uma habilidade !", Toast.LENGTH_SHORT).show();
            } else {
                AsyncTask<Object, Object, Object> saveMutantTask = new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        saveMutant();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                };
                saveMutantTask.execute((Object[]) null);
            }
        }
    };

    public void adicionarHabilidade(View v) {
        Intent intent = new Intent(this, FormHabilidadeActivity.class);
        intent.putExtra("operacao", "adicionar");
        startActivityForResult(intent, REQUEST_ADICAO_HABILIDADE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADICAO_HABILIDADE) {//Verifica se é o retorno da activity adicionar habilidade
            if (resultCode == RESPONSE_ADICAO_HABILIDADE) {//Verifica qual o retorno dessa activity
                String novaHabilidade = data.getStringExtra("NEW_SKILL");
                habilidades.add(novaHabilidade);
            } else if (resultCode == RESPONSE_EDITAR_HABILIDADE) {
                String novaHabilidade = data.getStringExtra("NEW_SKILL");
                Integer habilidadePosition = data.getIntExtra("habilidadePosition", 0);
                habilidades.set(habilidadePosition, novaHabilidade);
            }
            habilidadesAdapter.novosDados(habilidades);
        }
    }

    private void saveMutant() {
        String nome = editTextNome.getText().toString();
        DatabaseConnector databaseConnector = new DatabaseConnector(this);

        Mutante mutante = new Mutante();
        mutante.setId(rowId);
        mutante.setNome(nome);
        mutante.setHabilidades(habilidades);

        if (getIntent().getExtras() != null) {
            databaseConnector.updateMutant(mutante);
            databaseConnector.deleteSkills(mutante.getId());
        } else {
            databaseConnector.insertMutante(mutante);
        }
        databaseConnector.insertHabilidades(mutante.getId(), habilidades);
        databaseConnector.close();
    }
}
