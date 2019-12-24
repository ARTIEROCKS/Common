package artie.sensor.common.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import artie.sensor.common.enums.ConfigurationEnum;

@Configuration
public class SenderConfig {

	@Bean
	public Map<String, Object> producerConfigs(Map<String,String> configuration){
		
		String kafkaServer = configuration.get(ConfigurationEnum.KAFKA_SERVER.toString());
		
		Map<String,Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		return props;
		
	}
	
	@Bean
	public ProducerFactory<String, String> producerFactory(Map<String,String> configuration) {
		return new DefaultKafkaProducerFactory<>(producerConfigs(configuration));
	}
	
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(Map<String,String> configuration) {
		return new KafkaTemplate<>(producerFactory(configuration));
	}
	
}
