package encriptacion;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * Clase para encriptar.
 *
 */
public class Encripta {
    String clave;
    public Encripta(String clave)
    {
        this.clave=clave;
    }

    public String encriptaCadena(String mensaje) throws EncriptaException {
        byte[] rpta = encripta(mensaje);
        StringBuilder mensaje1 = new StringBuilder();
        for (int i = 0; i < rpta.length; i++) {
            String cad = Integer.toHexString(rpta[i]).toUpperCase();
            if (i != 0) {
                mensaje1.append("-");
            }
            if (cad.length() < 2) {
                mensaje1.append("0");
                mensaje1.append(cad);
            } else if (cad.length() > 2) {
                mensaje1.append(cad.substring(6, 8));
            } else {
                mensaje1.append(cad);
            }
        }
        return mensaje1.toString();
    }

    public byte[] encripta(String message) throws EncriptaException {
        try {
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest(clave.getBytes("utf-8"));
            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }
            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            final byte[] plainTextBytes = message.getBytes("utf-8");
            final byte[] cipherText = cipher.doFinal(plainTextBytes);
            return cipherText;
        } catch (NoSuchAlgorithmException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (NoSuchPaddingException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (InvalidKeyException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (InvalidAlgorithmParameterException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (IllegalBlockSizeException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (BadPaddingException ex) {
            throw new EncriptaException(ex.getMessage());
        }
    }

    public String desencripta(String cadena) throws EncriptaException {
        String[] sBytes = cadena.split("-");
        byte[] bytes = new byte[sBytes.length];
        for (int i = 0; i < sBytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(sBytes[i], 16);
        }
        return desencripta(bytes);
    }

    public String desencripta(byte[] message) throws EncriptaException {
        try {
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest(clave.getBytes("utf-8"));
            final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }
            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, key, iv);
            final byte[] plainText = decipher.doFinal(message);
            return new String(plainText, "UTF-8");
        } catch (NoSuchAlgorithmException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (NoSuchPaddingException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (InvalidKeyException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (InvalidAlgorithmParameterException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (IllegalBlockSizeException ex) {
            throw new EncriptaException(ex.getMessage());
        } catch (BadPaddingException ex) {
            throw new EncriptaException(ex.getMessage());
        }
    }
}

