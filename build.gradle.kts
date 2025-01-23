plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}


group = "kr.hhplus.be"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}

dependencies {
    // Spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
	implementation("org.projectlombok:lombok")

	// redisson
	implementation("org.redisson:redisson-spring-boot-starter:3.18.0")

	implementation ("org.springframework.boot:spring-boot-starter-aop")
	// DB
	runtimeOnly("com.mysql:mysql-connector-j")
	// runtimeOnly("com.h2database:h2")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.testcontainers:mysql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("user.timezone", "UTC")
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-parameters")
}
