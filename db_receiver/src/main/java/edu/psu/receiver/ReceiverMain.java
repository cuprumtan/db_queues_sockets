package edu.psu.receiver;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class ReceiverMain {

    public static void main(String[] args) {
        String passwd = getKey();
        Thread thread = new Thread(new QueuesReceiver(passwd));
        thread.setDaemon(false);
        thread.start();
    }

    private static String getKey() {
        try {
            // Отправляю публичный ключ RSA
            Cipher cipher = Cipher.getInstance("RSA");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair kp = keyGen.genKeyPair();
            Socket client = new Socket(PropertyReader.LoadProperty("socket.host"), Integer.parseInt(PropertyReader.LoadProperty("socket.port")));
            ObjectOutputStream os = new ObjectOutputStream(client.getOutputStream());
            os.writeObject(kp.getPublic());
            // Отправить
            os.flush();

            // Принять зашифрованное сообщение
            InputStream is = client.getInputStream();
            byte[] desKeyEncrypted = new byte[256];
            is.read(desKeyEncrypted);

            // Применить закрытый ключ для дешифрования сообщения:
            cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
            byte[] desKeyDecrypted = cipher.doFinal(desKeyEncrypted);

            os.close();
            is.close();
            client.close();
            return new String(desKeyDecrypted, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
