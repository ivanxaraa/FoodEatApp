package esan.menufragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import esan.menufragmentos.adapter.ItensPedidoAdapter;
import esan.menufragmentos.model.ItensPedido;

public class VerPedidoConfirmed extends Fragment {


    int idPedido;
    LinearLayout rowUser;
    TextView textIdPedido, textDate, textNomeCliente, textTable, voltarText;
    RecyclerView recyclerView;
    ArrayList<ItensPedido> itensPedidos;
    Button btnEntrege;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idPedido = bundle.getInt("idPedido", 0);
        }
        return inflater.inflate(R.layout.verpedido_confirmed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = URLS.URL_PEDIDOS + "?op=mostrarItens&idPedido=" + idPedido;

        textIdPedido = view.findViewById(R.id.textIdPedido);
        textIdPedido.setText("#" + String.valueOf(idPedido));

        textTable = view.findViewById(R.id.textTable);
        textDate = view.findViewById(R.id.textDate);
        textNomeCliente = view.findViewById(R.id.textCliente);
        rowUser = view.findViewById(R.id.rowUser);


        voltarText = view.findViewById(R.id.text_pedidos_voltar);
        voltarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_1 fragment = new Fragment_1();
                Bundle bundle = new Bundle();
                bundle.putInt("tab", 1);
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, fragment);
                ft.commit();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewProdutos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itensPedidos = new ArrayList<ItensPedido>();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                ItensPedido item = new ItensPedido();
                                item.id = c.getInt("id");
                                item.img = c.getString("img");
                                item.nome = c.getString("nome");
                                item.quantidade = c.getInt("quantidade");
                                item.preco = c.getDouble("preco");
                                itensPedidos.add( item );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Usar o Adapter para associar os dados à RecyclerView
                        ItensPedidoAdapter adapter = new ItensPedidoAdapter(itensPedidos);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);


        //Dados do pedido -> Data, cliente, Mesa...
        String url2 = URLS.URL_PEDIDOS + "?op=dadosPedido&idPedido=" + idPedido;

        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                textDate.setText(c.getString("date") + ", " + c.getString("time"));
                                textTable.setText(getContext().getResources().getString(R.string.Mesa) + " " + c.getString("numero"));
                                if(c.getString("username") != "null") {
                                    textNomeCliente.setText(c.getString("username"));
                                }else{
                                    rowUser.setVisibility(view.INVISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Erro", Toast.LENGTH_SHORT).show();
            }
        });
        queue2.add(stringRequest2);

        //FIM dados


        //aceitarPedido
        String url4 = URLS.URL_PEDIDOS + "?op=entregarPedido&idPedido=" + idPedido;
        btnEntrege = view.findViewById(R.id.btnEntrege);
        btnEntrege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url4, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "Entrega Confirmada!", Toast.LENGTH_SHORT).show();
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
                        params.put("op", "entregarPedido");
                        params.put("id", String.valueOf(idPedido));
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });
        //fim - aceitarPedido





    }

}
