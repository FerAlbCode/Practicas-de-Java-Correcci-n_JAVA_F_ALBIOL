package firma;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
public class firma {
    public static void main (String[] args) throws Exception {
            File archivo = new File("mensaje.txt");
            KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
            generador.initialize(2048);

            //SE CREA EL PAR DE CLAVES PRIVADA Y PUBLICA
            KeyPair par = generador.generateKeyPair();
            PrivateKey clavepriv = par.getPrivate();
            PublicKey clavepub = par.getPublic();

            Path archivoCifrado = FirmaGenerada(archivo,clavepriv);
            Boolean validar = Firmacomprobada(archivo,clavepub,archivoCifrado);

            if(validar) {
                System.out.println("FIRMA VERIFICADA CON CLAVE PUBLICA");
            }
            else {
                System.out.println("FIRMA NO VERIFICADA");
            }
        }

    private static Path FirmaGenerada(File archivo, PrivateKey clavepriv) throws Exception {
        String codigoHash = ObtenerHash(archivo);
        Path tempFile = Files.createTempFile("temp","txt");
        Files.writeString(tempFile,codigoHash);
        byte[] fileBytes = Files.readAllBytes(tempFile);
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(1,clavepriv);
        byte[] encryptedFileBytes = encryptCipher.doFinal(fileBytes);
        FileOutputStream stream = new FileOutputStream(tempFile.toFile());

        try {
            stream.write(encryptedFileBytes);
        } catch (Throwable var11) {
            try {
                stream.close();
            } catch (Throwable var10) {
                var11.addSuppressed(var10);
            }
            throw var11;
        }
        stream.close();
        return tempFile;
    }

    private static Boolean Firmacomprobada(File archivoOriginal, PublicKey clavepub, Path firmado) throws Exception {
        boolean validar = false;
        String codigoHashOriginal =ObtenerHash(archivoOriginal);
        byte[] encryptedFileBytes = Files.readAllBytes(firmado);
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(2, clavepub);
        byte[] decryptedFileBytes = decryptCipher.doFinal(encryptedFileBytes);
        FileOutputStream stream = new FileOutputStream(firmado.toFile());
        try{
            stream.write (decryptedFileBytes);
        } catch (Throwable var12) {
            try {
                stream.close();
            } catch (Throwable var11) {
                var12.addSuppressed(var11);
            }
            throw var12;
        }
        stream.close();
        String var13 = Files.readString(firmado);
        if (var13.contentEquals(codigoHashOriginal)) {
            validar = true;
        }
        return validar;
    }

    private static String ObtenerHash(File archivo) throws IOException, NoSuchAlgorithmException {
        BufferedReader bufferLectura = new BufferedReader(new FileReader(archivo));
           String textoMensaje;
           String linea;
           for (textoMensaje = ""; (linea = bufferLectura.readLine()) != null; textoMensaje = textoMensaje + linea) {}

           MessageDigest digest = MessageDigest.getInstance("SHA-256");
           byte[] encodedhash= digest.digest(textoMensaje.getBytes(StandardCharsets.UTF_8));
           return bytesToHex(encodedhash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for(int i = 0; i < hash.length; ++i) {
            String hex = Integer.toHexString(255 & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
