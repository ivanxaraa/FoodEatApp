package esan.menufragmentos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import esan.menufragmentos.R;
import esan.menufragmentos.URLS;

import esan.menufragmentos.VerProdutosCategoria;
import esan.menufragmentos.model.Produto;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ViewHolder> {


    // Definição do Holder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_nome, txt_stock;
        ImageView imgProduto;
        ImageButton img_status;

        ViewHolder(View itemView) {
            super(itemView);
            // Identificar as Views que vão apresentar os dados
            txt_nome = itemView.findViewById(R.id.txt_nome);
            txt_stock = itemView.findViewById(R.id.txt_stock);
            imgProduto = itemView.findViewById(R.id.imgProduto);
            img_status = itemView.findViewById(R.id.img_status);
        }


    }

    // Definição do construtor do Apdater e da lista dos objetos com os dados
    private ArrayList<Produto> produtos;
    public ProdutosAdapter(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public void setFilteredList(ArrayList<Produto> filteredList){
        produtos = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    // Método que cria as novas Views (item)
    // Invocado pelo gestor de layout
    @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_stockproduto,parent,false);
        return new ViewHolder(itemView);
    }

    // Método que renderiza cada item na RecyclerView
    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto item = produtos.get(position);
        holder.txt_nome.setText(item.nome);
        holder.txt_stock.setText(String.valueOf(item.stock) + " Items");
        if(item.status == 1){
            holder.img_status.setImageResource(R.color.confirmado);
        }else{
            holder.img_status.setImageResource(R.color.purple_800);
        }
        Picasso.get()
                .load(URLS.URL_IMG_PRODUTO + item.img)
                .into(holder.imgProduto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog popUp = new BottomSheetDialog(v.getContext());
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.popup_produto, null);

                TextView txt_nome = dialogView.findViewById(R.id.txt_nome);
                ImageView imgProduto = dialogView.findViewById(R.id.imgProduto);
                EditText EditStock = dialogView.findViewById(R.id.nameEditText);
                SwitchCompat mySwitch = dialogView.findViewById(R.id.idstatus);
                Button btnStock = dialogView.findViewById(R.id.btnStock);

                txt_nome.setText(item.nome);
                EditStock.setText(String.valueOf(item.stock));
                if(item.status == 1){
                    mySwitch.setChecked(true);
                }else{
                    mySwitch.setChecked(false);
                }
                Picasso.get()
                        .load(URLS.URL_IMG_PRODUTO + item.img)
                        .into(imgProduto);
                popUp.setContentView(dialogView);
                popUp.show();

                btnStock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String newStock = EditStock.getText().toString();
                        final int newStatus;
                        if(mySwitch.isChecked()){
                            newStatus = 1;
                        }else{
                            newStatus = 0;
                        }

                        popUp.dismiss();
                        Toast.makeText(v.getContext(), "Alterado com sucesso!", Toast.LENGTH_SHORT).show();

                        //começou
                        String url = URLS.URL_CATEGORIAS + "?op=updateProduto&idProduto=" + item.id + "&newStatus=" + newStatus+ "&newStock=" + newStock;
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.print(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.print(error.getMessage());
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                //add params to be sent to the server
                                Map<String, String> params = new HashMap<>();
                                params.put("idProduto", String.valueOf(item.id));
                                params.put("newStock", newStock);
                                params.put("newStatus", String.valueOf(newStatus));

                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                        requestQueue.add(stringRequest);
                        //acabou

                    }
                });



            } // click acabou
        });

    }


    // Método que devolve a dimensão do conjunto de dados
    @Override public int getItemCount() {
        return produtos.size();
    }
}