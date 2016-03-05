package feuyeux.aurora.kafka;

public interface Conf {
    String zkConnect = "192.168.3.103:2181,192.168.3.103:2182,192.168.3.103:2183";
    String kafkaServerList = "192.168.3.103:9091,192.168.3.103:9092,192.168.3.103:9093";
    String topic = "ktalk";
    int zkTimeout = 10000;
}