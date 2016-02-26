package com.example.gameengine.timeoutmonitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Created by rahul on 25/2/16.
 */
public class TimeoutMonitor {
    private static final ObjectMapper mapper = new ObjectMapper();

    Integer partitionId; // partition to monitor
    TimeoutGameEventHandler timeoutGameEventHandler;

    public TimeoutMonitor(Integer partitionId) {
        this.partitionId = partitionId;
        timeoutGameEventHandler = new TimeoutGameEventHandler();
    }

    public void monitor() throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(props);
        consumer.assign(Arrays.asList(new TopicPartition("timeout", this.partitionId)));
        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(100);
            for (ConsumerRecord<Integer, String> record : records) {
                System.out.printf("offset = %d, key = %s", record.offset(), record.key());
                Map<String, Object> params = mapper.readValue(record.value(), Map.class);
                Long timeToSleep = (Long) params.get("timeout") - System.currentTimeMillis();
                if (timeToSleep > 0) {
                    try {
                        Thread.sleep(timeToSleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    timeoutGameEventHandler.processRequest(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TimeoutMonitor timeoutMonitor = new TimeoutMonitor(Integer.parseInt(args[0]));
        timeoutMonitor.monitor();
    }
}
