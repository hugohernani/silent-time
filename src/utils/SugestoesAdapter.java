package utils;

import ia.hugo.silencetime.R;

import java.util.List;

import persistencia.SugestaoDao;

import model.Sugestao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SugestoesAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Sugestao> mSugestoes;
	
	public SugestoesAdapter(Context context, List<Sugestao> sugestoes){
		this.mContext = context;
		this.mSugestoes = sugestoes;
	}

	@Override
	public int getCount() {
		return mSugestoes.size();
	}

	@Override
	public Sugestao getItem(int position) {
		return mSugestoes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mSugestoes.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		if (v == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.items_sugestoes, null);
		}
		
		final Sugestao sugestao = mSugestoes.get(position);
		Button btSugestaoTime = (Button) v.findViewById(R.id.ctvSugestaoItem);
		TextView tvRepeticaoItem = (TextView) v.findViewById(R.id.tvRepeticaoSugestao);
		btSugestaoTime.setText(sugestao.getDiasRestantes());
		tvRepeticaoItem.setText(String.valueOf(sugestao.getRepeticao()));
		
		Button btApagar = (Button) v.findViewById(R.id.btApagarSugestao);
		btApagar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSugestoes.remove(position);
				notifyDataSetChanged();
				new SugestaoDao(mContext).apagar(sugestao);
				Toast.makeText(mContext, "Sugestão removida.", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		return v;
	}
}
