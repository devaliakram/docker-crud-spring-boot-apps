package com.ali.crud.example.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Slf4j
public class EmployeeUserProcessor implements ItemProcessor<EmployeeDump, List<User>> {

    private final UserRepository userRepository;

    // Collect chunk data
    private List<EmployeeDump> buffer = new ArrayList<>();

    public EmployeeUserProcessor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> process(EmployeeDump item) {

        buffer.add(item);

        // Process only when buffer reaches chunk size
        if (buffer.size() >= 100) {
            return processWithForkJoin(new ArrayList<>(buffer));
        }

        return null; // wait for more data
    }

    private List<User> processWithForkJoin(List<EmployeeDump> list) {

        ForkJoinPool pool = new ForkJoinPool();

        EmployeeForkJoinTask task =
                new EmployeeForkJoinTask(list, userRepository);

        List<User> result = pool.invoke(task);

        buffer.clear(); // clear after processing

        return result;
    }
}