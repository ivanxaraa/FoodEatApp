package esan.menufragmentos;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SHARED_PREF_NAME = "sharedpref";
    public static final String KEY_NOME = "keynome";
    public static final String KEY_EMAIL = "keyemail";
    public static final String KEY_TELEFONE = "keytelefone";
    private static final String KEY_ID = "keyid";
    public static final Boolean KEY_GUARDAR = false;

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager (Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin (User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putInt(KEY_TELEFONE, user.getTelefone());
        editor.putString(KEY_NOME, user.getNome());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putBoolean(String.valueOf(KEY_GUARDAR), user.getGuardar());
        editor.apply();
    }

    //this method will check wether user is logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(String.valueOf(KEY_GUARDAR), false) == true;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getInt(KEY_TELEFONE, 999),
                sharedPreferences.getString(KEY_NOME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getBoolean(String.valueOf(KEY_GUARDAR), false)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }
}
