package persistencia;

import java.util.ArrayList;
import java.util.List;

import persistencia.Banco.EntryAgendamento;
import persistencia.Banco.EntrySugestao;
import model.Sugestao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SugestaoDao {
	
	private SQLiteDatabase database;
	private Banco.BancoOpenHelper sqliteOpenHelper;
	
	public SugestaoDao(Context context){
		sqliteOpenHelper = new Banco.BancoOpenHelper(context);
	}
	
	public void open(){
		database = sqliteOpenHelper.getWritableDatabase();
	}
	
	public void close(){
		sqliteOpenHelper.close();
	}
	
	public boolean create(Sugestao sugestao){
		int tempoDeEspera = sugestao.getTempoDeEspera();
		if (tempoDeEspera != 7 && tempoDeEspera != 14 && tempoDeEspera != 0){
			ContentValues values = new ContentValues();
			values.put(EntrySugestao.SUGESTOES_ITEM, tempoDeEspera);
			values.put(EntrySugestao.SUGESTOES_REPETICAO, sugestao.getRepeticao());
			
			return database.insert(EntrySugestao.TABLE_SUGESTOES, null, values)!=-1?true:false;
		}
		return false;
		
	}
	
	public Sugestao incrementarRepeticao(Sugestao sugestao){
		int repeticao = sugestao.getRepeticao();
		Sugestao mSugestao = sugestao;
		if (repeticao != 10){
			open();
			
			String where = EntrySugestao._ID + " = ?";
			String[] whereArgs = new String[]{
					String.valueOf(sugestao.getId())
			};
			
			Cursor cursor = database.query(EntrySugestao.TABLE_SUGESTOES, EntrySugestao.columns,
					where, whereArgs, null, null, null);
			
			if (cursor.getCount() != 0){
				mSugestao.setRepeticao(repeticao+1);
				update(sugestao);
			}
			close();
			cursor.close();
		}
		return mSugestao;
	}
	
	public Sugestao decrementarRepeticao(Sugestao sugestao){
		int repeticao = sugestao.getRepeticao();
		Sugestao mSugestao = sugestao;
		if (repeticao == 1){
			apagar(sugestao);
		}else{
			open();
			String where = EntrySugestao._ID + " = ?";
			String[] whereArgs = new String[]{
					String.valueOf(sugestao.getId())
			};
			
			Cursor cursor = database.query(EntrySugestao.TABLE_SUGESTOES, EntrySugestao.columns,
					where, whereArgs, null, null, null);
			
			if (cursor.getCount() != 0){
					mSugestao.setRepeticao(repeticao-1);
					update(mSugestao);
			}
			close();
			cursor.close();
		}
		return mSugestao;

	}
	
	private boolean update(Sugestao sugestao) {
		ContentValues values = new ContentValues();
		values.put(EntrySugestao.SUGESTOES_ITEM, sugestao.getTempoDeEspera());
		values.put(EntrySugestao.SUGESTOES_REPETICAO, sugestao.getRepeticao());
		
		String where = EntrySugestao._ID + " = ?";
		String[] whereArgs = new String[]{
				String.valueOf(sugestao.getId())
		};
		
		return database.update(EntrySugestao.TABLE_SUGESTOES, values, where, whereArgs)!=-1?true:false;
		
	}

	public ArrayList<Sugestao> getSugestoes(){
		open();
		ArrayList<Sugestao> lista = new ArrayList<Sugestao>();
		
		Cursor c = database.query(EntrySugestao.TABLE_SUGESTOES, EntrySugestao.columns,
				null, null, null, null, null);
		
		c.moveToFirst();
		if (!c.isAfterLast()){
			do{
				Sugestao sugestao = new Sugestao(
						c.getLong(0),
						c.getInt(1),
						c.getInt(2));
				lista.add(sugestao);
			}while(c.moveToNext());
		}
		
		close();
		c.close();
		return lista;
	}

	public List<Sugestao> decrementarTodos(List<Sugestao> listaSugestoes) {
		
		List<Sugestao> sugestoes = new ArrayList<Sugestao>();
		
		for (Sugestao sugestao : listaSugestoes) {
			sugestoes.add(
					decrementarRepeticao(sugestao)
					);
		}
		
		return sugestoes;
	}

	public boolean apagar(Sugestao sugestao) {
		open();
		String where = EntrySugestao._ID + " = ?";
		String whereArgs[] = new String[]{
				String.valueOf(sugestao.getId())
		};
		
		boolean result = false;
		if (database.delete(EntrySugestao.TABLE_SUGESTOES, where, whereArgs)!=0)
			result = true;
		else
			result = false;
		close();
		return result;
	}

	public boolean apagarTudo() {
		open();
		boolean resposta = false;
		if (database.delete(EntrySugestao.TABLE_SUGESTOES, null, null)!=0){
			resposta = true;
		}
		close();
		return resposta;
		
	}

}
