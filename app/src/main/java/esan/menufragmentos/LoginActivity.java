package esan.menufragmentos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText email, password;
    CheckBox checkBoxRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        checkBoxRemember = findViewById(R.id.checkBoxRemember);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
    }



    private void userLogin() {

        //first getting the values
        final String textEmail = email.getText().toString();
        final String textPassword = password.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(textEmail)) {
            email.setError("Por favor introduza o seu email");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(textPassword)) {
            password.setError("Por favor introduza a sua password");
            password.requestFocus();
            return;
        }
        //fim validating

        boolean guardarDados;
        if(checkBoxRemember.isChecked()) {
            guardarDados = true;
        }else{
            guardarDados = false;
        }

        //if everything is fine
        class UserLogin extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        User user = new User(
                                obj.getInt("id"),
                                obj.getInt("telefone"),
                                obj.getString("nome"),
                                obj.getString("email"),
                                guardarDados
                        );

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email ou Password inválidos.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", textEmail);
                params.put("password", textPassword);

                //returning the response
                return requestHandler.sendPostRequest(URLS.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}