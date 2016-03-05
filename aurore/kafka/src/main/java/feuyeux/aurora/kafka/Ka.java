package feuyeux.aurora.kafka;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.message.MessageAndMetadata;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by erichan
 * on 16/2/22.
 */
public class Ka {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("metadata.broker.list", Conf.kafkaServerList);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        props = new Properties();
        props.put("zookeeper.connect", Conf.zkConnect);
        props.put("group.id", "my_group");
        props.put("zookeeper.session.timeout.ms", "100");
        props.put("zookeeper.sync.time.ms", "100");
        props.put("auto.commit.interval.ms", "100");
        props.put("auto.offset.reset", "smallest");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(Conf.topic, new Integer(1));
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
        KafkaStream<String, String> stream = consumerMap.get(Conf.topic).get(0);

        KeyedMessage<String, String> data = new KeyedMessage<String, String>(Conf.topic, "k", "vvvv");
        producer.send(data);
        producer.close();

        ConsumerIterator<String, String> it = stream.iterator();
        while (it.hasNext()) {
            MessageAndMetadata<String, String> data0 = it.next();
            System.out.println(data0.key() + ":" + data0.message());
        }
        consumer.shutdown();
    }
}
