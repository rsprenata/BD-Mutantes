package com.example.rsprenata.bd_mutantes;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CADASTRAR_MUTANTE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cadastrar(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivityForResult(intent, REQUEST_CADASTRAR_MUTANTE);
    }

    public void listar(View view) {
        Intent intent = new Intent(this, ListarActivity.class);
        startActivity(intent);
    }

    public void pesquisar(View view) {
        Intent intent = new Intent(this, PesquisarActivity.class);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CADASTRAR_MUTANTE) {
            if (resultCode == RESULT_OK) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Sucesso");
                builder.setMessage("Mutante adicionado com sucesso !");
                builder.setPositiveButton("OK", null);
                builder.show();
            } else if (resultCode != RESULT_CANCELED)  {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Erro");
                builder.setMessage("Erro ao cadastrar mutante !");
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        }
    }
}
