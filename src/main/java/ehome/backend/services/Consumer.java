package ehome.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import ehome.backend.dtos.QueueMeasurementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class Consumer implements ApplicationRunner {
    private final static String QUEUE_NAME = "device_data";
    private final static boolean PRODUCTION = true;

    @Autowired
    private MeasurementService measurementService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        if (PRODUCTION) {
            factory.setUsername("vumvtywu");
            factory.setPassword("6hfQs_cqd4jHZm8nJw4ZiG6E50mXIhmJ");
            factory.setHost("stingray.rmq.cloudamqp.com");
            factory.setPort(5672);
            factory.setVirtualHost("vumvtywu");
        }else {
            factory.setUsername("admin");
            factory.setPassword("anaaremere");
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setVirtualHost("/");
        }

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            QueueMeasurementDTO queueMeasurementDTO = mapper.readValue(message, QueueMeasurementDTO.class);
            measurementService.saveMeasurement(queueMeasurementDTO);
            //System.out.println(" [x] Received '" + queueMeasurementDTO.toString() + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
