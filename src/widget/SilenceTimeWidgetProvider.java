package widget;

import ia.hugo.silencetime.R;
import ia.hugo.silencetime.SilenciarOpcoes;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class SilenceTimeWidgetProvider extends AppWidgetProvider {
	
	private static final String decrementarDia = "decrementarDia";
	private static final String diaRestantePrefs = "diaRestante";
	private static final String mensagemConstante = "Em sil�ncio durante %d dias";
	
	private static final String namePreferences = "SilenceTimeWidgetProviderPreferences";
	private static SharedPreferences prefs;
	private static Editor editor;
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		
//		prefs = context.getSharedPreferences(namePreferences, Context.MODE_PRIVATE);
//		editor = prefs.edit();
		
		Log.i("WidgetEnable", "Widget is enable");
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
		
		views.setOnClickPendingIntent(R.id.btSilenciar, buildButtonPendingIntent(context));
//		views.setTextViewText(R.id.tVPeriodoEmSilencio, String.format(mensagemConstante, 0));
		
	}
	
	public PendingIntent buildButtonPendingIntent(Context context){
		Log.i("WidgetAction", "Clickado");
		Intent intent = new Intent(context,SilenciarOpcoes.class);
		return PendingIntent.getActivity(context, 0, intent, 0);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
//		prefs = context.getSharedPreferences(namePreferences, Context.MODE_PRIVATE);
//		editor = prefs.edit();

//		Boolean decrementar = prefs.getBoolean(decrementarDia, false);
//		Integer diaRestante = prefs.getInt(diaRestantePrefs, 0);
		
//		if (decrementar && diaRestante != 0) diaRestante--;
		
		for (int i = 0; i < appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
			
			views.setOnClickPendingIntent(R.id.btSilenciar, buildButtonPendingIntent(context));
			
//			views.setTextViewText(R.id.tVPeriodoEmSilencio, String.format(mensagemConstante, diaRestante));
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	public static void onUpdateMessage(Context appContext, Integer dia, Boolean AGENDAMENTOS_CRIADOS){
		prefs = appContext.getSharedPreferences(namePreferences, Context.MODE_PRIVATE);
		editor = prefs.edit();
		
		if (AGENDAMENTOS_CRIADOS){
			editor.putInt(diaRestantePrefs, dia);
			editor.putBoolean(decrementarDia, true);
			editor.commit();
		}else{
			editor.putInt(diaRestantePrefs, 0);
			editor.putBoolean(decrementarDia, false);
			editor.commit();
		}
		pushWidgetUpdate(appContext);
	}
	
	public static void pushWidgetUpdate(Context appContext){
		ComponentName myWidget = new ComponentName(appContext, SilenceTimeWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(appContext);
		
		RemoteViews remoteViews = new RemoteViews(appContext.getPackageName(), R.layout.main);
		
//		Integer diasRestantes = prefs.getInt(diaRestantePrefs, 0);
		
//		remoteViews.setTextViewText(R.id.tVPeriodoEmSilencio, String.format(mensagemConstante, diasRestantes));
		
		manager.updateAppWidget(myWidget, remoteViews);
	}	
}