package etec.coda_softwares.meupdv;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_principal_menu, menu);
        menu.findItem(R.id.mp_sair)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        System.exit(0);
                        return false;
                    }
                });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar ab = (Toolbar) findViewById(R.id.mp_toolbar);
        ab.setBackgroundColor(getResources().getColor(R.color.cor_acento));
        ab.setTitleTextColor(Color.rgb(255, 255, 255));
        setSupportActionBar(ab);
        populateList();
    }

    private void populateList() {
        ListView a = (ListView) findViewById(R.id.mp_listaItems);
        MenuPrincipalAdapter adapter = new MenuPrincipalAdapter(this, R.layout.menu_principal_item,
                R.id.item_Title);
        adapter.add(new ItemMenuPrincipal(R.drawable.mypdv, "Samuel", "is awesome", new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MenuPrincipal.this, "Certo", Toast.LENGTH_SHORT).show();
            }
        }));
        adapter.add(new ItemMenuPrincipal(R.drawable.mypdv, "Jeffe", "is awesome", new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MenuPrincipal.this, "Errado", Toast.LENGTH_SHORT).show();
            }
        }));

        adapter.add(new ItemMenuPrincipal(R.drawable.mypdv, "Cianureto", "Is the only option", new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        }));
        a.setAdapter(adapter);
    }

}

