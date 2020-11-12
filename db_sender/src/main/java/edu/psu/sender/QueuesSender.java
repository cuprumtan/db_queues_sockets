package edu.psu.sender;

import edu.psu.generateSQLite.tables.pojos.PermCityPolyclinic_7Registry;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.jms.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;

public class QueuesSender {

    public void pushToQueue(PermCityPolyclinic_7Registry row, String passwd) {

        try {
            // Создание соединения и сессии
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Наименование очереди
            Destination destination = session.createQueue("rows");

            // Создание "поставщика" для очереди, указанной в сессии
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Создание сообщения, шифрование и отправка в очередь
            BytesMessage message = session.createBytesMessage();
            byte[] permCityPolyclinicRegistry = objectToByteArray(row);
            byte[] permCityPolyclinicRegistryEncoded = encrypt(permCityPolyclinicRegistry, passwd);
            message.writeBytes(permCityPolyclinicRegistryEncoded);
            producer.send(message);

            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] objectToByteArray(Object object) {
        byte[] byteObject = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            byteObject = bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return byteObject;
    }

    public static byte[] encrypt(byte[] message, String passwd) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digest = md.digest(passwd.getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digest, 24);

        final SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return cipher.doFinal(message);
    }
}
