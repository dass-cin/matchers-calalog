package br.cin.ufpe.dass.matchers;

import br.cin.ufpe.dass.matchers.config.ApplicationProperties;
import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.repository.MatcherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.smu.tspell.wordnet.WordNetDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

import static java.time.ZoneId.systemDefault;
import static java.time.ZonedDateTime.ofInstant;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class MatchersCatalogApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(MatchersCatalogApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MatchersCatalogApplication.class, args);

	}

	@Override
	public void run(String... strings) throws Exception {
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

	@Bean
	public WordNetDatabase wordNetDatabase() {
		// Initialize the WordNet interface.

		WordNetDatabase wordNet = null;

		String wordnetdir = "/usr/local/Cellar/wordnet/3.1/dict";

		System.setProperty("wordnet.database.dir", wordnetdir);
		// Instantiate wordnet.
		try {
			wordNet = WordNetDatabase.getFileInstance();
		} catch (Exception e) {
			log.error("Failed to start wordnet database");
		}

		log.info("Wordnet database initialized");

		return wordNet;
	}

}
