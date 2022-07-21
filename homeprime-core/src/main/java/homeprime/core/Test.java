package homeprime.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) throws ParseException {
        // TODO Auto-generated method stub
        // String publisherId = UUID.randomUUID().toString();
        // try {
        // IMqttClient publisher = new MqttClient("tcp://iot.eclipse.org:1883", publisherId);
        // // MqttConnectOptions options = new MqttConnectOptions();
        // // options.setAutomaticReconnect(true);
        // // options.setCleanSession(true);
        // // options.setConnectionTimeout(10);
        // publisher.connect();
        //
        // publisher.publish(publisherId.toString(), new MqttMessage("Contact-1:ON".getBytes()));
        //
        // } catch (MqttException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // example response: 2019-04-18 23:26:49
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date lFromDate1 = datetimeFormatter1.parse("2019-04-18 23:26:49");
        System.out.println("gpsdate :" + lFromDate1);
        System.out.println(lFromDate1.toInstant().toEpochMilli());
        System.out.println(System.currentTimeMillis());
        System.out.println("Delta: " + (System.currentTimeMillis() - lFromDate1.toInstant().toEpochMilli()));
    }

}
