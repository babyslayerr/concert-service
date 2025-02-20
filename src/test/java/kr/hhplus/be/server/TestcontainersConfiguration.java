package kr.hhplus.be.server;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
class TestcontainersConfiguration {

	public static final MySQLContainer<?> MYSQL_CONTAINER;
	public static final GenericContainer<?> REDIS_CONTAINER;
	public static final KafkaContainer KAFKA_CONTAINER;


	static {
		// MYSQL Container
		MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
			.withDatabaseName("hhplus")
			.withUsername("test")
			.withPassword("test");
		MYSQL_CONTAINER.start();

		System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
		System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
		System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());

		// Redis Container
		REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("bitnami/redis:7.4"))
				.withExposedPorts(6379)  // default redis port
				.withEnv("ALLOW_EMPTY_PASSWORD", "yes")
				.withEnv("REDIS_DISABLE_COMMANDS", "FLUSHDB,FLUSHALL");
		REDIS_CONTAINER.start();

		String redisHost = REDIS_CONTAINER.getHost();
		Integer redisPort = REDIS_CONTAINER.getFirstMappedPort();

		System.setProperty("spring.data.redis.host", redisHost);
		System.setProperty("spring.data.redis.port", redisPort.toString());

		KAFKA_CONTAINER = new KafkaContainer("apache/kafka-native:3.8.0");


		KAFKA_CONTAINER.start();
		System.setProperty("spring.kafka.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());

	}

	@PreDestroy
	public void preDestroy() {
		if (MYSQL_CONTAINER.isRunning()) {
			MYSQL_CONTAINER.stop();
		}
		if (REDIS_CONTAINER.isRunning()) {
			REDIS_CONTAINER.stop();
		}
		if (KAFKA_CONTAINER.isRunning()) {
			KAFKA_CONTAINER.stop();
		}
	}
}