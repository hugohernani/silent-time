package broadcasts;

import ia.hugo.silencetime.ListaSugestoesAgendamentoActivity;
import ia.hugo.silencetime.R;
import model.TempoAgendamentoSugestao;
import services.AlarmesService;
import services.CancelarModoVibratorio;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.PowerManager;
import android.util.Log;

public class PerceberBoot extends BroadcastReceiver{
	
	private SharedPreferences prefs;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("PerceberBoot", "Receiver iniciado pós-inicialização do sistema operacional");
		
		long timeInMillis = prefs.getLong(AlarmesService.CALENDAR_TIME_IN_MILLIS, -1L);
		
		if (timeInMillis != -1){
			Log.i("PerceberBoot", "Tempo válido. Iniciará o serviço AlarmesService");
			Intent serviceIntent = new Intent(context, AlarmesService.class);
			context.startService(serviceIntent);
		}
		
	}
	

}