package org.qtum.wallet.ui.fragment.fragment_input_password;

public interface IInputPasswordInteractor {
    String getPassword();

    void savePassword(String password);

    String generateSHA256String(String pin);
    void setKeyGeneratedInstance(boolean isKeyGenerated);
}
