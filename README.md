# Overview

This project is a simple CRUD application. 
Allows you to perform a set of operations to manage students and tutors.

After launching the application, three tables will be created in the database: students, tutors, contact_details.

<hr>

# Setup project and run the application

__Clone:__ git clone https://github.com/Krekerok01/crud-app.git

__Run:__ 
+ ```cd ./crud-app```
+ enter ```docker compose up -d --build```

__Stop:__
+ enter ```docker compose down```

<hr>

# Usage

## Create:

* For create new student use: POST ``` http://localhost:8081/students/create```

  With such JSON body:

    ```
    {
        "name":"Alexandra",
        "age":29,
        "phone":"+375299999999",
        "email":"alexandra@gmail.com"
    }
  ```
  
* For create new tutor use: POST ``` http://localhost:8081/tutors/create```

  With such JSON body:

    ```
    {
        "name":"John",
        "specialization":"Modern art",
        "phone":"+375288888888",
        "email":"john@gmail.com"
    }
  ```

## Read:

### Students

* To find all students use: GET ```http://localhost:8081/students/get```
* To find specific student by id use: GET ```http://localhost:8081/students/get?id=<id>```

Also, you can use other search parameters or a combination of them: name, age, phone, email.


### Tutors

* To find all tutors use: GET ```http://localhost:8081/tutors/get```
* To find specific tutor by id use: GET ```http://localhost:8081/tutors/get?id=<id>```
 
Also, you can use other search parameters or a combination of them: name, specialization, phone, email.


## Update:

* For update student use: PUT ``` http://localhost:8081/students/update```

  With such JSON body:

    ```
     {
        "id":1
        "name":"Alexandra",
        "age":30,
        "phone":"+375299999999",
        "email":"new.alexandra@gmail.com"
    }
  ```
  
* For update tutor use: PUT ``` http://localhost:8081/tutors/update```

  With such JSON body:

    ```
     {
        "id":1
        "name":"John",
        "specialization":"Dancing",
        "phone":"+375333333333",
        "email":"new.john@gmail.com"
    }
  ```

## Delete:

* For delete student use: DELETE ```http://localhost:8081/students/delete?id=1```
* For delete tutor use: DELETE ```http://localhost:8081/tutors/delete?id=1```