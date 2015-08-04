package fragments;

import ia.hugo.silencetime.R;

import java.util.Collections;
import java.util.List;

import model.Sugestao;
import persistencia.SugestaoDao;
import utils.SugestoesAdapter;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SugestoesFragment extends ListFragment {
	
	
	public interface SugestoesFragmentInterface{
		public void setTempoDeEspera(Integer tempoDeEspera);
		public void changeTab(String tag);
	}
	
	private SugestoesFragmentInterface interfaceSugestoesFragment;
	
	
	private SugestoesAdapter adapterSugestoes;
	private List<Sugestao> listaSugestoes;
	private SugestaoDao daoSugestao;
	
	//listeners
	OnItemClickListener listenerSugestaoSelecionada = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			Sugestao mSugestao = daoSugestao.incrementarRepeticao(
					adapterSugestoes.getItem(position));
			
			listaSugestoes.set(position, mSugestao);
			adapterSugestoes.notifyDataSetChanged();
			interfaceSugestoesFragment.setTempoDeEspera(mSugestao.getTempoDeEspera());
			interfaceSugestoesFragment.changeTab("Tab2");
		}
	};

	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		try{
			interfaceSugestoesFragment = (SugestoesFragmentInterface) activity;
		}catch(ClassCastException c){
			throw new ClassCastException(activity.toString() + " deve implementar  SugestoesFragmentInterface ");
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		daoSugestao = new SugestaoDao(getActivity());
		listaSugestoes = daoSugestao.getSugestoes();
		Collections.sort(listaSugestoes);
		
		adapterSugestoes = new SugestoesAdapter(getActivity(), listaSugestoes);
		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.silenciar_sugestoes, container, false);
		setListAdapter(adapterSugestoes);
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getListView().setOnItemClickListener(listenerSugestaoSelecionada);
	}
	
	@Override
	public void onDestroy() {
		
		daoSugestao.decrementarTodos(listaSugestoes);
		
		super.onDestroy();
	}

}
