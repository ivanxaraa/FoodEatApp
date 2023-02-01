package esan.menufragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import esan.menufragmentos.adapter.CategoriasAdapter;
import esan.menufragmentos.adapter.PedidosNoConfirmedAdapter;
import esan.menufragmentos.adapter.ProdutosAdapter;
import esan.menufragmentos.model.PedidosNoConfirmed;
import esan.menufragmentos.model.Produto;

public class VerProdutosCategoria extends Fragment {


    int idCategoria;
    String nomeCategoria;
    TextView voltarText;
    RecyclerView recyclerView;
    ArrayList<Produto> produtos;
    SearchView searchView;
    private ProdutosAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idCategoria = bundle.getInt("idCategoria", 0);
            nomeCategoria = bundle.getString("nomeCategoria", null);
        }
        return inflater.inflate(R.layout.verprodutos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.searchView);

        voltarText = view.findViewById(R.id.text_pedidos_voltar);
        voltarText.setText(nomeCategoria);
        voltarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_2 fragment = new Fragment_2();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, fragment);
                ft.commit();
            }
        });


        String url = URLS.URL_CATEGORIAS + "?op=mostrarProdutos&idCategoria=" + idCategoria;

        recyclerView = view.findViewById(R.id.recyclerViewProdutos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        produtos = new ArrayList<Produto>();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                Produto p = new Produto();
                                p.id = c.getInt("id");
                                p.img = c.getString("img");
                                p.nome = c.getString("nome");
                                p.stock = c.getInt("stock");
                                p.status = c.getInt("status");
                                produtos.add( p );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Usar o Adapter para associar os dados à RecyclerView
                        adapter = new ProdutosAdapter(produtos);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

        //search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform search when the user submits the query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filter the data based on the new text entered in the search bar
                filterData(newText);
                return true;
            }
        });
        //fim


        //refresh
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the action to refresh the data here
                // For example, you can reload the data from the server or refresh the list
                produtos.clear();
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject c = jsonArray.getJSONObject(i);
                                        Produto p = new Produto();
                                        p.id = c.getInt("id");
                                        p.img = c.getString("img");
                                        p.nome = c.getString("nome");
                                        p.stock = c.getInt("stock");
                                        p.status = c.getInt("status");
                                        produtos.add( p );
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Usar o Adapter para associar os dados à RecyclerView
                                adapter = new ProdutosAdapter(produtos);
                                recyclerView.setAdapter(adapter);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Erro ao carregar os dados", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);

                swipeRefresh.setRefreshing(false);
            }
        });
        //fim refresh

    }


    private void filterData(String text) {
        ArrayList<Produto> filteredList = new ArrayList<>();
        for (Produto item : produtos) {
            if (item.getNome().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if(filteredList.isEmpty()){
            //Toast.makeText(getActivity(), "Não foi encontrada", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setFilteredList(filteredList);
        }
    }

}
