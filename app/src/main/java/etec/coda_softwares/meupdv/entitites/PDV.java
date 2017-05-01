package etec.coda_softwares.meupdv.entitites;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import etec.coda_softwares.meupdv.TelaInicial;

/**
 * Classe que representa o PDV do usuario, contendo todos os dados, o logo nome, produtos, etc.
 * Teoricamente somente uma instancia dessa classe por programa, instancia que fica salva em
 * {@link TelaInicial#getCurrentPdv()}
 *
 */
public class PDV implements Serializable {
    public static final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private String nome = "";
    private String lema = "";
    private String id = "";
    private List<String> integrantes = new ArrayList<>();

    public PDV() {
    }

    public PDV(String nome, List<String> integrantes, String lema) {
        this.nome = nome;
        this.lema = lema;
        this.integrantes = integrantes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLema() {
        return lema;
    }

    public void setLema(String lema) {
        this.lema = lema;
    }

    public List<String> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<String> integrantes) {
        this.integrantes = integrantes;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void initId(String id) {
        if (this.id.equals("")) this.id = id;
    }


    /**
     * Atualiza ou salva a instancia desse PDV no banco de dados atual e assimila com
     * {@link FirebaseAuth#getCurrentUser()} até agora todas as entidades atualizão-se
     * automaticamente quando modificadas. (29/04/17)
     */
    public void saveOnDB() {
        final DatabaseReference userPDV = root.child("pdv");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        final HandlerPadrao save = new HandlerPadrao(null) {
            @Override
            public Transaction.Result doTransaction(MutableData data) {
                PDV saved = data.getValue(PDV.class);
                if (saved != null) {
                    if (saved.equals(PDV.this))
                        return Transaction.abort();
                }
                saved = PDV.this;
                data.setValue(saved);
                root.child("user").child(user.getUid()).child("pdv").setValue(saved.getId());
                return Transaction.success(data);
            }
        };

        if (id.equals("")) {
            root.child("user").child(user.getUid()).child("pdv").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String usrid = (String) dataSnapshot.getValue();
                            if (usrid != null) {
                                if (!usrid.equals(""))
                                    return;
                            }
                            DatabaseReference dfr = userPDV.push();
                            initId(dfr.getKey());
                            dfr.runTransaction(save);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            FirebaseCrash.report(databaseError.toException());
                            System.exit(1);
                        }
                    });
        } else {
            userPDV.child(id).runTransaction(save);
        }
    }
}
