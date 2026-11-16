package padaria.util;

import java.util.Locale;

public final class Moeda {
    private Moeda() {
    }

    public static String formatar(double valor) {
        return String.format(Locale.ROOT, "R$ %.2f", valor).replace('.', ',');
    }
}
