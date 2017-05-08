package etec.coda_softwares.meupdv;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by samuelh on 07/05/17.
 */

public class Util {

    public static String lerString(EditText view) {
        return view.getText().toString();
    }

    public static double lerDouble(EditText t) {
        return Double.parseDouble(t.getText().toString());
    }

    public static boolean verificarStringsVazias(String... valores) {
        for (String s : valores) {
            if (s.trim().equals("")) {
                return false;
            }
        }
        return true;
    }

    public static void showToast(AppCompatActivity ativ, String msg) {
        Toast.makeText(ativ, msg, Toast.LENGTH_SHORT).show();
    }
}