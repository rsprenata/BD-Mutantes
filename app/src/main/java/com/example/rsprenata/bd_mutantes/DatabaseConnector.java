package com.example.rsprenata.bd_mutantes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
		database.update("mutantes", editMutant, "id=" + mutante.getId(), null);
		close();
	}

	public Cursor getAllMutants() {
		return database.query("mutantes", new String[]{"id", "nome"}, null, null, null, null, "nome");
	}

	public Cursor getOneMutant(Integer id) {
		return database.query("mutantes", null, "id=" + id, null, null, null, null);
	}

	public void deleteMutant(Integer id) {
		open();
		database.delete("mutantes", "id=" + id, null);
	}

	private class DatabaseOpenHelper extends SQLiteOpenHelper {
		private static final String CREATE_QUERY = "CREATE TABLE mutantes (id integer primary key autoincrement, nome TEXT);";
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
