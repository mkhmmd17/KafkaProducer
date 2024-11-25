package com.conduktor.demos.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {

    public static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());

    public static void main(String[] args) throws InterruptedException {
        log.info("Starting Kafka Producer");

        // Create producer properties
        Properties properties = new Properties();

        // Kafka cluster connection settings
        properties.setProperty("bootstrap.servers", "pkc-619z3.us-east1.gcp.confluent.cloud:9092");
        properties.setProperty("security.protocol", "SASL_SSL"); // Correct protocol
        properties.setProperty("sasl.mechanism", "PLAIN");

        // Inline JAAS configuration
        properties.setProperty("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                        "username=\"LWFF2XF2MS4SXMOP\" " +
                        "password=\"UM4XUAujhKjU7NKwtgNln5nUDdzUbs6oV9SFnkFAqBWZU2umnyU7IMoP3LIfrRmR\";");

        // Serializer settings
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // Create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {

                String topic = "first_topic";
                String key = "id_" + i;
                String value = "hello world" + i;
                // Create a producer record
                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<>(topic, key, value);

                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metaData, Exception e) {
                        //executes every time a record successfully sent or an exception is thrown
                        if (e == null) {
                            // the record was successfully send
                            log.info("Key: " + key + " | Partition: " + metaData.partition());
                        } else {
                            log.error("Error while sending message", e);
                        }
                    }
                });
            }


            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // Flush and close the producer
        producer.flush();
        producer.close();

        log.info("Kafka Producer has shut down.");
    }
}
