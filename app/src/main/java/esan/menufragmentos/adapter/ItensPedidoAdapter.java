package esan.menufragmentos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import esan.menufragmentos.R;
import esan.menufragmentos.URLS;
import esan.menufragmentos.model.ItensPedido;

public class ItensPedidoAdapter extends RecyclerView.Adapter<ItensPedidoAdapter.ViewHolder> {


    // Definição do Holder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_preco, txt_nome, txt_quantidade;
        ImageView imgProduto;
        ViewHolder(View itemView) {
            super(itemView);
            // Identificar as Views que vão apresentar os dados
            txt_preco = itemView.findViewById(R.id.txt_preco);
            txt_nome = itemView.findViewById(R.id.txt_nome);
            imgProduto = itemView.findViewById(R.id.imgProduto);
            txt_quantidade = itemView.findViewById(R.id.txt_quantidade);
        }


    }


    // Definição do construtor do Apdater e da lista dos objetos com os dados
    private ArrayList<ItensPedido> itensPedidos;
    public ItensPedidoAdapter(ArrayList<ItensPedido> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }

    @NonNull
    // Método que cria as novas Views (item)
    // Invocado pelo gestor de layout
    @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_itempedido,parent,false);
        return new ViewHolder(itemView);
    }

    // Método que renderiza cada item na RecyclerView
    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItensPedido item = itensPedidos.get(position);
        holder.txt_preco.setText(String.valueOf(item.preco) + " €");
        holder.txt_nome.setText(item.nome);
        holder.txt_quantidade.setText(String.valueOf(item.quantidade) + " x ");
        Picasso.get()
                .load(URLS.URL_IMG_PRODUTO + item.img)
                .into(holder.imgProduto);

    }


    // Método que devolve a dimensão do conjunto de dados
    @Override public int getItemCount() {
        return itensPedidos.size();
    }
}