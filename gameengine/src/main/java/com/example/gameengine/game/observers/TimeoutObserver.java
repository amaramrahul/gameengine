package com.example.gameengine.game.observers;

import com.example.gameengine.game.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.*;

/**
 * Created by rahul on 25/2/16.
 */
public class TimeoutObserver implements Observer {
    protected static final ObjectMapper mapper = new ObjectMapper();

    public void update(Observable obs, Object arg) {
        if (!(obs instanceof Game)) {
            return;
        }

        Game game = (Game) obs;
        if ((Integer) game.getState().get("isActive") == 0) {
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("id", game.getGameId());
        params.put("timeout", System.currentTimeMillis() + game.getTimeoutInterval()*1000);
        params.put("version", game.getState().get("version"));

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<Integer, String> producer = new KafkaProducer<>(props);
        try {
            producer.send(new ProducerRecord<Integer, String>(
                    game.getGameName(), game.getGameId(), mapper.writeValueAsString(params)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        producer.close();
    }
}
