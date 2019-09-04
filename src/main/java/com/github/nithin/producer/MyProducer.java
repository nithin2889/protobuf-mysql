package com.github.nithin.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class MyProducer {

    private static Logger logger = LoggerFactory.getLogger(MyProducer.class.getName());

    public static void main(String[] args) {
        // Create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create the Producer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        // Create a Producer Record
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>("first_topic", "Hello World");

        // Send data - Asynchronous
        kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
            // executes every time a record is successfully sent or an exception is thrown
            if (e == null) {
                // record was successfully sent
                logger.info("Received new metadata \n" +
                    "Topic: " + recordMetadata.topic() + "\n" +
                    "Partition: " + recordMetadata.partition() + "\n" +
                    "Offset: " + recordMetadata.offset() + "\n" +
                    "Timestamp: " + recordMetadata.timestamp());
            } else {
                e.printStackTrace();
            }
        });

        // flush data
        kafkaProducer.flush();
        // flush and close producer
        kafkaProducer.close();
    }

}