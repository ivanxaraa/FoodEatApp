package esan.menufragmentos.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import esan.menufragmentos.R;
import esan.menufragmentos.VerPedidoConfirmed;
import esan.menufragmentos.VerProdutosCategoria;
import esan.menufragmentos.model.Categoria;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private ArrayList<Categoria> categorias;
    SwipeRefreshLayout swipeRefreshLayout;

    public void setFilteredList(ArrayList<Categoria> filteredList){
        categorias = filteredList;
        notifyDataSetChanged();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        Button btnCategoria;


        ViewHolder(View itemView) {
            super(itemView);
            btnCategoria = itemView.findViewById(R.id.btnCategoria);
        }

    }

    // Definição do construtor do Adapter e da lista dos objetos com os dados
    public CategoriasAdapter(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
    }

    @NonNull
    // Método que cria as novas Views (item)
    @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_categoria,parent,false);
        return new ViewHolder(itemView);


    }

    // Método que renderiza cada item na RecyclerView
    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria item = categorias.get(position);
        holder.btnCategoria.setText(String.valueOf(item.nome));

        holder.btnCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerProdutosCategoria fragment = new VerProdutosCategoria();
                Bundle bundle = new Bundle();
                bundle.putInt("idCategoria", item.id);
                bundle.putString("nomeCategoria", String.valueOf(item.nome));
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, fragment);
                ft.commit();
            }
        });

    }

    // Método que devolve a dimensão do conjunto de dados
    @Override public int getItemCount() {
        return categorias.size();
    }
}