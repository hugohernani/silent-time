package ia.hugo.silencetime;

import java.util.Collections;
import java.util.List;

import model.Agendamento;
import persistencia.AgendamentoSilenciosoDao;
import utils.AgendamentosAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class ListaAgendamentosActivity extends ListActivity{
	
	private AgendamentoSilenciosoDao daoAgendamentos;
	
	private List<Agendamento> listaAgendamentos;
	private AgendamentosAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_agendamentos_activity);
		
		daoAgendamentos = new AgendamentoSilenciosoDao(this);
		daoAgendamentos.open();
		
		listaAgendamentos = daoAgendamentos.getAll();
		Collections.sort(listaAgendamentos);
		adapter = new AgendamentosAdapter(this, listaAgendamentos);
		
		setListAdapter(adapter);
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lista_agendamentos, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itApagarTodosAgendamentos:
			if (adapter.deleteAll()){
				Toast.makeText(this, "Todos os agendamentos foram apagados!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "N�o foi poss�vel apagar. Contate o criador para maiores informa��es", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}

