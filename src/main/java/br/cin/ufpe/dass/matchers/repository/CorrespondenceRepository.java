package br.cin.ufpe.dass.matchers.repository;

import br.cin.ufpe.dass.matchers.core.Correspondence;
import br.cin.ufpe.dass.matchers.core.Ontology;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by diego on 07/03/17.
 */
public interface CorrespondenceRepository extends MongoRepository<Correspondence, String> {

}
