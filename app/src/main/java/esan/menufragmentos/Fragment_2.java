package esan.menufragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import esan.menufragmentos.model.Categoria;
import esan.menufragmentos.model.PedidosNoConfirmed;

public class Fragment_2 extends Fragment {

    ArrayList<Categoria> categorias;
    RecyclerView recyclerView;
    SearchView searchView;
    private CategoriasAdapter adapter;
    int idUser = SharedPrefManager.getInstance(getContext()).getUser().getId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onViewCreated(view, savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) view;

        searchView = view.findViewById(R.id.searchView);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Obter a relação de distritos por HTTP
        categorias = new ArrayList<Categoria>();

        // Inicializar a RequestQueue e definir o URL do pedido
        RequestQueue queue = Volley.newRequestQueue(getActivity());



        String url = URLS.URL_CATEGORIAS + "?op=mostrarCategorias&id=" + idUser;
        // Solicitar uma string de resposta a um pedido por URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                System.out.println(i);
                                JSONObject c = jsonArray.getJSONObject(i);
                                Categoria ct = new Categoria();
                                ct.id = c.getInt("id");
                                ct.nome = c.getString("nome");
                                categorias.add( ct );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Usar o Adapter para associar os dados à RecyclerView
                        adapter = new CategoriasAdapter(categorias);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();
            }
        });
        // Adicionar o pedido à RequestQueue.
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


        //refresh
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the action to refresh the data here
                // For example, you can reload the data from the server or refresh the list
                categorias.clear();
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        System.out.println(i);
                                        JSONObject c = jsonArray.getJSONObject(i);
                                        Categoria ct = new Categoria();
                                        ct.id = c.getInt("id");
                                        ct.nome = c.getString("nome");
                                        categorias.add( ct );
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Usar o Adapter para associar os dados à RecyclerView
                                adapter = new CategoriasAdapter(categorias);
                                recyclerView.setAdapter(adapter);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);

                swipeRefresh.setRefreshing(false);
            }
        });


        //fim
    }

    private void filterData(String text) {
        ArrayList<Categoria> filteredList = new ArrayList<>();
        for (Categoria item : categorias) {
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