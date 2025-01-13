# expenses-manager-api

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)


This is a repository with the implementation of an expense management API that uses jwt authorization, automated tests with JUnit and Mockito, and migration control.

To use this repository you need to clone the project using:
  ```bash
  git clone https://github.com/gabrielferreira02/expenses-manager-api.git
  ```
Now ropen the project with your IDE and start the application.

To access the application's resources, you must be authenticated with a user registered in the database. So, to register a user, the correct endpoint is:
  ```
  POST http://localhost:8080/auth/register
  ```
And in the body of this request, put the following code as a json format in postman, imnsonia or any other.
  ```
  {
    "username": "user",
    "password": "1234"
  }
  ```

You should receive status code 200 with the following response.
  ```
  {
    "id": "some id",
    "username": "user",
    "password": "$2a$10$8IrJdLelPQYLTago7jfoaeIJ2qtG4N4g.rE4kpwrps7GtVsdN7JeS"
  }
  ```
You need to store the user id to use it in other endpoints.

With the user registered on the database, now you can use the same data in the body of the request to login in the application with the following endpoint:
  ```
  POST http://localhost:8080/auth/login
  ```
You should received a 200 status code and in the body of the request have the following code:
  ```
  {
    "token": "your jwt token"
  }
  ```
This token is the key to access all the other resourcers in the application and it is used as a Bearer token authentication and in every request that you will make, is neccessary to put this token.

To create a new transaction use the following endpoint
  ```
  POST http://localhost:8080/transactions
  ```
In the body of the request as a json put this:
  ```
  {
    "id": "your user id",
    "value": 200.0,
    "type": "RECEIVED"
  }
  ```
An important point is that the type of transaction is validate with a Enum like this:
  ```
  {
    RECEIVED,
    PAID
  }
  ```
Only this two values can be registered on the transaction's type

You can create as many transactions as you want

To edit any transaction:
  ```
  PATCH http://localhost:8080/transactions
  ```
  ```
  {
    "id": "transaction id",
    "value": 250.0,
    "type": "RECEIVED"
  }
  ```
To delete any transaction:
  ```
  DELETE http://localhost:8080/transactions/{transaction_id}
  ```
To return a list of the transactions you have made
  ```
  GET http://localhost:8080/transactions/{user_id}
  ```
And to return a report with the total received in the current month:
  ```
  GET http://localhost:8080/transactions/report/{user_id}
  ```
This will return the following response: 
```
  {
    "total": 100.0
  }
  ```
The total is calculated subtracting the sum of the received transactions by the sum of the paid transactions.

And also, you can run the automated tests using mvn test in the terminal or using the maven manager of your IDE.
