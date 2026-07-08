package com.ali.crud.example.batch;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class EmployeeForkJoinTask extends RecursiveTask<List<User>> {

    private static final int THRESHOLD = 20;

    private List<EmployeeDump> list;
    private UserRepository userRepository;

    public EmployeeForkJoinTask(List<EmployeeDump> list,
                                UserRepository userRepository) {
        this.list = list;
        this.userRepository = userRepository;
    }

    @Override
    protected List<User> compute() {

        if (list.size() <= THRESHOLD) {
            return processRecords(list);
        }

        int mid = list.size() / 2;

        EmployeeForkJoinTask left =
                new EmployeeForkJoinTask(list.subList(0, mid), userRepository);

        EmployeeForkJoinTask right =
                new EmployeeForkJoinTask(list.subList(mid, list.size()), userRepository);

        left.fork();

        List<User> rightResult = right.compute();
        List<User> leftResult = left.join();

        List<User> result = new ArrayList<>();
        result.addAll(leftResult);
        result.addAll(rightResult);

        return result;
    }

    private List<User> processRecords(List<EmployeeDump> subList) {

        List<User> users = new ArrayList<>();
        Set<Long> processedIds = new HashSet<>();

        for (EmployeeDump dump : subList) {

            // ✅ Handle duplicate dump records
            if (processedIds.contains(dump.getId())) {
                continue;
            }

            processedIds.add(dump.getId());

            try {
                Optional<User> existingUser =
                        userRepository.findById(dump.getId());

                User user = existingUser.orElse(new User());

                // mapping
                user.setId(dump.getId());
                user.setFirstName(dump.getFirstName());
                user.setLastName(dump.getLastName());
                user.setEmailId(dump.getEmailId());
                user.setGrade(dump.getGrade());
                user.setActive(dump.getActive());

                users.add(user);

            } catch (Exception e) {
                log.error("Error processing {}", dump.getId(), e);
            }
        }

        return users;
    }
}