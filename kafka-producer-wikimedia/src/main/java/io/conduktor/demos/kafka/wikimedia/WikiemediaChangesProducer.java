package io.conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikiemediaChangesProducer {
    public static void main(String[] args) throws InterruptedException {
//        String bootstrapServers = "pkc-619z3.us-east1.gcp.confluent.cloud:9092";
        //create Producer properties
        Properties properties = new Properties();

        // Kafka cluster connection settings
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //CONFLUENT CLUSTER
//        properties.setProperty("bootstrap.servers", bootstrapServers);
//        properties.setProperty("security.protocol", "SASL_SSL"); // Correct protocol
//        properties.setProperty("sasl.mechanism", "PLAIN");
//
//        // Inline JAAS configuration
//        properties.setProperty("sasl.jaas.config",
//                "org.apache.kafka.common.security.plain.PlainLoginModule required " +
//                        "username=\"LWFF2XF2MS4SXMOP\" " +
//                        "password=\"UM4XUAujhKjU7NKwtgNln5nUDdzUbs6oV9SFnkFAqBWZU2umnyU7IMoP3LIfrRmR\";");
//
//        // Serializer settings
//        properties.setProperty("key.serializer", StringSerializer.class.getName());
//        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // set high throughput producer configs
//        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
//        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
//        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");


        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "wikimedia.recentchange";

        EventHandler eventHandler = new WikimediaChangeHandler(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();


        // start the producer in another thread
        eventSource.start();

        // we produce for 10 minutes and block the pgogram until then
        TimeUnit.MINUTES.sleep(10);
    }
}
