package com.github.farrellw;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) {
        Logger logger =  LoggerFactory.getLogger(Consumer.class.getName());

        Dotenv dotenv = Dotenv.load();
        String gcpBootstrapServer = dotenv.get("BOOTSTRAP_SERVER");
        String topic = "reviews";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, gcpBootstrapServer);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "consumer-application-id");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        // subscribe consumer
        consumer.subscribe(Arrays.asList(topic));

        // poll for new data
        while(true){
            Duration duration = Duration.ofMillis(100);
            ConsumerRecords<String, String> records = consumer.poll(duration);
            for(ConsumerRecord<String, String> record : records){
                logger.info("Key: " + record.key() + " ,Value: " + record.value());
//                logger.info("Partition: " + record.partition() + " ,Offset " + record.offset());
            }
        }
    }
}