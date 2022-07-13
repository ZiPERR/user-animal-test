# API documentation

Перед работой вам нужно указать свои данные в application.properties, такие как: название базы данных
**postgre_database**, имя и пароль пользователя Postgre **username**, **password**

Username Available ```GET http://localhost:8080/api/usernameAvailability/{username}/isUsernameAvailable```

Register ```POST http://localhost:8080/api/register```
```json
{
  "username": "username",
  "password": "password"
}
```


Login ```POST http://localhost:8080/login```
```json 
{ 
    "username": "username",
    "password": "password"
}
```

Get All Users ```GET http://localhost:8080/api/user/getAllUsers```

Get All Animals ```GET http://localhost:8080/api/animal/getAllAnimals```

Get One User ```GET http://localhost:8080/api/user/{userId}/get```

Get One Animal ```GET http://localhost:8080/api/animal/{animalId}/get```

Add Animal To Common List ```POST http://localhost:8080/api/animal/create_process```
```json
{
  "species": "species",
  "birthDate": "birthDate(yyyy-MM-dd)",
  "sex": "sex",
  "nickname": "nickname"
}
```

Add Animal to User's List ```GET http://localhost:8080/api/user/{userId}/animal/{animalId}/add```

Get User's Animals ```GET http://localhost:8080/api/user/{userId}/animal/getUserAnimals```

Delete Animal From Common List ```DELETE http://localhost:8080/api/animal/{animalId}/delete```

Delete Animal From User's List ```DELETE http://localhost:8080/api/user/{userId}/animal/{animalId}/delete```

Edit Animal ```POST http://localhost:8080/api/animal/{animalId}/edit ```

```json
{
  "species": "newSpecies",
  "birthDate": "newBirthDate(yyyy-MM-dd)",
  "sex": "newSex",
  "nickname": "newNickname"
}
```



