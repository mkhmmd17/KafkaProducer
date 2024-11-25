package com.conduktor.demos.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    public static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {
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

        // Create a producer record
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>("first_topic", "Hello, Kafka from Java!");

        // Send data asynchronously
        producer.send(producerRecord, (metadata, exception) -> {
            if (exception == null) {
                log.info("Record sent successfully! \n" +
                        "Topic: " + metadata.topic() + "\n" +
                        "Partition: " + metadata.partition() + "\n" +
                        "Offset: " + metadata.offset() + "\n" +
                        "Timestamp: " + metadata.timestamp());
            } else {
                log.error("Error while producing", exception);
            }
        });

        // Flush and close the producer
        producer.flush();
        producer.close();

        log.info("Kafka Producer has shut down.");
    }
}
