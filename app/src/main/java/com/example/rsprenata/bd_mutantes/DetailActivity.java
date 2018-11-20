package com.example.rsprenata.bd_mutantes;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DetailActivity extends AppCompatActivity {
    private long rowId;
    private TextView textViewNome;
    private ListView listViewHabilidades;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        listViewHabilidades = (ListView) findViewById(R.id.listViewHabilidades);

        rowId = getIntent().getLongExtra("row_id");
    }

    @Override
    protected void onResume() {
        super.onResume();

        new LoadMutantTask().execute(rowId);
    }

    private class LoadMutantTask extends AsyncTask<Long, Object, Cursor> {
        DatabaseConnector databaseConnector = new DatabaseConnector(DetailActivity.this);

        @Override
        protected Cursor doInBackground(Long... params) {
            databaseConnector.open();

            return databaseConnector.getOneMutant(params[0]);
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            result.moveToFirst();

            int nomeIndex = result.getColumnIndex("nome");

            textViewNome.setText(result.getString(nomeIndex));

            result.close();
            databaseConnector.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_detail, menu);
        return true;
    }

    private void deleteMutant() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);

        builder.setTitle("Confirmação");
        builder.setMenssage("Deseja excluir esse mutante?");
        builder.setPositiveButton("Excluir", 
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int button) {
                    final DatabaseConnector databaseConnector = new DatabaseConnector(DetailActivity.this);

                    AsyncTask<Long, Object, Object> deleteMutant = new AsyncTask<Long, Object, Object>() {
                        @Override
                        protected Object doInBackground(Long... params) {
                            databaseConnector.deleteMutant(params[0]);
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Object result) {
                            finish();
                        }
                    }
                    deleteMutant.execute(new Long[]{rowId});
                }
            });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
