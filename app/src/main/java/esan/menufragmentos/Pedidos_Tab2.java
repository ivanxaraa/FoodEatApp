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

import esan.menufragmentos.adapter.PedidosConfirmedAdapter;
import esan.menufragmentos.model.PedidosConfirmed;


public class Pedidos_Tab2 extends Fragment {

    RecyclerView recyclerView;
    ArrayList<PedidosConfirmed> pedidosConfirmed;

    int idUser = SharedPrefManager.getInstance(getContext()).getUser().getId();
    String url2 = URLS.URL_PEDIDOS + "?op=mostrarPedido&estado=Confirmado" + "&id=" + idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pedidosConfirmed = new ArrayList<PedidosConfirmed>();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                System.out.println(i);
                                JSONObject c = jsonArray.getJSONObject(i);
                                PedidosConfirmed d = new PedidosConfirmed();
                                d.id = c.getInt("id");
                                d.numeroMesa = c.getInt("numeroMesa");
                                d.quantidade = c.getInt("quantidade");
                                d.Mesa_id = c.getInt("Mesa_id");
                                pedidosConfirmed.add( d );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Usar o Adapter para associar os dados à RecyclerView
                        PedidosConfirmedAdapter adapter = new PedidosConfirmedAdapter(pedidosConfirmed);
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
                pedidosConfirmed.clear();
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject c = jsonArray.getJSONObject(i);
                                        PedidosConfirmed d = new PedidosConfirmed();
                                        d.id = c.getInt("id");
                                        d.numeroMesa = c.getInt("numeroMesa");
                                        d.quantidade = c.getInt("quantidade");
                                        d.Mesa_id = c.getInt("Mesa_id");
                                        pedidosConfirmed.add( d );
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                PedidosConfirmedAdapter adapter = new PedidosConfirmedAdapter(pedidosConfirmed);
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
