package services;

import ia.hugo.silencetime.R;

import java.util.ArrayList;
import java.util.List;

import broadcasts.IniciarNotificacao;

import model.Agendamento;
import persistencia.EventsAndroid;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmesService extends IntentService{
	
	public final static String NAME_PREFERENCES_APPLICATION = "PREFERENCES_APPLICATION";
	
	public static final String CRIAR_NOTIFICACAO_PARA_VIBRACAO = "istoVibrate";
	public static final String CALENDAR_TIME_IN_MILLIS = "calendarTimeInMillis";

	public static Boolean AGENDAMENTOS_CRIADOS = false;
	
	private SharedPreferences prefs;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.w("alarmService-StartCommand", "StartComand starting...");
		
		onHandleIntent(intent);
		return START_STICKY;
	}
	
	public AlarmesService() {
		super("AlarmesService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.w("alarmService-onHandleIntent", "Iniciando o onHandleIntent de AlarmesService");
		prefs = getSharedPreferences(NAME_PREFERENCES_APPLICATION, Context.MODE_PRIVATE);
		
		long timeInMillis = prefs.getLong(CALENDAR_TIME_IN_MILLIS, -1L);
		
		if (timeInMillis != -1L){
			Log.i("alarmService-onHandleIntent", "Tempo capturado corretamente. Dando continuidado ao serviço");
			EventsAndroid events = new EventsAndroid(getBaseContext());
			
			// captura os eventos e armazena na lista de objetos Agendamento
			ArrayList<Agendamento> lista = events.getEvents(timeInMillis);
			
			if(lista.isEmpty()){
				AGENDAMENTOS_CRIADOS = false;
				Toast.makeText(getBaseContext(), "Nao há agendamentos a adicionar para este(s) dia(s)!", Toast.LENGTH_LONG).show();
			}else{
				
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				
				Log.i("alarmService-onHandleIntent", "As " + lista.size()*2 + " notificações serão programadas agora");
				
				for (final Agendamento agendamento : lista) {
					Intent intentNotificacaoVibrar = new Intent(AlarmesService.this, IniciarNotificacao.class);
					
					intentNotificacaoVibrar.putExtra(CRIAR_NOTIFICACAO_PARA_VIBRACAO, Boolean.TRUE);
					intentNotificacaoVibrar.putExtra(IniciarNotificacao.ID_INSTANCIA_EVENTO_AGENDAMENTO, agendamento.getId());
					intentNotificacaoVibrar.putExtra(IniciarNotificacao.ID_EVENTO_AGENDAMENTO, agendamento.getId_evento());
					intentNotificacaoVibrar.putExtra(IniciarNotificacao.TITULO_AGENDAMENTO, agendamento.getTitulo());
					intentNotificacaoVibrar.putExtra(IniciarNotificacao.DESCRICAO_AGENDAMENTO, agendamento.getDescricao());
					intentNotificacaoVibrar.putExtra(IniciarNotificacao.DATA_AGENDAMENTO, agendamento.getInicio());

					PendingIntent piVibrar = PendingIntent.getBroadcast(getBaseContext(), agendamento.getInicio().intValue(), intentNotificacaoVibrar, PendingIntent.FLAG_CANCEL_CURRENT);

					alarmManager.set(AlarmManager.RTC_WAKEUP, agendamento.getInicio(), piVibrar);
			        
			        Intent intentNotificacaoSom = new Intent(AlarmesService.this, IniciarNotificacao.class);
			        
			        intentNotificacaoSom.putExtra(CRIAR_NOTIFICACAO_PARA_VIBRACAO, Boolean.FALSE);
					intentNotificacaoSom.putExtra(IniciarNotificacao.ID_INSTANCIA_EVENTO_AGENDAMENTO, agendamento.getId());
					intentNotificacaoSom.putExtra(IniciarNotificacao.ID_EVENTO_AGENDAMENTO, agendamento.getId_evento());
					intentNotificacaoSom.putExtra(IniciarNotificacao.TITULO_AGENDAMENTO, agendamento.getTitulo());
					intentNotificacaoSom.putExtra(IniciarNotificacao.DESCRICAO_AGENDAMENTO, agendamento.getDescricao());
					intentNotificacaoSom.putExtra(IniciarNotificacao.DATA_AGENDAMENTO, agendamento.getFim());
					
					PendingIntent piSom = PendingIntent.getBroadcast(getBaseContext(), agendamento.getFim().intValue(), intentNotificacaoSom, PendingIntent.FLAG_CANCEL_CURRENT);
			        alarmManager.set(AlarmManager.RTC_WAKEUP, agendamento.getFim(), piSom);

			        
				}
				AGENDAMENTOS_CRIADOS = true;
				
				Toast.makeText(getApplicationContext(), "Agendamentos criados", Toast.LENGTH_SHORT).show();

			}
		}
		stopSelf();
	}
}