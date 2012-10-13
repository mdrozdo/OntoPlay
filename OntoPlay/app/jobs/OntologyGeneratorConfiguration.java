package jobs;

import models.owlGeneration.OntologyGenerator;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

public class OntologyGeneratorConfiguration extends Job {
	@Override
	public void doJob() throws Exception {
		OntologyGenerator.initialize("file:test/AiGConditionsOntology.owl", "./test");
		super.doJob();
	}
}
