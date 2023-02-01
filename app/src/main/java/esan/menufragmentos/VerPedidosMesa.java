package esan.menufragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;

import esan.menufragmentos.adapter.PedidosMesaAdapter;
import esan.menufragmentos.adapter.PedidosNoConfirmedAdapter;
import esan.menufragmentos.model.PedidoMesa;
import esan.menufragmentos.model.PedidosNoConfirmed;


public class VerPedidosMesa extends Fragment {


    public int idMesa, numeroMesa;
    TextView voltarText;
    RecyclerView recyclerView;
    ArrayList<PedidoMesa> pedidos;
    private PedidosMesaAdapter adapter;
    Button btnConfirmarPagamento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idMesa = bundle.getInt("idMesa", 0);
            numeroMesa = bundle.getInt("numeroMesa", 0);
        }
        return inflater.inflate(R.layout.verpedidos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //confirmar pagamento
        String url2 = URLS.URL_MESAS + "?op=confirmarPedido&idMesa=" + idMesa;
        btnConfirmarPagamento = view.findViewById(R.id.btnConfirmarPagamento);
        btnConfirmarPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Pedido Pago!", Toast.LENGTH_SHORT).show();
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
                        params.put("op", "confirmarPedido");
                        params.put("idMesa", String.valueOf(idMesa));
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });
        //fim - confirmar pagamento


        voltarText = view.findViewById(R.id.text_pedidos_voltar);
        voltarText.setText(getContext().getResources().getString(R.string.Mesa) + " " + numeroMesa);
        voltarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_4 fragment = new Fragment_4();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, fragment);
                ft.commit();
            }
        });


        String url = URLS.URL_MESAS + "?op=mostrarPedidosMesa&idMesa=" + idMesa;

        recyclerView = view.findViewById(R.id.recyclerViewProdutos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pedidos = new ArrayList<PedidoMesa>();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                PedidoMesa p = new PedidoMesa();
                                p.id = c.getInt("id");
                                p.quantidade = c.getInt("quantidade");
                                p.estado = c.getString("estado");
                                p.date   = c.getString("date");
                                p.time = c.getString("time");
                                pedidos.add( p );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Usar o Adapter para associar os dados à RecyclerView
                        adapter = new PedidosMesaAdapter(pedidos);
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
                pedidos.clear();
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject c = jsonArray.getJSONObject(i);
                                        PedidoMesa p = new PedidoMesa();
                                        p.id = c.getInt("id");
                                        p.quantidade = c.getInt("quantidade");
                                        p.estado = c.getString("estado");
                                        p.date   = c.getString("date");
                                        p.time = c.getString("time");
                                        pedidos.add( p );
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Usar o Adapter para associar os dados à RecyclerView
                                adapter = new PedidosMesaAdapter(pedidos);
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

    }




}
