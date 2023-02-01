package esan.menufragmentos.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import esan.menufragmentos.R;
import esan.menufragmentos.VerPedidosMesa;
import esan.menufragmentos.model.Categoria;
import esan.menufragmentos.model.Mesa;

public class MesasAdapter extends RecyclerView.Adapter<MesasAdapter.ViewHolder> {

    private ArrayList<Mesa> mesas;

    public void setFilteredList(ArrayList<Mesa> filteredList){
        mesas = filteredList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_mesa, txt_id, txt_quantidade;
        Button btnVerPedidos;

        ViewHolder(View itemView) {
            super(itemView);
            txt_mesa = itemView.findViewById(R.id.txt_mesa);
            txt_quantidade = itemView.findViewById(R.id.txt_quantidade);
            btnVerPedidos = itemView.findViewById(R.id.btnVerPedidos);
        }

    }

    // Definição do construtor do Adapter e da lista dos objetos com os dados
    public MesasAdapter(ArrayList<Mesa> mesas) {
        this.mesas = mesas;
    }

    @NonNull
    // Método que cria as novas Views (item)
    @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_mesa,parent,false);
        return new ViewHolder(itemView);


    }

    // Método que renderiza cada item na RecyclerView
    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mesa item = mesas.get(position);
        holder.txt_quantidade.setText(String.valueOf(item.numeroPedidos) + " " + holder.itemView.getContext().getResources().getString(R.string.title_pedido));
        holder.txt_mesa.setText(holder.itemView.getContext().getResources().getString(R.string.Mesa) + " " + String.valueOf(item.numeroMesa));

        holder.btnVerPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPedidosMesa fragment = new VerPedidosMesa();
                Bundle bundle = new Bundle();
                bundle.putInt("idMesa", item.id);
                bundle.putInt("numeroMesa", item.numeroMesa);
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, fragment);
                ft.commit();
            }
        });

    }

    // Método que devolve a dimensão do conjunto de dados
    @Override public int getItemCount() {
        return mesas.size();
    }
}