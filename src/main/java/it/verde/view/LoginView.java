package it.verde.view;

import it.verde.view.bean.LoginCredentialsBean;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;

public final class LoginView extends BaseView {
    public LoginView(InputView input, OutputView output, UiManager ui) {
        super(input, output, ui);
    }

    public LoginCredentialsBean readCredentials() {
        clearScreen();
        ui.showHeader();
        showTitle("Login", ui.theme().success(), 80);

        LoginCredentialsBean credentials = new LoginCredentialsBean();
        credentials.setUsername(readRequiredString("Inserisci username: "));
        credentials.setPassword(readRequiredString("Inserisci password: "));
        return credentials;
    }
}
