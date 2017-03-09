package br.cin.ufpe.dass.matchers;

import br.cin.ufpe.dass.matchers.config.ApplicationProperties;
import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.core.MatcherFeature;
import br.cin.ufpe.dass.matchers.repository.MatcherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.wdilab.coma.matching.Resolution;
import de.wdilab.coma.matching.SimilarityMeasure;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class MatchersCatalogApplication implements CommandLineRunner {

	private MatcherRepository matcherRepository;

	public MatchersCatalogApplication(MatcherRepository matcherRepository) {
		this.matcherRepository = matcherRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(MatchersCatalogApplication.class, args);

	}

	@Override
	public void run(String... strings) throws Exception {

		if (matcherRepository.findByName("COMA") == null) {
			Matcher matcher = new Matcher();
			matcher.setName("COMA");
			matcher.setVersion("3.0");
			matcher.setEndPoint("http://localhost:8081/api/coma");

			matcher.getConfigurationParameters().put("resolution", "RES2_SELFNODE");
			matcher.getConfigurationParameters().put("measure", "SIM_STR_LEVENSHTEIN_LUCENE");

			matcherRepository.save(matcher);
		}

		// @TODO povoar banco com demais matchers existentes e combinações
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		MappingJackson2HttpMessageConverter converter =
				new MappingJackson2HttpMessageConverter(mapper);
		return converter;
	}

}
