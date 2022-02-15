package org.example;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
public class consumer1 {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers","localhost:9092");
        properties.put("group.id", "customer-consumer-group-v1");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://localhost:8081");
        properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, CustomerV1> kafkaConsumer = new KafkaConsumer<String, CustomerV1>(properties);
        String topic = "customer-avro_1";
        kafkaConsumer.subscribe(Collections.singleton(topic));

        System.out.println("Waiting for data...");

        while(true){
            System.out.println("Polling");
            //poll() returns a list of records.
            // Each record contains the topic and partition the record came from,
            // the offset of the record within the partition, and of course the key
            // and the value of the record. Typically we want to iterate over the list and process the records individually.
            ConsumerRecords<String, CustomerV1> records = kafkaConsumer.poll(Duration.ofMinutes(1));

            for (ConsumerRecord<String, CustomerV1> record : records){
                CustomerV1 customer = record.value();
                System.out.println(customer);
            }
            kafkaConsumer.commitSync();
        }
    }
}
