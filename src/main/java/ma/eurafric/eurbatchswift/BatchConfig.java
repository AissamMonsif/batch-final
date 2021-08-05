package ma.eurafric.eurbatchswift;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import ma.eurafric.eurbatchswift.Repository.SwiftDetailsRepository;
import ma.eurafric.eurbatchswift.entities.Swift;
import ma.eurafric.eurbatchswift.entities.SwiftReader;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Value("input/swift*.txt")
	private Resource[] inputResources;

	@Autowired
	EntityManagerFactory emf;
	
	@Autowired
	private SwiftDetailsRepository swiftDetailsRepository;
	

	private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);
	
	@Bean
	public MultiResourceItemReader<SwiftReader> multiResourceItemReader() {
		MultiResourceItemReader<SwiftReader> resourceItemReader = new MultiResourceItemReader<SwiftReader>();
		resourceItemReader.setResources(inputResources);
		resourceItemReader.setDelegate(reader());
		return resourceItemReader;
	}

	@Bean
	public FlatFileItemReader<SwiftReader> reader() {

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter("$");

		tokenizer.setNames(new String[] { "field1", "field2" });
		tokenizer.setStrict(false);

		DefaultLineMapper<SwiftReader> lineMapper = new DefaultLineMapper<SwiftReader>();
		lineMapper.setLineTokenizer(tokenizer);

		return new FlatFileItemReaderBuilder<SwiftReader>().name("swiftItemReader")

				.lineTokenizer(tokenizer).fieldSetMapper(new BeanWrapperFieldSetMapper<SwiftReader>() {
					{
						setTargetType(SwiftReader.class);
					}
				}).build();
	}

	@Bean
	public SwiftItemProcess processorSwift() {
		return new SwiftItemProcess();
	}

	@Bean
	public JpaItemWriter<Swift> writer() {
		JpaItemWriter<Swift> writer = new JpaItemWriter<Swift>();
		writer.setEntityManagerFactory(emf);
		return writer;
	}

	@Bean
	public Step chargementSwifts() {
		return stepBuilderFactory.get("chargementSwifts").<SwiftReader, Swift>chunk(10).reader(multiResourceItemReader())
				.processor(processorSwift()).writer(writer()).build();
	}

	@Bean
	public Job importSwiftJob(JobExecutionListener listener) {
		return jobBuilderFactory.get("importSwiftJob").incrementer(new RunIdIncrementer()).listener(listener)
				.flow(chargementSwifts()).end().build();
	}
	
	@Bean
	public JobExecutionListener listener() {
		return new JobExecutionListener() {

			@Override
			public void beforeJob(JobExecution jobExecution) {
				log.info("!!!!! STARTING !!!!!");
			}

			@Override
			public void afterJob(JobExecution jobExecution) {
				if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
					log.info("!!! JOB FINISHED! Time to verify the results");
					swiftDetailsRepository.findAll()
							.forEach(swiftDetailsRepository -> log.info("Found <" + swiftDetailsRepository.toString() + "> in the database."));
					log.info("!!!! SUCCESS !!!!");
				}
			}
		};
	}

}
