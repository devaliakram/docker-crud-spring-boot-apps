package com.ali.crud.example.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

    @Autowired
    private EmployeeDumpRepository employeeDumpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeUserWriter employeeUserWriter;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // ✅ Reader
    @Bean
    public RepositoryItemReader<EmployeeDump> employeeReader() {
        RepositoryItemReader<EmployeeDump> reader = new RepositoryItemReader<>();

        Map<String, Sort.Direction> sort = new HashMap<>();
        sort.put("id", Sort.Direction.ASC);

        reader.setRepository(employeeDumpRepository);
        reader.setMethodName("findAll");
        reader.setSort(sort);

        return reader;
    }

    // ✅ Processor
    @Bean
    public EmployeeUserProcessor employeeProcessor() {
        return new EmployeeUserProcessor(userRepository);
    }

    // ✅ Step
    @Bean
    public Step employeeStep() {
        return stepBuilderFactory.get("employeeStep")
                .<EmployeeDump, List<User>>chunk(100)
                .reader(employeeReader())
                .processor(employeeProcessor())
                .writer(employeeUserWriter)
                .build();
    }

    // ✅ Job
    @Bean
    public Job employeeJob() {
        return jobBuilderFactory.get("employeeJob")
                .start(employeeStep())
                .build();
    }
}