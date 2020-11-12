package edu.psu.receiver;

import edu.psu.generateSQLite.tables.pojos.PermCityPolyclinic_7Registry;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.jms.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.util.Arrays;

public class QueuesReceiver implements Runnable, ExceptionListener {

    private String passwd;

    QueuesReceiver(String passwd) {
        this.passwd = passwd;
    }

    public void run() {
        while (true) {
            try {
                // Создание соединения и сессии
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
                Connection connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Наименование очереди
                Destination destination = session.createQueue("rows");

                // Создание "подписчика" для очереди, указанной в сессии
                MessageConsumer consumer = session.createConsumer(destination);

                // Получение сообщения из очереди
                Message message = consumer.receive();
                System.out.println("Информация из ActiveMQ успешно сохранена в БД!");

                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] psuCoursesBytesEncoded = new byte[(int) bytesMessage.getBodyLength()];
                // Считывается массив байт из сообщения, затем он дешифруется
                bytesMessage.readBytes(psuCoursesBytesEncoded);
                byte[] psuCoursesBytesDecoded = decrypt(psuCoursesBytesEncoded, passwd);
                // Расшифрованный массив байт приводится к объекту
                PermCityPolyclinic_7Registry psuCourses = (PermCityPolyclinic_7Registry) byteArrayToObject(psuCoursesBytesDecoded);
                // Конвертация и сохранение
                DataSaver tablesConverter = new DataSaver();
                tablesConverter.convertAndInsertToDB(psuCourses);

                consumer.close();
                session.close();
                connection.close();
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onException(JMSException e) {
        e.printStackTrace();
    }

    public static Object byteArrayToObject(byte[] byteArray) {
        Object object = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInput in;
        try {
            in = new ObjectInputStream(bis);
            object = in.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return object;
    }

    public static byte[] decrypt(byte[] message, String passwd) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digest = md.digest(passwd.getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digest, 24);

        final SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        return decipher.doFinal(message);
    }
}