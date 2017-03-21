package br.cin.ufpe.dass.matchers.config;

import br.cin.ufpe.dass.matchers.core.Matcher;
import br.cin.ufpe.dass.matchers.repository.MatcherRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by diego on 20/03/17.
 */
@Component
public class MatchersInit implements ApplicationListener<ApplicationReadyEvent> {

    private final MatcherRepository matcherRepository;

    public MatchersInit(MatcherRepository matcherRepository) {
        this.matcherRepository = matcherRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        if (matcherRepository.findByName("COMA") == null) {
			Matcher matcher = new Matcher();
			matcher.setName("COMA");
			matcher.setVersion("3.0");
			matcher.setEndPoint("http://localhost:8081/api/coma");

			matcher.getConfigurationParameters().put("resolution", "RES2_ALLNODES"); //
			matcher.getConfigurationParameters().put("measure", "SIM_STR_EDITDIST");

			matcherRepository.save(matcher);
		}

        // @TODO povoar banco com demais matchers existentes e combinações

    }

}
