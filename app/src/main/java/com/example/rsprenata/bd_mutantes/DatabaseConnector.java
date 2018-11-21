package com.example.rsprenata.bd_mutantes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
	private static final String DATABASE_NAME = "db_mutantes";
	private SQLiteDatabase database;
	private DatabaseOpenHelper databaseOpenHelper;

	public DatabaseConnector(Context context) {
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
	}

	public void open() throws SQLException {
		database = databaseOpenHelper.getWritableDatabase();
	}

	public void close() {
		if (database != null) {
			database.close();
		}
	}

	public void insertMutante(Mutante mutante) {
		ContentValues newMutant = new ContentValues();

		newMutant.put("nome", mutante.getNome());

		open();

		int novoId = (int) database.insert("mutantes", null, newMutant);

		mutante.setId(novoId);

		close();
	}

	public void insertHabilidades(Integer mutante_id, List<String> habilidades) {
		ContentValues newSkills = new ContentValues();

		open();
		
		for (String s : habilidades) {
            newSkills.put("habilidade", s);
			newSkills.put("mutante_id", mutante_id);
			database.insert("habilidades", null, newSkills);
		}

		close();
	}

	public void updateMutant(Mutante mutante) {
		ContentValues editMutant = new ContentValues();
		editMutant.put("nome", mutante.getNome());

		open();
		database.update("mutantes", editMutant, "_id=" + mutante.getId(), null);
		close();
	}

	public Cursor getAllMutants() {
		return database.query("mutantes", new String[]{"_id", "nome"}, null, null, null, null, "nome");
	}

    public List<String> getNomeMutantesByName(String nome) {
        Cursor cursor =  database.query("mutantes", new String[]{"_id", "nome"}, "nome like '%"+nome+"%'", null, null, null, "nome");
        List<String> nomeMutantes = new ArrayList<String>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                nomeMutantes.add(cursor.getString(cursor.getColumnIndex("nome")));
                cursor.moveToNext();
            }

            cursor.close();
        }

        return nomeMutantes;
    }

    public List<String> getNomeMutantesByHabilidade(String habilidade) {
        Cursor cursor =  database.rawQuery("SELECT m.nome FROM mutantes m JOIN habilidades h ON h.mutante_id = m._id WHERE h.habilidade like '%"+habilidade+"%'", null);
        List<String> nomeMutantes = new ArrayList<String>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                nomeMutantes.add(cursor.getString(cursor.getColumnIndex("nome")));
                cursor.moveToNext();
            }

            cursor.close();
        }

        return nomeMutantes;
    }

	public Cursor getOneMutant(Integer id) {
		return database.query("mutantes", null, "_id=" + id, null, null, null, null);
	}

	public Mutante carregarMutante(Integer id) {
	    Mutante mutante = null;
        open();
	    Cursor cursor = getOneMutant(id);
	    if (cursor.getCount() > 0) {
	        mutante = new Mutante();
	        List<String> habilidades = new ArrayList<String>();
            cursor.moveToFirst();

            int nomeIndex = cursor.getColumnIndex("nome");
            int idIndex = cursor.getColumnIndex("_id");
            mutante.setId(cursor.getInt(idIndex));
            mutante.setNome(cursor.getString(nomeIndex));

            cursor = getSkillsMutant(cursor.getInt(idIndex));

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                habilidades.add(cursor.getString(cursor.getColumnIndex("habilidade")));
                cursor.moveToNext();
            }
            mutante.setHabilidades(habilidades);

            cursor.close();
        }
        close();

	    return mutante;
    }

    public Cursor getSkillsMutant(Integer mutante_id) {
        return database.query("habilidades", new String[]{"mutante_id as _id", "habilidade"}, "mutante_id=" + mutante_id, null, null, null, null);
    }

	public void deleteMutant(Integer id) {
		open();
		database.delete("mutantes", "_id=" + id, null);
	}

    public void deleteSkills(Integer mutante_id) {
        open();
        database.delete("habilidades", "mutante_id=" + mutante_id, null);
    }


    private class DatabaseOpenHelper extends SQLiteOpenHelper {
		private static final String CREATE_QUERY = "CREATE TABLE mutantes (_id integer primary key autoincrement, nome TEXT);";
        private static final String CREATE_QUERY2 = "CREATE TABLE habilidades (mutante_id integer, habilidade TEXT);";

		public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		    db.execSQL(CREATE_QUERY);
            db.execSQL(CREATE_QUERY2);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	}
}
