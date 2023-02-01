package esan.menufragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Fragment_3 extends Fragment {

    Button btnGuardar, btnLogout;
    EditText nameEditText, emailEditText, telEditText, passAntigaEditText, passNovaEditText;
    boolean alterarPass;

    String currentUserName = SharedPrefManager.getInstance(getContext()).getUser().getNome();
    String currentUserEmail = SharedPrefManager.getInstance(getContext()).getUser().getEmail();
    int currentUserTelefone = SharedPrefManager.getInstance(getContext()).getUser().getTelefone();
    int idUser = SharedPrefManager.getInstance(getContext()).getUser().getId();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        nameEditText.setText(currentUserName);

        emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        emailEditText.setText(currentUserEmail);

        telEditText = (EditText) view.findViewById(R.id.telEditText);
        telEditText.setText(String.valueOf(currentUserTelefone));

        passAntigaEditText = (EditText) view.findViewById(R.id.passAntigaEditText);
        passNovaEditText = (EditText) view.findViewById(R.id.passNovaEditText);

        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarDados();
            }
        });

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getContext().getApplicationContext()).logout();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void alterarDados(){

        final String textNome = nameEditText.getText().toString();
        final String textEmail = emailEditText.getText().toString();
        final String textTelefone = telEditText.getText().toString();
        final String textPassAntiga = passAntigaEditText.getText().toString();
        final String textPassNova = passNovaEditText.getText().toString();



        //Validação
        if (TextUtils.isEmpty(textNome)) {
            nameEditText.setError("Por favor introduza o seu nome");
            nameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(textEmail)) {
            emailEditText.setError("Por favor introduza o seu email");
            emailEditText.requestFocus();
            return;
        }else if(!(isValid(textEmail))){
            emailEditText.setError("O seu email é inválido");
            emailEditText.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(textTelefone)) {
            telEditText.setError("Por favor introduza o seu telefone");
            telEditText.requestFocus();
            return;
        }else if(textTelefone.length() != 9){
                    telEditText.setError("O telefone deve ter 9 digitos");
                    telEditText.requestFocus();
                    return;
                }else{
                    int newTelefone = Integer.parseInt(textTelefone);
                }

        if (TextUtils.isEmpty(textPassAntiga) || TextUtils.isEmpty(textPassNova)){
            alterarPass = false;
        }else{
            alterarPass = true;
        }

        //FIM Validação


        //começou
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLS.URL_CONTA, new Response.Listener<String>() {
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
                params.put("id", String.valueOf(idUser));
                params.put("nome", textNome);
                params.put("email", textEmail);
                params.put("telefone", textTelefone);
                if(alterarPass){
                    params.put("passAntiga", textPassAntiga);
                    params.put("passNova", textPassNova);
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        //acabou

        //atualizar a sharedPref

        SharedPreferences prefs = getContext().getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPrefManager.KEY_NOME, textNome);
        editor.putString(SharedPrefManager.KEY_EMAIL, textEmail);
        editor.putInt(SharedPrefManager.KEY_TELEFONE, Integer.parseInt(textTelefone));
        editor.commit();


        Toast.makeText(getActivity(), "Dados alterados com Sucesso!", Toast.LENGTH_SHORT).show();
    }

    public static boolean isValid(String email)    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}

