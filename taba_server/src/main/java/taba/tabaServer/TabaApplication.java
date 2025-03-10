package taba.tabaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import taba.tabaServer.tabaserver.security.config.JwtProperties;
//import taba.tabaServer.tabaserver.security.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class TabaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TabaApplication.class, args);
	}
}
