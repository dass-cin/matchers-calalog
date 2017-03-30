package br.cin.ufpe.dass.yamwrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import yamSS.main.oaei.run.YAM;

@SpringBootApplication
@ComponentScan({"br.cin.ufpe.dass.yamwrapper"})
public class YAMWrapperApplication {
	public static void main(String[] args) {
		SpringApplication.run(YAMWrapperApplication.class, args);
	}


	@Bean
	public YAM yam() {
		return YAM.getInstance();
	}

}
