package core.pkcs;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

/**
 * @author vadym
 * @since 11.06.15 23:11
 */
public class PasswordBasedCrypto {
    private static final String ALGORITHM = "HmacSHA1";
    private Mac PRF;

    public PasswordBasedCrypto() {
    }

    public PasswordBasedCrypto(String password) {
        setPassword(password);
    }

    public void setPassword(String password) {
        if (password == null || password.trim().equals(""))
            throw new IllegalArgumentException("Password should be set");

        try {
            PRF = Mac.getInstance(ALGORITHM);
            PRF.init(new SecretKeySpec(password.getBytes(), ALGORITHM));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BigInteger deriveKey(BigInteger salt) {
        byte[] key = PBKDF2(salt.toByteArray(), 1000, PRF.getMacLength());

        return (new BigInteger(key)).abs();
    }


    private byte[] PBKDF2(byte[] salt, int c, int dkLen) {
        int hLen = PRF.getMacLength();
        int l = (int) Math.ceil(dkLen / (float)hLen);
        int r = dkLen - (l - 1) * hLen;

        byte T[] = new byte[l * hLen];
        int ti_offset = 0;

        for (int i = 1; i <= l; i++)
        {
            F(T, ti_offset, salt, c, i);
            ti_offset += hLen;
        }
        if (r < hLen)
        {
            // Incomplete last block
            byte DK[] = new byte[dkLen];
            System.arraycopy(T, 0, DK, 0, dkLen);
            return DK;
        }
        return T;
    }

    private void F(byte[] dest, int offset, byte[] S, int c, int blockIndex)
    {
        int hLen = PRF.getMacLength();
        byte U_r[] = new byte[hLen];

        // U0 = S || INT (i)
        byte U_i[] = new byte[S.length + 4];
        System.arraycopy(S, 0, U_i, 0, S.length);

        // INT(i)
        U_i[S.length] = (byte) (blockIndex / (256 * 256 * 256));
        U_i[S.length + 1] = (byte) (blockIndex / (256 * 256));
        U_i[S.length + 2] = (byte) (blockIndex / (256));
        U_i[S.length + 3] = (byte) (blockIndex);

        for (int i = 0; i < c; i++)
        {
            // U_i = PRF (P, U_(i-1))
            U_i = PRF.doFinal(U_i);

            // U_r = U_r XOR U_i
            for (int j = 0; j < U_r.length; j++)
            {
                U_r[j] ^= U_i[j];
            }
        }
        System.arraycopy(U_r, 0, dest, offset, hLen);
    }
}
