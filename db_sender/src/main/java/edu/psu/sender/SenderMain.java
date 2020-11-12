package edu.psu.sender;

import edu.psu.generateSQLite.tables.pojos.PermCityPolyclinic_7Registry;
import edu.psu.sender.service.PermCityPolyclinic7Service;

import javax.crypto.Cipher;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

public class SenderMain {

    public static void main(String[] args) {
        String passwd = shareKey();

        PermCityPolyclinic7Service permCityPolyclinic7Service = new PermCityPolyclinic7Service();
        QueuesSender queuesSender = new QueuesSender();
        for (PermCityPolyclinic_7Registry row:permCityPolyclinic7Service.getAllCourses()) {
            queuesSender.pushToQueue(row, passwd);
        }
    }

    private static String shareKey() {
        // String desKey = "abcde98345";
        String desKey = PropertyReader.LoadProperty("Tdes.key");
        try {
            // Взять открытый ключ Consumer
            // ServerSocket server = new ServerSocket(60000);
            ServerSocket server = new ServerSocket(Integer.parseInt(PropertyReader.LoadProperty("socket.port")));
            Socket socket = server.accept();
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            PublicKey senderPublicKey = (PublicKey) is.readObject();

            // Зашифровать сообщение с использованием открытого ключа QueuesSender
            Cipher cipher = Cipher.getInstance("RSA");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            cipher.init(Cipher.ENCRYPT_MODE, senderPublicKey);
            byte[] desPasswdEncrypted = cipher.doFinal(desKey.getBytes());

            // Отправляю зашифрованный ключ DES
            os.write(desPasswdEncrypted);
            os.flush();

            socket.close();
            server.close();
            is.close();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return desKey;
    }
}
