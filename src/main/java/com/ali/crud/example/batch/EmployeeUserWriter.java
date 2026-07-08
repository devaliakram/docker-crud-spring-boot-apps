package com.ali.crud.example.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class EmployeeUserWriter implements ItemWriter<List<User>> {

    private final UserRepository userRepository;

    // ✅ Constructor Injection
    public EmployeeUserWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void write(List<? extends List<User>> chunk) throws Exception {
        log.info("Writer received chunk of size: {}", chunk.size());

        List<User> allUsers = new ArrayList<>();

        for (List<User> users : chunk) {
            if (users != null && !users.isEmpty()) {
                allUsers.addAll(users);
            }
        }

        if (!allUsers.isEmpty()) {
            userRepository.saveAll(allUsers);
            log.info("Saved {} users to database", allUsers.size());
        }
    }
}