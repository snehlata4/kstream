package org.example;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import jdk.jfr.StackTrace;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
//import org.apache.kafka.streams.kstream.KStreamBuilder;
import java.util.Properties;

public class WordCount {

    public static void main(String[] args) throws InterruptedException {

        // When configuring the default serdes of StreamConfig
        try {
            final Properties props = new Properties();
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "myApplicationName_6");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());
            props.setProperty("schema.registry.url", "http://localhost:8081");
            props.put("key.subject.name.strategy", "io.confluent.kafka.serializers.subject.TopicNameStrategy");
            props.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy");

            props.put(ConsumerConfig.GROUP_ID_CONFIG, "customer-consumer-group-v2");
           // props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

//        props.put("auto.commit.enable", "false");
//        props.setProperty("specific.avro.reader", "true");

            StreamsBuilder builder = new StreamsBuilder();

            KStream<String, CustomerV1> stream = builder.stream("customer-avro_1");
            stream.foreach((key, value) -> {
                System.out.println("================");
                System.out.println(key);
                System.out.println(value);
            });

            KafkaStreams streams = new KafkaStreams(builder.build(), props);
            streams.start();

            // usually the stream application would be running forever,
            // in this example we just let it run for some time and stop since the input data is finite.
            Thread.sleep(5000L);
            streams.close();
        }
        catch (Exception e){
           System.out.println(e.getMessage());
        }
//        StreamsBuilder streamBuilder = new StreamsBuilder();
//        streamBuilder.stream("customer-avro_1")
//                        .foreach((key, value)-> {
//                            System.out.printf("key = %s", key);
//                        });
//
//        KafkaStreams testKafkaStream = new KafkaStreams(streamBuilder.build(), streamsConfiguration);
//
//
//        testKafkaStream.start();

//        System.out.println(testKafkaStream.toString());

//        Runtime.getRuntime().addShutdownHook(new Thread(testKafkaStream::close));
    }
}
