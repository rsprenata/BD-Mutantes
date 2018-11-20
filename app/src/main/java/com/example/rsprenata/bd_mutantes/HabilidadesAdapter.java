package com.example.rsprenata.bd_mutantes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public abstract class HabilidadesAdapter extends BaseAdapter {
    private List<String> habilidades;
    private LayoutInflater inflater;

    public HabilidadesAdapter(Context context, List<String> habilidades) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.habilidades = habilidades;
    }

    public void novosDados(List<String> habilidades) {
        this.habilidades = habilidades;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return habilidades.size();
    }

    @Override
    public Object getItem(int position) {
        return habilidades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_habilidade, null);
        ((TextView)(v.findViewById(R.id.txtHabilidade))).setText(habilidades.get(position));
        ((Button)(v.findViewById(R.id.btnEditar))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarHabilidade(habilidades.get(position), position);
            }
        });
        ((Button)(v.findViewById(R.id.btnExcluir))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirHabilidade(position);
            }
        });

        return v;
    }

    public abstract void editarHabilidade(String habilidade, Integer habilidadePosition);

    public void excluirHabilidade(Integer habilidadePosition) {
        habilidades.remove(habilidadePosition.intValue());
        novosDados(habilidades);
    }
}
