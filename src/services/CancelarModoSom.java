package services;

import java.util.Calendar;

import model.TempoAgendamentoSugestao;
import broadcasts.IniciarNotificacao;

import persistencia.TASDao;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class CancelarModoSom extends IntentService {
	public static boolean DECREMENTAR = false;
	private TASDao daoTas;

	public CancelarModoSom(Context context) {
		super("CancelarModoSom");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onHandleIntent(intent);
		return START_NOT_STICKY;

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (DECREMENTAR){
			daoTas = new TASDao(getBaseContext());
			daoTas.open();

			TempoAgendamentoSugestao tas = new TempoAgendamentoSugestao();
			Bundle extras = intent.getExtras();
			
			tas.setDiaDaSemana(extras.getInt(IniciarNotificacao.DIA_DA_SEMANA_TAS, -1));
			tas.setHora(extras.getInt(IniciarNotificacao.HORA_TAS,-1));
			tas.setMinuto(extras.getInt(IniciarNotificacao.MINUTO_TAS,-1));
			
			Toast.makeText(getBaseContext(), "Cancelando a alteração para modo som", Toast.LENGTH_SHORT).show();
			
			if (daoTas.decrementarPeso(tas)){
				Log.i("CancelarModoSom", "Peso decrementado para o agendamento clickado");
			}
			daoTas.close();
		}
		stopSelf();

	}

}
