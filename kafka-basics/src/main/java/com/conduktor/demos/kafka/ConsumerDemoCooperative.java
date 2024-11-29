package com.conduktor.demos.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoCooperative {

    public static final Logger log = LoggerFactory.getLogger(ConsumerDemoCooperative.class.getSimpleName());

    public static void main(String[] args) {

        String groupId = "my-java-application";
        String topic = "first_topic";
        log.info("I am Kafka Consumer");

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

        //consumer config
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");
        properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());



        //create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //shutdown hook
        final Thread mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread() {
                 public void run() {
                     log.info("Detected a shutdown, let's exit by calling consumer.wake()...");
                     consumer.wakeup();

                     // join the main thread to allow the execution of the code in the main thread
                     try {
                         mainThread.join();
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
        });

        //subscribe to a topic


        try {
            consumer.subscribe(Arrays.asList(topic));
            // poll for data
            while (true) {
                log.info("Waiting for messages. Beginning data received...");

                ConsumerRecords<String, String> records = consumer.poll(1000);

                for (ConsumerRecord<String, String> record : records) {
                    log.info(record.key() + ": " + record.value());
                    log.info(record.topic() + ": " + record.partition() + ": " + record.offset());
                }
            }
        } catch(WakeupException e) {
            log.info("Consumer is starting to shut down");
        } catch (Exception e) {
            log.error("Unexpected exception", e);
        } finally {
            consumer.close(); //close the consumer, this will also commit offsets
            log.info("Consumer is shut down");
        }
    }
}