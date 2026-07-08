package com.ali.crud.example.batch;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_master")
@Data
public class User {

    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String emailId;
    private String grade;
    private Integer active;
}