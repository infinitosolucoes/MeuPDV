package etec.coda_softwares.meupdv.menuPrincipal;

/**
 * Created by dovahkiin on 22/04/17.
 */

public class ItemMenuPrincipal {
    private int image;
    private String titulo;
    private Runnable action;

    public ItemMenuPrincipal(int image, String titulo, Runnable action) {
        this.image = image;
        this.titulo = titulo;
        this.action = action;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
