# spring-boot-crud-example

This is a basic **Spring Boot CRUD application** designed to manage data through a **MySQL** database. All core database configurations (URL, username, and password) are currently managed within the `application.properties` file.
create a read file for spring boot docker based apps with this content - # spring-boot-crud-example


A. ) **This is basic spring boot crud application which will deal with mysql db
having their configuration is added in application.properties file(db config)

THIS CHANGED ARE INTO master BRANCH**

----------------------------------

Research- 1
EXception which I was getting like  - com.mysql.cj.jdbc.exceptions.**CommunicationsException**: **Communications link failure**
                                     | Caused by: com.mysql.cj.exceptions.**CJCommunicationsException**: Communications link failure
                                       Caused by: java.net.**ConnectException**: Connection refused (Connection refused)
This confirms the "Race Condition" . Because your Spring Boot version is older (2.2.4), it d**oesn't have built-in retry logic**. It tries to connect to MySQL the millisecond the container starts, 

but MySQL is still busy "initializing" its internal files.

The Permanent Professional Fix
If you want to avoid this error every time you run up, you should add a Healthcheck. This forces the application to sit and wait until MySQL is actually "healthy" (not just started, but ready for queries).
Use this yaml file - so the spring boot apps will wait until mysql conatiner to start 

        version: "3.8"
        
        ``services:
        mysql-db:
        image: 'mysql:latest'
        environment:
        MYSQL_ROOT_PASSWORD: admin12
        MYSQL_DATABASE: crud
        ports:
        - '3307:3306'
        # --- ADDED: This checks if MySQL is actually ready ---
        healthcheck:
        test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-padmin12"]
        interval: 5s
        timeout: 5s
        retries: 10
        
        application:
        build:
        context: .
        dockerfile: Dockerfile
        image: docker-spring-boot:1.0
        depends_on:
        mysql-db:
        condition: service_healthy # --- ADDED: App will now WAIT for the healthcheck ---
        ports:
        '9292:9192'
        environment:
        SPRING_DATASOURCE_URL: 'jdbc:mysql://mysql-db:3306/crud?allowPublicKeyRetrieval=true&useSSL=false'
        SPRING_DATASOURCE_USERNAME: root
        SPRING_DATASOURCE_PASSWORD: admin12

A.)
or you can also used this yaml file ( in this case you have to re run the boot apps so that mysql should start first then run the spring boot apps)

        `version: "3.8"
        
        services:
        mysql-db:
        image: 'mysql:latest'
        environment:
        MYSQL_ROOT_PASSWORD: admin12
        MYSQL_DATABASE: crud
        ports:
        - '3307:3306'
        
        application:
        build:
        context: .
        dockerfile: Dockerfile
        image: docker-spring-boot:1.0
        depends_on:
        - mysql-db
        ports:
          - '9292:9192'
          environment:
          SPRING_DATASOURCE_URL: 'jdbc:mysql://mysql-db:3306/crud?allowPublicKeyRetrieval=true&useSSL=false'
        # CHANGE THIS FROM admin TO root
        SPRING_DATASOURCE_USERNAME: admin
        SPRING_DATASOURCE_PASSWORD: admin12`


--------------------

Research- 2

Always take username for mysql as **root**, we are talking username of mysql other than root it will gives an exception - 

    java.sql.SQLException: Access denied for user 'admin'@'172.18.0.3' (using password: YES)