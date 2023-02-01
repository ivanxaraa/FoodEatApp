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
import esan.menufragmentos.VerPedidoNoConfirmed;
import esan.menufragmentos.model.PedidosNoConfirmed;

public class PedidosNoConfirmedAdapter extends RecyclerView.Adapter<PedidosNoConfirmedAdapter.ViewHolder> {


    // Definição do Holder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_mesa, txt_id, txt_quantidade;
        Button btnVerPedido;
        ViewHolder(View itemView) {
            super(itemView);
            // Identificar as Views que vão apresentar os dados
            txt_mesa = itemView.findViewById(R.id.txt_mesa);
            txt_id = itemView.findViewById(R.id.txt_preco);
            txt_quantidade = itemView.findViewById(R.id.txt_quantidade);
            btnVerPedido = itemView.findViewById(R.id.btnVerPedido);
        }
    }


    // Definição do construtor do Apdater e da lista dos objetos com os dados
    private ArrayList<PedidosNoConfirmed> pedidosNoConfirmeds;
    public PedidosNoConfirmedAdapter(ArrayList<PedidosNoConfirmed> pedidos) {
        this.pedidosNoConfirmeds = pedidos;
    }

    @NonNull
    // Método que cria as novas Views (item)
    // Invocado pelo gestor de layout
    @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_pedido,parent,false);
        return new ViewHolder(itemView);
    }

    // Método que renderiza cada item na RecyclerView
    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PedidosNoConfirmed item = pedidosNoConfirmeds.get(position);
        holder.txt_id.setText("#" + String.valueOf(item.id));
        holder.txt_quantidade.setText(String.valueOf(item.quantidade) + " Items");
        holder.txt_mesa.setText(holder.itemView.getContext().getResources().getString(R.string.Mesa) + " " + String.valueOf(item.numeroMesa));

        holder.btnVerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPedidoNoConfirmed fragment = new VerPedidoNoConfirmed();
                Bundle bundle = new Bundle();
                bundle.putInt("idPedido", item.id);
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, fragment);
                ft.commit();
            }
        });

    }


    // Método que devolve a dimensão do conjunto de dados
    @Override public int getItemCount() {
        return pedidosNoConfirmeds.size();
    }
}