package broadcasts;

import java.util.Calendar;
import java.util.GregorianCalendar;

import model.TempoAgendamentoSugestao;

import persistencia.AgendamentoSilenciosoDao;
import persistencia.TASDao;

import ia.hugo.silencetime.R;
import services.AlarmesService;
import services.CancelarModoSom;
import services.CancelarModoVibratorio;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

public class IniciarNotificacao extends BroadcastReceiver {
	
	public static final String ID_INSTANCIA_EVENTO_AGENDAMENTO = "id_instancia_evento_agendamento";
	public static final String TITULO_AGENDAMENTO = "titulo_agendamento";
	public static final String DATA_AGENDAMENTO = "data_agendamento";
	public static final String DESCRICAO_AGENDAMENTO = "descricao_agendamento";
	public static final String ID_EVENTO_AGENDAMENTO = "id_evento_agendamento";
	
	/*
	 * Estas constantes são para captura destas
	 * informações nas classes CancelarModoSom
	 * e CancelarModoVibratorio
	 */
	public static final String DIA_DA_SEMANA_TAS = "DIA_DA_SEMANA_TAS";
	public static final String HORA_TAS = "HORA_TAS";
	public static final String MINUTO_TAS = "MINUTO_TAS";


	private Notification.Builder builderNotification;
	private NotificationManager notificationManager;
	
	private TASDao daoTas;
	private AgendamentoSilenciosoDao daoAgendamento;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		daoTas = new TASDao(context);
		daoAgendamento = new AgendamentoSilenciosoDao(context);
		
		Log.i(IniciarNotificacao.class.getName(), "Iniciando o onReceive de " +
				IniciarNotificacao.class.getName());
		
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PARTIAL_WAKE_LOCK");
        //Acquire the lock
        wl.acquire();
        
    	Bundle extras = intent.getExtras();
    	Long id_instancia_evento_agendamento = extras.getLong(ID_INSTANCIA_EVENTO_AGENDAMENTO, -1L);
    	Long id_evento = extras.getLong(ID_EVENTO_AGENDAMENTO,-1L);
    	String titulo_agendamento = extras.getString(TITULO_AGENDAMENTO, "");
    	String descricao = extras.getString(DESCRICAO_AGENDAMENTO, "");
    	Long data_agendamento = extras.getLong(DATA_AGENDAMENTO, -1L);
        
        if (intent.getBooleanExtra(AlarmesService.CRIAR_NOTIFICACAO_PARA_VIBRACAO, Boolean.FALSE)){
        	        	
        	createNotificationVibrate(context, id_instancia_evento_agendamento,
					id_evento, titulo_agendamento, descricao,
					data_agendamento);

        }else{
        	
			createNotificationSound(context, id_instancia_evento_agendamento,
			id_evento, titulo_agendamento, descricao,
			data_agendamento);
        }
	}
	
	private void createNotificationVibrate(final Context context,
			final Long id_instancia, final Long id_evento, final String titulo,
			final String descricao, final Long time) {
		
		//
		Calendar calendarChegado = new GregorianCalendar();
		calendarChegado.setTimeInMillis(time);
		
		final Integer dia_da_semana = calendarChegado.get(Calendar.DAY_OF_WEEK);
		final Integer hora = calendarChegado.get(Calendar.HOUR_OF_DAY);
		final Integer minuto = calendarChegado.get(Calendar.MINUTE);

		Intent i = new Intent(context, CancelarModoVibratorio.class);
		i.putExtra(DIA_DA_SEMANA_TAS, dia_da_semana);
		i.putExtra(HORA_TAS, hora);
		i.putExtra(MINUTO_TAS, minuto);
		
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		
		builderNotification = new Notification.Builder(context)
				.setContentTitle(titulo)
				.setContentText("Entrará no modo vibratório em alguns segundos")
				.setSmallIcon(R.drawable.olho_humano)
				.setContentIntent(pi)
				.setDefaults(Notification.DEFAULT_SOUND)
				.setAutoCancel(true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				final int idNotificacao = id_instancia.intValue() + time.intValue();

				notificationManager.notify(idNotificacao, builderNotification.getNotification());
				builderNotification.setDefaults(Notification.DEFAULT_LIGHTS);

				try{
					Thread.sleep(16000);
					
					builderNotification.setContentText("Entrando no modo vibratório...");
					notificationManager.notify(idNotificacao, builderNotification.getNotification());

					Thread.sleep(2000);

					AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					
					CancelarModoVibratorio.DECREMENTAR = false;
					notificationManager.cancel(idNotificacao);
					
					/*
					 * Criará uma sugestão de dia e horário.
					 * Caso exista, será incrementado
					 */
					TempoAgendamentoSugestao tas = new TempoAgendamentoSugestao();
					
					tas.setTitulo(titulo);
					tas.setDescricao(descricao);
					tas.setDiaDaSemana(dia_da_semana);
					tas.setHora(hora);
					tas.setMinuto(minuto);
					
					daoTas.create(tas);

				}catch(InterruptedException i){
					Log.i("ErroNotification", "Notification can't sleep. Sleep failure");
					i.printStackTrace();
				}

			}
		}).start();
	}

	private void createNotificationSound(final Context context,
			final Long id_instancia_evento, final Long id_evento, final String titulo,
			final String descricao, final Long time) {

		Calendar calendarChegado = new GregorianCalendar();
		calendarChegado.setTimeInMillis(time);

		final Integer dia_da_semana = calendarChegado.get(Calendar.DAY_OF_WEEK);
		final Integer hora = calendarChegado.get(Calendar.HOUR_OF_DAY);
		final Integer minuto = calendarChegado.get(Calendar.MINUTE);

		Intent i = new Intent(context, CancelarModoSom.class);
		i.putExtra(DIA_DA_SEMANA_TAS, dia_da_semana);
		i.putExtra(HORA_TAS, hora);
		i.putExtra(MINUTO_TAS, minuto);
		
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		
		builderNotification = new Notification.Builder(context)
				.setContentTitle(titulo)
				.setContentText("Entrará no modo som em alguns segundos")
				.setSmallIcon(R.drawable.aprendendo)
				.setContentIntent(pi)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setAutoCancel(true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				final int idNotificacao = id_instancia_evento.intValue() + time.intValue();

				notificationManager.notify(idNotificacao, builderNotification.getNotification());
				builderNotification.setDefaults(Notification.DEFAULT_LIGHTS);
								
				try{
					Thread.sleep(16000);
					
					builderNotification.setContentText("Entrando no modo som...");
					notificationManager.notify(idNotificacao, builderNotification.getNotification());
					
					Thread.sleep(2000);

					AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					
					CancelarModoSom.DECREMENTAR = false;
					notificationManager.cancel(idNotificacao);

					/*
					 * Criará uma sugestão de dia e horário.
					 * Caso exista, será incrementado
					 */
					TempoAgendamentoSugestao tas = new TempoAgendamentoSugestao();
					
					tas.setTitulo(titulo);
					tas.setDescricao(descricao);
					tas.setDiaDaSemana(dia_da_semana);
					tas.setHora(hora);
					tas.setMinuto(minuto);
					
					daoAgendamento.deleteByIdAndIdEvent(id_instancia_evento, id_evento);
					
				}catch(InterruptedException i){
					Log.i("ErroNotification", "Notification can't sleep. Sleep failure");
					i.printStackTrace();
				}
			}
				
		}).start();
	}

}
