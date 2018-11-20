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

public class CadastroActivity extends AppCompatActivity {
    public static final int REQUEST_ADICAO_HABILIDADE = 0;
    public static final int RESPONSE_ADICAO_HABILIDADE = 1;
    public static final int RESPONSE_EDITAR_HABILIDADE = 2;
    private ListView listViewHabilidades;
    private List<String> habilidades;
    private HabilidadesAdapter habilidadesAdapter;
    private EditText editTextNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        habilidades = new ArrayList<String>();

        listViewHabilidades = (ListView)findViewById(R.id.listViewHabilidades);
        editTextNome = (EditText)findViewById(R.id.editTextNome);

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
            atualizarHabilidades(habilidades);
        }
    }

    public void atualizarHabilidades(List<String> habilidades) {
        habilidadesAdapter.novosDados(habilidades);
    }

    public void cadastrarMutante(View view) {
        String nome = editTextNome.getText().toString();
        if (nome.length() < 1 ) {
            Toast.makeText(this, "Digite um nome !", Toast.LENGTH_SHORT);
        } else if (habilidades.size() < 1) {
            Toast.makeText(this, "Adicione uma habilidade !", Toast.LENGTH_SHORT);
        } else {
            DatabaseConnector databaseConnector = new DatabaseConnector(this);

            Mutante mutante = new Mutante();
            mutante.setNome(nome);
            mutante.setHabilidades(habilidades);

            databaseConnector.insertMutante(mutante);
            databaseConnector.insertHabilidades(mutante.getId(), habilidades);

            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}
