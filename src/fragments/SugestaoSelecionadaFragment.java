package fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import model.Agendamento;
import model.TempoAgendamentoSugestao;
import ia.hugo.silencetime.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SugestaoSelecionadaFragment extends DialogFragment {
	
	public interface SugestaoSelecionadaInterface{
		public TempoAgendamentoSugestao getTas();
		public void onNegativeClick(DialogFragment dialog);
		public void onNeutralClick(DialogFragment dialog);
		public void onPositiveClick(DialogFragment dialog, final String titulo,
				final String descricao,	final Long timeInicial,
				final Long timeFinal, Integer qntRepeticoes);
	}
	
	private SugestaoSelecionadaInterface interfaceSugestaoSelecionada;
	private HashMap<String, Integer> hmMesCalendar = new HashMap<String, Integer>(12);
	private ArrayAdapter<Integer> diasAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		
		try{
			interfaceSugestaoSelecionada = (SugestaoSelecionadaInterface)
					activity;
		}catch(ClassCastException c){
			Log.i(SugestaoSelecionadaFragment.class.getName(),
					"A activity " + activity.getClass().getName() +
					" deve implementar 'interfaceSugestaoSelecionada'");
			c.printStackTrace();
		}
		
		super.onAttach(activity);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
				
		View view_click_sugestao = inflater.inflate(R.layout.alert_dialog_view_sugestao_agendamento,
				null);
		builder.setView(view_click_sugestao);
		
		final EditText etTitulo = (EditText) view_click_sugestao.findViewById(R.id.etTituloDialogTAS);
		final EditText etDescricao = (EditText) view_click_sugestao.findViewById(R.id.etDescricaoDialogTAS);
		final TextView tvDia = (TextView) view_click_sugestao.findViewById(R.id.tvDiaDialogTas);
		final Spinner spMes = (Spinner) view_click_sugestao.findViewById(R.id.spMesDialogTas);		
		final TextView tvAno = (TextView) view_click_sugestao.findViewById(R.id.tvAnoDialogTas);
		
		final Spinner spDia = (Spinner) view_click_sugestao.findViewById(R.id.spDiaDialogTas);
		
		final EditText etQntRepeticao = (EditText) view_click_sugestao.findViewById(R.id.etQntRepeticaoDialogTas);

		
		/*
		 * Inicializa��es
		 */
		final TempoAgendamentoSugestao tas = interfaceSugestaoSelecionada.getTas();
		etTitulo.setText(tas.getTitulo());
		etDescricao.setText(tas.getDescricao());
		
		/*
		 * Tratamento do campo Dia e do Spinner para o dia
		 * Inicio
		 */
		final Calendar calendarInicio = new GregorianCalendar();
		calendarInicio.set(Calendar.DAY_OF_WEEK, tas.getDiaDaSemana());
		
		// Setando o m�s e o ano
		spMes.setSelection(calendarInicio.get(Calendar.MONTH));
		tvAno.setText(String.valueOf(calendarInicio.get(Calendar.YEAR)));
		
		// Continuando
		final ArrayList<Integer> listaDiasMes = new ArrayList<Integer>();
		Integer d = calendarInicio.get(Calendar.DAY_OF_MONTH);
		Integer maximoMes = calendarInicio.getActualMaximum(Calendar.DAY_OF_MONTH);
		Toast.makeText(getActivity(), "M�ximo do m�s: " + maximoMes, Toast.LENGTH_SHORT).show();
		listaDiasMes.add(d);
		while(d <= maximoMes){
			d += 7;
			listaDiasMes.add(d);
		}
		diasAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_list_item_1, listaDiasMes);
		spDia.setAdapter(diasAdapter);
		spDia.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View arg1,
					int position, long arg3) {
				calendarInicio.set(Calendar.DAY_OF_WEEK, diasAdapter.getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
				Toast.makeText(getActivity(), "Voc� precisa selecionar um dia", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		// Fim
		
		spMes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				calendarInicio.set(Calendar.MONTH, position); // a posi��o equivale ao m�s
				calendarInicio.set(Calendar.DAY_OF_WEEK, tas.getDiaDaSemana());
				atualizarListaDias(tas.getDiaDaSemana(), calendarInicio.getActualMaximum(Calendar.DAY_OF_MONTH),
						listaDiasMes);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});

		

		builder.setTitle("Sugest�o de Agendamento")
		.setNegativeButton("Apagar", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				interfaceSugestaoSelecionada.onNegativeClick(SugestaoSelecionadaFragment.this);
			}
		});
		builder.setNeutralButton("Cancelar", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				interfaceSugestaoSelecionada.onNeutralClick(SugestaoSelecionadaFragment.this);
			}
		});
		builder.setPositiveButton("Usar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String titulo = etTitulo.getEditableText().toString();
				String descricao = etDescricao.getEditableText().toString();
				
				Integer repeticoes = Integer.valueOf(etQntRepeticao.getEditableText().toString()
						);
				
				Calendar calendarioTermino = calendarInicio;
				
				calendarioTermino.add(Calendar.HOUR_OF_DAY, 2);
				
				if (!titulo.isEmpty()){
						interfaceSugestaoSelecionada.onPositiveClick(SugestaoSelecionadaFragment.this,
								titulo, descricao, calendarInicio.getTimeInMillis(), calendarioTermino.getTimeInMillis(),
								repeticoes);
				}else{
					Toast.makeText(getActivity(), "Algo n�o foi corretamente preenchido.", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		return builder.create();
	}
	
	private void inicializarHashMapMesCalendar(){
		String []meses = getResources().getStringArray(R.array.mes_ano);
		
		for (int i = 0; i < meses.length; i++) {
			hmMesCalendar.put(meses[i], i);
		}

	}
	
	private int getMesCorrespondente(String mesPorExtenso) {
		return hmMesCalendar.get(mesPorExtenso);
	}
	
	private void atualizarListaDias(Integer dia, Integer maximoMes, ArrayList<Integer> listaDiasMes) {
		listaDiasMes.clear();
		listaDiasMes.add(dia);
		while(dia <= maximoMes){
			dia += 7;
			listaDiasMes.add(dia);
		}
		diasAdapter.notifyDataSetChanged();
		
	}
}