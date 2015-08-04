package persistencia;

import java.util.ArrayList;
import java.util.List;

import persistencia.Banco.EntryTAS;
import model.TempoAgendamentoSugestao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TASDao {

	private SQLiteDatabase database;
	private Banco.BancoOpenHelper sqliteOpenHelper;
	
	public TASDao(Context context){
		sqliteOpenHelper = new Banco.BancoOpenHelper(context);
	}
	
	public void open(){
		database = sqliteOpenHelper.getWritableDatabase();
	}
	
	public void close(){
		sqliteOpenHelper.close();
	}
	
	public boolean create(TempoAgendamentoSugestao TAS){
		open();
		
		boolean resposta = false;
		
		int dia_da_semana = TAS.getDiaDaSemana();
		int hora = TAS.getHora();
		int minuto = TAS.getMinuto();
				
		if (!existByDiaAndHoraAndMinuto(dia_da_semana, hora, minuto)){
			
			Log.i("TASDao", "Tas será criado para o dia " + dia_da_semana + " às " + hora + ":" + minuto);
			
			int unidadeMinuto = minuto/10;
			minuto = minuto - unidadeMinuto;
			
			ContentValues values = new ContentValues();
			values.put(EntryTAS.TAS_TITULO, TAS.getTitulo());
			values.put(EntryTAS.TAS_DESCRICAO, TAS.getDescricao());
			values.put(EntryTAS.TAS_DIA_DA_SEMANA, dia_da_semana);
			values.put(EntryTAS.TAS_HORA, hora);
			values.put(EntryTAS.TAS_MINUTO, minuto);
			values.put(EntryTAS.TAS_PESO, TAS.getPeso());
			
			resposta = database.insert(EntryTAS.TABLE_TEMPO_AGENDAMENTO_SUGESTAO,
					null, values)!=-1?true:false;
		}else{
			Log.i("TASDao", "Tas já existe, portanto será incrementado para o dia " + dia_da_semana + " às " + hora + ":" + minuto);
			if (incrementarPeso(TAS)){
				Log.i("TASDao", "Foi incrementado");
			}
		}
		close();
		return resposta;
	}
	
	private boolean existByDiaAndHoraAndMinuto(int dia_da_semana, int hora,
			int minuto) {
		
		int unidadeMinuto = minuto/10;
		minuto = minuto - unidadeMinuto;
		
		String where = EntryTAS.TAS_DIA_DA_SEMANA + " = ? AND "
				+ EntryTAS.TAS_HORA + " = ? AND "
				+ EntryTAS.TAS_MINUTO + " = ?";
		
		String []whereArgs = new String[]{
				String.valueOf(dia_da_semana),
				String.valueOf(hora),
				String.valueOf(minuto)
		};
		
		Cursor cursor = database.query(EntryTAS.TABLE_TEMPO_AGENDAMENTO_SUGESTAO,
				EntryTAS.columns, where, whereArgs, null, null, null);
		
		return cursor.getCount()!=0?true:false;
	}

	public boolean updateByDiaAndHoraAndMinuto(TempoAgendamentoSugestao TAS){
		ContentValues values = new ContentValues();
		values.put(EntryTAS.TAS_TITULO, TAS.getTitulo());
		values.put(EntryTAS.TAS_DESCRICAO, TAS.getDescricao());
		values.put(EntryTAS.TAS_DIA_DA_SEMANA, TAS.getDiaDaSemana());
		values.put(EntryTAS.TAS_PESO, TAS.getPeso());
		
		String where = EntryTAS.TAS_HORA + " = ? AND "
				+ EntryTAS.TAS_MINUTO + " = ? AND "
				+ EntryTAS.TAS_DIA_DA_SEMANA + " = ?";
		String []whereArgs = new String[]{
				String.valueOf(TAS.getHora()),
				String.valueOf(TAS.getMinuto()),
				String.valueOf(TAS.getDiaDaSemana())
		};
		
		boolean resposta = false;
		if(database.update(EntryTAS.TABLE_TEMPO_AGENDAMENTO_SUGESTAO,
				values, where, whereArgs)!=-1)
			resposta = true;
		close();
		return resposta;
	}
	
	public boolean incrementarPeso(TempoAgendamentoSugestao TAS){
		open();
		int pesoAtual = TAS.getPeso();
		boolean resposta = false;
		if (pesoAtual <= 3){
			TAS.setPeso(pesoAtual+1);
			resposta = updateByDiaAndHoraAndMinuto(TAS);
		}
		close();
		return resposta;
	}
	
	public boolean decrementarPeso(TempoAgendamentoSugestao TAS){
		open();
		int pesoAtual = TAS.getPeso();
		boolean resposta = false;
		if (pesoAtual > 0){
			TAS.setPeso(pesoAtual-1);
			resposta = updateByDiaAndHoraAndMinuto(TAS);
		}
		close();
		return resposta;
	}
	
	public List<TempoAgendamentoSugestao> getAll(){
		open();
		List<TempoAgendamentoSugestao> lista = new ArrayList<TempoAgendamentoSugestao>();
		
		Cursor cursor = database.query(EntryTAS.TABLE_TEMPO_AGENDAMENTO_SUGESTAO,
				EntryTAS.columns, null, null, null, null, null);
		
		Log.i("CursorTASDao", "Quantidade de sugestões de tempo para agendamento: " + cursor.getCount());
		
		cursor.moveToFirst();
		if (!cursor.isAfterLast()){
			do{
				TempoAgendamentoSugestao TAS = new TempoAgendamentoSugestao(
						cursor.getLong(0), cursor.getString(1),
						cursor.getString(2), cursor.getInt(3),
						cursor.getInt(4), cursor.getInt(5),
						cursor.getInt(6));
				lista.add(TAS);
			}while(cursor.moveToNext());
		}else{
			Log.i("CursorTASDao", "O cursor está vazio");
		}
		cursor.close();
		close();
		
		return lista;
		
	}
	
	public boolean deleteById(Long id){
		open();
		
		String where = EntryTAS._ID + " = ?";
		String []whereArgs = new String[]{
				String.valueOf(id)
		};
		
		boolean resposta = false;
		if (database.delete(EntryTAS.TABLE_TEMPO_AGENDAMENTO_SUGESTAO, where, whereArgs)!=0){
			resposta = true;
		}
		close();
		return resposta;
		
	}
	
	public boolean deleteAll(){
		open();
		boolean resposta = false;
		if (database.delete(EntryTAS.TABLE_TEMPO_AGENDAMENTO_SUGESTAO, null, null)!=0)
			resposta = true;
		close();
		return resposta;
	}

	public boolean decrementarTodos() {
		List<TempoAgendamentoSugestao> lista = getAll();
		
		Integer qnt = lista.size();
		Integer count = 0;
		
		for (TempoAgendamentoSugestao tempoAgendamentoSugestao : lista) {
			if (decrementarPeso(tempoAgendamentoSugestao)){
				count += 1;
			}
		}
		
		return qnt==count?true:false;
	}
	
	public List<TempoAgendamentoSugestao> getMelhoresSugestoes(){
		open();
		List<TempoAgendamentoSugestao> lista = new ArrayList<TempoAgendamentoSugestao>();
		
		String where = EntryTAS.TAS_PESO + " >= ?";
		String []whereArgs = new String[]{
				String.valueOf(3)
		};
		
		Cursor cursor = database.query(EntryTAS.TABLE_TEMPO_AGENDAMENTO_SUGESTAO,
				EntryTAS.columns, where, whereArgs, null, null, null);
		
		Log.i("CursorTASDao", "Quantidade de sugestões de tempo para agendamento: " + cursor.getCount());
		
		cursor.moveToFirst();
		if (!cursor.isAfterLast()){
			do{
				TempoAgendamentoSugestao TAS = new TempoAgendamentoSugestao(
						cursor.getLong(0), cursor.getString(1),
						cursor.getString(2), cursor.getInt(3),
						cursor.getInt(4), cursor.getInt(5),
						cursor.getInt(6));
				lista.add(TAS);
			}while(cursor.moveToNext());
		}else{
			Log.i("CursorTASDao", "O cursor está vazio");
		}
		cursor.close();
		close();
		
		return lista;

	}

	public TempoAgendamentoSugestao getTasbyId(Long id) {
		open();
		TempoAgendamentoSugestao tas = null;
		
		String where = EntryTAS._ID + " = ?";
		String []whereArgs = new String[]{
				String.valueOf(id)
		};
		
		Cursor cursor = database.query(EntryTAS.TABLE_TEMPO_AGENDAMENTO_SUGESTAO,
				EntryTAS.columns, where, whereArgs, null, null, null);
		
		cursor.moveToFirst();
		if (!cursor.isAfterLast()){
			tas = new TempoAgendamentoSugestao(
					cursor.getLong(0), cursor.getString(1),
					cursor.getString(2), cursor.getInt(3),
					cursor.getInt(4), cursor.getInt(5),
					cursor.getInt(6));
		}
		close();
		cursor.close();
		
		return tas;
	}

	
}
