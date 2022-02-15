package org.example;


import com.timgroup.statsd.Event;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.serialization.StringSerializer;


import java.util.List;
import java.util.Properties;

public class producer1 {
    public static String topic1="customer-avro_1";
    public static void main(String [] args) throws InterruptedException {

        Properties props=new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("key.serializer", StringSerializer.class.getName());
        props.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        props.setProperty("schema.registry.url", "http://localhost:8081");
        KafkaProducer<String,CustomerV1> producer=new KafkaProducer<String, CustomerV1>(props);


        for(int i=0;i<10;i++) {
            CustomerV1 v1= CustomerV1.newBuilder()
                    .setFirstName("Sneh"+i)
                    .setMiddleName("-")
                    .setLastName("Yadav")
                    .setAddress("Noida")
                    .setPhoneNumber("1234567890")
                    .setHeight(5.6f)
                    .setWeight(00)
                    .build();
            ProducerRecord<String, CustomerV1> record = new ProducerRecord<String, CustomerV1>(topic1, v1);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("Success");
                        System.out.println(metadata.toString());
                    } else {
                        exception.printStackTrace();
                    }
                }
            });

            Thread.sleep(10000);
        }
        producer.flush();
        producer.close();
    }

}