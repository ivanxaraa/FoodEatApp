package esan.menufragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import esan.menufragmentos.adapter.PedidosNoConfirmedAdapter;
import esan.menufragmentos.model.PedidosNoConfirmed;

public class Pedidos_Tab1 extends Fragment {

    RecyclerView recyclerView;
    ArrayList<PedidosNoConfirmed> pedidosNoConfirmeds;

    int idUser = SharedPrefManager.getInstance(getContext()).getUser().getId();
    String url = URLS.URL_PEDIDOS + "?op=mostrarPedido&estado=porConfirmar" + "&id=" + idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pedidosNoConfirmeds = new ArrayList<PedidosNoConfirmed>();
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
                                PedidosNoConfirmed d = new PedidosNoConfirmed();
                                d.id = c.getInt("id");
                                d.numeroMesa = c.getInt("numeroMesa");
                                d.quantidade = c.getInt("quantidade");
                                d.Mesa_id = c.getInt("Mesa_id");
                                pedidosNoConfirmeds.add( d );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Usar o Adapter para associar os dados à RecyclerView
                        PedidosNoConfirmedAdapter adapter = new PedidosNoConfirmedAdapter(pedidosNoConfirmeds);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the action to refresh the data here
                // For example, you can reload the data from the server or refresh the list
                pedidosNoConfirmeds.clear();
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject c = jsonArray.getJSONObject(i);
                                        PedidosNoConfirmed d = new PedidosNoConfirmed();
                                        d.id = c.getInt("id");
                                        d.numeroMesa = c.getInt("numeroMesa");
                                        d.quantidade = c.getInt("quantidade");
                                        d.Mesa_id = c.getInt("Mesa_id");
                                        pedidosNoConfirmeds.add( d );
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                PedidosNoConfirmedAdapter adapter = new PedidosNoConfirmedAdapter(pedidosNoConfirmeds);
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

    }
}
