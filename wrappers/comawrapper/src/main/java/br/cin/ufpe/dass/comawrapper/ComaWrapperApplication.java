package br.cin.ufpe.dass.comawrapper;

import br.cin.ufpe.dass.matchers.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"br.cin.ufpe.dass.comawrapper","br.cin.ufpe.dass.matchers"})
public class ComaWrapperApplication {
	public static void main(String[] args) {
		SpringApplication.run(ComaWrapperApplication.class, args);
	}
}
