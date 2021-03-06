package etec.coda_softwares.meupdv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import de.hdodenhof.circleimageview.CircleImageView;
import etec.coda_softwares.meupdv.entitites.Fornecedor;

public class Fornecedores extends AppCompatActivity {
    private FirebaseListAdapter<Fornecedor> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fornecedores_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        populateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_fornecedores, menu);
        MenuItem item = menu.findItem(R.id.menu_fornecedor_add);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Fornecedores.this, CadastrarFornecedor.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }

    private void showOptions(final Fornecedor item) {
        AlertDialog.Builder fabrica = new AlertDialog.Builder(this);
        fabrica.setTitle(item.getNome().split(" ")[0]);
        fabrica.setCancelable(true);

        final ArrayAdapter<String> opcoes =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        opcoes.add("Editar");
        opcoes.add("Apagar");

        fabrica.setAdapter(opcoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String opcao = opcoes.getItem(which);
                assert opcao != null;

                if (opcao.equalsIgnoreCase("editar")) {
                    Intent i = new Intent(Fornecedores.this, CadastrarFornecedor.class);
                    i.putExtra("fornecedor", item);
                    startActivity(i);
                } else if (opcao.equalsIgnoreCase("apagar")) {
                    if (item.hasImagem())
                        TelaInicial.eraseFile(item.getImagem());
                    Fornecedor.DBROOT.child(item.getId()).setValue(null);
                }
            }
        });
        fabrica.show();
    }

    private void populateList() {
        ListView list = (ListView) findViewById(R.id.fornecedores_lista);
        DatabaseReference fornecedores = Fornecedor.DBROOT;

        adapter = new FirebaseListAdapter<Fornecedor>(this,
                Fornecedor.class, R.layout.fornecedor_item, fornecedores.orderByKey()) {
            @Override
            protected void populateView(final View v, final Fornecedor model, int position) {

                View nenhum = findViewById(R.id.fornecedor_nenhum);
                if (nenhum.getVisibility() != View.GONE)
                    nenhum.setVisibility(View.GONE);

                TextView titulo = (TextView) v.findViewById(R.id.fornecedor_item_nome),
                        telefone = (TextView) v.findViewById(R.id.fornecedor_item_telefone);
                // Menu de contexo.
                v.setLongClickable(true);
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showOptions(model);
                        return true;
                    }
                });

                ImageButton more = (ImageButton) v.findViewById(R.id.fornecedor_opc);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOptions(model);
                    }
                });

                final CircleImageView cImg = (CircleImageView) v.findViewById(R.id.fornecedor_img);
                if (model.hasImagem()) {
                    cImg.setImageBitmap(null); // Force the reloading of the image
                    TelaInicial.getFile(model.getImagem(), new TelaInicial.UriCallback() {
                        @Override
                        public void done(Uri u) {
                            cImg.setImageURI(u);
                        }
                    });
                } else {
                    cImg.setImageResource(R.drawable.ic_def_fornecedor);
                }
                model.setId(this.getRef(position).getKey());
                titulo.setText(model.getNome());
                PhoneNumberUtil ut = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber number;
                try {
                    number = ut.parse(model.getTelefones().get(0), "BR");
                    telefone.setText(ut.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
                } catch (NumberParseException e) {
                    telefone.setText(model.getTelefones().get(0));
                }
            }
        };
        list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        adapter.cleanup();
        super.onDestroy();
    }
}
