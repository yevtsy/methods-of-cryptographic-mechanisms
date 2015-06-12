package core.pkcs;

import core.cryptosystem.ElGamal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vadym
 * @since 12.06.15 8:23
 */
public class Token {
    public enum Mechanism {
        SHA_1_HMAC, RSA_PKCS_OAEP, ELGAMAL
    }
    private final String PIN = "1234";
    private final String label;

    private List<Mechanism> mechanisms = new ArrayList<>(Arrays.asList(Mechanism.ELGAMAL));
    private Session session;

    public Token(String pin, String label) {
        if (!PIN.equals(pin)) throw new RuntimeException("Wrong PIN");
        this.label = label;

        session = new ElGamal();
    }

    public List<Mechanism> getMechanismList() {
        return mechanisms;
    }

    public Session getSession() {
        return session;
    }

    public String getLabel() {
        return label;
    }
}
