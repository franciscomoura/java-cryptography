package demo;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class Encriptador {

    private static final String ALGORITIMO = "AES";
    private static final String ALGORITIMO_DECIFRAR = "AES/ECB/PKCS5Padding";
    private static final String HEX_DIGITS = "0123456789ABCDEF";

    /**
     * Essa chave é aleatória e foi utilizada somente como ilustração/testes/demo
     * Em homologação/produção, deverá ser utilizada a chave fornecida.
     */
    private static final String KEY = "D06D60D9BA0CAFEDD8648FB53F11C28D"; // TODO: change it!

    private static String cifrar(String texto) {

        String valorCriptografado;


        try {
            SecretKeySpec KeySpec = new SecretKeySpec(asByte(KEY), ALGORITIMO);
            Cipher cipher = Cipher.getInstance(ALGORITIMO);
            cipher.init(Cipher.ENCRYPT_MODE, KeySpec);
            byte[] encrypted = cipher.doFinal(texto.getBytes());
            valorCriptografado = Base64.getEncoder().encodeToString(encrypted);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {

            throw new RuntimeException("Ocorreu um erro ao cifrar o texto.", e);
        }

        return valorCriptografado;
    }

    private static String decifrar(String texto) {

        String valorDescriptografado;

        try {
            SecretKeySpec KeySpec = new SecretKeySpec(asByte(KEY), ALGORITIMO);
            byte[] decoded = Base64.getDecoder().decode(texto.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(ALGORITIMO_DECIFRAR);
            cipher.init(Cipher.DECRYPT_MODE, KeySpec);
            valorDescriptografado = new String(cipher.doFinal(decoded));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Ocorreu um erro ao decifrar o texto.");
        }

        return valorDescriptografado;
    }

    private static byte[] asByte(String hexa) throws IllegalArgumentException {

        if (hexa.length() % 2 != 0) {
            throw new IllegalArgumentException("String hexa invalida");
        }

        byte[] b = new byte[hexa.length() / 2];

        for (int i = 0; i < hexa.length(); i += 2) {
            b[i / 2] = (byte) ((HEX_DIGITS.indexOf(hexa.charAt(i)) << 4) | (HEX_DIGITS.indexOf(hexa.charAt(i + 1))));
        }
        return b;
    }

    public static void main(String[] args) {
        String texto = "0000374378020619";
        String s = cifrar(texto);
        String msg = String.format(">> String criptografada de '%s': %s", texto, s);
        System.out.println(msg);

        // criptografia de 0000374378020619
        String d = decifrar(s);

        // Criptografia decifrada:
        msg = String.format(">> Código '%s' decifrado: %s", s, d);
        System.out.println(msg);
    }
}
