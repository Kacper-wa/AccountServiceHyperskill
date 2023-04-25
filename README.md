# AccountServiceHyperskill


[Hyperskill project](https://hyperskill.org/projects/217) with a focus on Spring Security

## Main features

- Authentication
- Authorization
- Logging security events to database
- H2 database
- Role model
- Spring Beans, Components and Configurations

APIs and Access Levels
The following table lists the APIs included in the project and the access levels required to use them:

| API Endpoint              | Anonymous | User | Accountant | Administrator | Auditor |
|---------------------------|-----------|------|------------|---------------|---------|
| POST api/auth/signup      | +         | +    | +          | +             | +       |
| POST api/auth/changepass  |           | +    | +          | -             | -       |
| GET api/empl/payment      | -         | +    | +          | -             | -       |
| POST api/acct/payments    | -         | -    | +          | -             | -       |
| PUT api/acct/payments     | -         | -    | +          | -             | -       |
| GET api/admin/user        | -         | -    | -          | +             | -       |
| DELETE api/admin/user     | -         | -    | -          | +             | -       |
| PUT api/admin/user/role   | -         | -    | -          | +             | -       |
| PUT api/admin/user/access | -         | -    | -          | +             | -       |
| GET api/security/events   | -         | -    | -          | -             | +       |

Note that "Anonymous" refers to users who are not logged in.

Logging Events
The project includes logging events for different actions. These events can be used to monitor user activity and identify potential security issues. The following table lists the logging events and their corresponding event names:

|                          Description                         |    Event Name   |
|--------------------------------------------------------------|-----------------|
| A user has been successfully registered                      | CREATE_USER     |
| A user has changed the password successfully                 | CHANGE_PASSWORD |
| A user is trying to access a resource without access rights  | ACCESS_DENIED   |
| Failed authentication                                        | LOGIN_FAILED    |
| A role is granted to a user                                  | GRANT_ROLE      |
| A role has been revoked                                      | REMOVE_ROLE     |
| The Administrator has locked the user                        | LOCK_USER       |
| The Administrator has unlocked a user                        | UNLOCK_USER     |
| The Administrator has deleted a user                         | DELETE_USER     |
| A user has been blocked on suspicion of a brute force attack | BRUTE_FORCE     |
How to Use
To use the project, you will need to have Java and the Spring framework installed on your computer. Once you have these installed, you can download or clone the project repository and run it using an IDE such as Eclipse or IntelliJ IDEA or run it using gradle.
1. Clone repository
    ```shell
    git clone https://github.com/Kacper-wa/AccountServiceHyperskill
    ```
2. Go to project directory
    ```shell
    cd .\AccountService
    ```
3. Start project using Gradle
    ```shell
    gradle bootRun
    ```

You can then test the APIs using tools such as Postman or curl. Note that some APIs require authentication, so you will need to obtain an access (set roles) before using them.
