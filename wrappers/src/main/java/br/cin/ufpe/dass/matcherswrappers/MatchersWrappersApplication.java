package br.cin.ufpe.dass.matcherswrappers;

import Method.FCA_Map;
import aml.AML;
import br.cin.ufpe.dass.matchers.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import uk.ac.ox.krr.logmap2.LogMap_Full;
import yamSS.main.oaei.run.YAM;

@SpringBootApplication
@ComponentScan({"br.cin.ufpe.dass.matcherswrappers"})
public class MatchersWrappersApplication {
	public static void main(String[] args) {
		SpringApplication.run(MatchersWrappersApplication.class, args);
	}


	@Bean
	public YAM yam() {
		return YAM.getInstance();
	}

	@Bean
	public AML aml() {
		return AML.getInstance();
	}

	@Bean
	public FCA_Map fcaMap() { return new FCA_Map(); }

}
