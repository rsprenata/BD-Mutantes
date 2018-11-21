package com.example.rsprenata.bd_mutantes;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    private Integer rowId;
    private TextView textViewNome;
    private ListView listViewHabilidades;
    private CursorAdapter skillsAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        listViewHabilidades = (ListView) findViewById(R.id.listViewHabilidades);

        rowId = getIntent().getIntExtra("row_id", 0);

        String[] from = new String[]{"habilidade"};
        int[] to = new int[]{R.id.mutantTextView};

        skillsAdapter = new SimpleCursorAdapter(DetailActivity.this, R.layout.activity_listar, null, from, to, 0);

        listViewHabilidades.setAdapter(skillsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new LoadMutantTask().execute(rowId);
        new LoadSkillsTask().execute(rowId);
    }

    private class LoadMutantTask extends AsyncTask<Integer, Object, Cursor> {
        DatabaseConnector databaseConnector = new DatabaseConnector(DetailActivity.this);

        @Override
        protected Cursor doInBackground(Integer... params) {
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

    private class LoadSkillsTask extends AsyncTask<Integer, Object, Cursor> {
        DatabaseConnector databaseConnector = new DatabaseConnector(DetailActivity.this);

        @Override
        protected Cursor doInBackground(Integer... params) {
            databaseConnector.open();
            return databaseConnector.getSkillsMutant(params[0]);
        }

        @Override
        protected void onPostExecute(Cursor result) {
            skillsAdapter.changeCursor(result);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.editItem:
                Intent intent = new Intent(DetailActivity.this, FormMutanteActivity.class);
                intent.putExtra("row_id", rowId);
                startActivity(intent);
                return true;
            case R.id.deleteItem:
                deleteMutant();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteMutant() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);

        builder.setTitle("Confirmação");
        builder.setMessage("Deseja excluir esse mutante?");
        builder.setPositiveButton("Excluir", 
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int button) {
                    final DatabaseConnector databaseConnector = new DatabaseConnector(DetailActivity.this);

                    AsyncTask<Integer, Object, Object> deleteMutant = new AsyncTask<Integer, Object, Object>() {
                        @Override
                        protected Object doInBackground(Integer... params) {
                            databaseConnector.deleteMutant(params[0]);
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Object result) {
                            finish();
                        }
                    };
                    deleteMutant.execute(new Integer[]{rowId});
                }
            });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
