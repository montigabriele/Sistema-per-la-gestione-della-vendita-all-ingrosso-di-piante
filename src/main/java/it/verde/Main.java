package it.verde;

import it.verde.config.ApplicationCompositionRoot;
import it.verde.exception.DaoException;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        try (ApplicationCompositionRoot root = new ApplicationCompositionRoot()) {
            root.applicationRunner().run();
        } catch (DaoException e) {
            System.err.println("Errore durante la chiusura della sessione database: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Errore durante l'avvio dell'applicazione: " + e.getMessage());
        }
    }
}
