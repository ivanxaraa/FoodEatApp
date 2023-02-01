package esan.menufragmentos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment f1, f2, f3, f4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        f1 = new Fragment_1();
        f2 = new Fragment_2();
        f3 = new Fragment_3();
        f4 = new Fragment_4();

        BottomNavigationView navigation = findViewById(R.id.navegacao);
        navigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_1:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.placeholder, f1)
                            .commit();
                    break;
                case R.id.nav_2:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.placeholder, f2)
                            .commit();
                    break;
                case R.id.nav_3:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.placeholder, f3)
                            .commit();
                    break;

                case R.id.nav_4:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.placeholder, f4)
                            .commit();
                    break;
            }
            return true;
        });
    }
}