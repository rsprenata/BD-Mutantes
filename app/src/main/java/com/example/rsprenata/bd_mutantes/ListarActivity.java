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

public class ListarActivity extends ListActivity {
    private List<Mutante> mutantes;
    private CursorAdapter mutantAdapter;
    private ListView mutantListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mutantListView = getListView();
        mutantListView.setOnItemClickListener(viewMutantListener);

        String[] from = new String[]{"nome"};
        int[] to = new int[]{R.id.mutantTextView};

        mutantAdapter = new SimpleCursorAdapter(ListarActivity.this, R.layout.activity_listar, null, from, to, 0);

        setListAdapter(mutantAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetMutantsTask().execute((Object[]) null);
    }

    @Override
    protected void onStop() {
        Cursor cursor = mutantAdapter.getCursor();

        if (cursor != null)
            cursor.moveToFirst();

        mutantAdapter.changeCursor(null);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_listar, menu);
        return true;
    }

    OnItemClickListener viewMutantListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent viewMutant = new Intent(ListarActivity.this, DetailMutant.class);
            viewMutant.putExtra("row_id", arg3);
            startActivity(viewMutant);
        }
    }

    private class GetMutantsTask extendes AsyncTask<Object, Object, Cursor> {
        DatabaseConnector databaseConnector = new DatabaseConnector(ListarActivity.this);

        @Override
        proteced Cursor doInBackground(Object... params) {
            databaseConnector.open();
            return databaseConnector.getAllMutants();
        }
        @Override
        protected void onPostExecute(Cursor result) {
            mutantAdapter.changeCursor(result);
            databaseConnector.close();
        }
    }
}
