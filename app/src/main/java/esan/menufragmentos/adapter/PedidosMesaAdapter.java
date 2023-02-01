package esan.menufragmentos.adapter;

import android.os.Bundle;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.Duration;

import esan.menufragmentos.R;
import esan.menufragmentos.URLS;
import esan.menufragmentos.VerPedidoConfirmed;
import esan.menufragmentos.VerPedidoMesa;
import esan.menufragmentos.VerPedidosMesa;
import esan.menufragmentos.model.PedidoMesa;


public class PedidosMesaAdapter extends RecyclerView.Adapter<PedidosMesaAdapter.ViewHolder> {


    // Definição do Holder
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_quantidade, txt_tempoAtras;
        ImageButton img_status;
        Button btnVerPedido;

        ViewHolder(View itemView) {
            super(itemView);
            // Identificar as Views que vão apresentar os dados
            txt_quantidade = itemView.findViewById(R.id.txt_quantidade);
            txt_tempoAtras = itemView.findViewById(R.id.txt_tempoAtras);
            img_status = itemView.findViewById(R.id.img_status);
            btnVerPedido = itemView.findViewById(R.id.btnVerPedido);
        }


    }

    // Definição do construtor do Apdater e da lista dos objetos com os dados
    private ArrayList<PedidoMesa> pedidos;
    public PedidosMesaAdapter(ArrayList<PedidoMesa> pedidos) {
        this.pedidos = pedidos;
    }




    @NonNull
    // Método que cria as novas Views (item)
    // Invocado pelo gestor de layout
    @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_pedidomesa,parent,false);
        return new ViewHolder(itemView);
    }

    // Método que renderiza cada item na RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PedidoMesa item = pedidos.get(position);
        holder.txt_quantidade.setText(String.valueOf(item.quantidade) + " Items");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(item.date + " " + item.time);
            Date currentDate = new Date();
            long diff = currentDate.getTime() - date.getTime();
            long minutes = diff / (60 * 1000);
            if(minutes < 60) {
                holder.txt_tempoAtras.setText(minutes + " " + holder.itemView.getContext().getResources().getString(R.string.time_minutos));
            } else if (minutes < 1440) {
                holder.txt_tempoAtras.setText((minutes / 60) + " " + holder.itemView.getContext().getResources().getString(R.string.time_horas));
            } else {
                holder.txt_tempoAtras.setText((minutes / 1440) + " " + holder.itemView.getContext().getResources().getString(R.string.time_dias));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Switch statement for item.estado
        switch(item.estado){
            case "porConfirmar":
                holder.img_status.setImageResource(R.color.porConfirmar);
                break;
            case  "Confirmado":
                holder.img_status.setImageResource(R.color.confirmado);
                break;
            case  "Entregue":
                holder.img_status.setImageResource(R.color.Pago);
                break;
            case  "Recusado":
                holder.img_status.setImageResource(R.color.Recusado);
                break;
        }

        holder.btnVerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerPedidoMesa fragment = new VerPedidoMesa();
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
        return pedidos.size();
    }
}