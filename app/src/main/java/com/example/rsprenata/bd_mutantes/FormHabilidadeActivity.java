package com.example.rsprenata.bd_mutantes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormHabilidadeActivity extends AppCompatActivity {
	private EditText editTextHabilidade;
	private Button btnAdEdHabilidade;
	private Integer mutanteId;
	public static String operacao;
    public static String habilidade;
    public static Integer habilidadePosition;
    public static final int RESPONSE_ADICAO_HABILIDADE = 1;
    public static final int RESPONSE_EDITAR_HABILIDADE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_habilidade);

        editTextHabilidade = (EditText) findViewById(R.id.editTextHabilidade);
        btnAdEdHabilidade = (Button) findViewById(R.id.btnAdEdHabilidade);

        operacao = getIntent().getStringExtra("operacao");

        if ("editar".equals(operacao)) {
            habilidade = getIntent().getStringExtra("habilidade");
            habilidadePosition = getIntent().getIntExtra("habilidadePosition", 0);
            editTextHabilidade.setText(habilidade);
            btnAdEdHabilidade.setText("Editar");
            setTitle("Editar Habilidade");
        }
    }

    public void adicionarHabilidade(View v) {
        String habilidade = editTextHabilidade.getText().toString();

        if (habilidade.length() > 0) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_SKILL", habilidade);
            if ("editar".equals(operacao)) {

                resultIntent.putExtra("habilidadePosition", habilidadePosition);
                setResult(RESPONSE_EDITAR_HABILIDADE, resultIntent);
            } else {
                setResult(RESPONSE_ADICAO_HABILIDADE, resultIntent);
            }
            finish();
            /*AsyncTask<Object, Object, Object> saveMutantTask = new AsyncTask<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... objects) {
                    saveS();
                    return null;
                }

                @Override
                protected void onPostExecute(Object result) {
                    finish();
                }
            };
            saveMutantTask.execute((Object[]) null);*/
        } else {
            Toast.makeText(this, "Digite a habilidade !", Toast.LENGTH_SHORT);
        }
    }

    public void voltar(View v) {
        finish();
    }
}
