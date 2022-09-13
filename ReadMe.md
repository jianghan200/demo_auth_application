 # Requirement 

As required by the interviewer, only JDK allowed and no third-party library.


### Some key point
- Token generation: UUID based
- Password encryption : native md5
- Test: functional test


# Deserialization

Since not third party library is allowed, so a self-defined object serialization format is defined

The program accept request body and converse to java object via `ObjectParser` class

User is serialized as ```roles=[{1:admin},{2:operation}]&id=1&username=hi&password=hello```
Role is an object that has id and name, it is serialized as {1:admin} 
Roles is a set of role, it is serialized as [{1:admin},{2:operation}] 


## API cookbook

### Create user

`POST /user`

Request body with following text, self defined object serialization 

```roles=[{1:admin},{2:operation}]&id=1&username=hi&password=hello```



### DELETE user
`DELETE /user`

Request body with text, self defined object serialization

```id=1&username=hi&password=hello&roles=[{1:admin},{2:operation}]```

### Create role
`POST /role`

POST Body with text, self defined object serilization :

```{1:admin}```

### Delete role
REST DELETE method, with body of role object
`DELETE /role`
POST Body with text, self defined object serilization :
```{1:admin}```


### Add role to user
REST PATCH method, with key "Token" and it's value in request header
`PATCH /user/add_role`

Request body with text like following:
```{1:admin}```


### Authenticate 
`POST /login`
Request body with following text:
`username=john&password=pass123`

Response is the token value


### Invalidate
`DELETE /login`
Request body with following token's value:
`1663056279809-3a9f3ac5-280f-42bb-9c2e-c1ef2980e6b1`


### Check role
`POST /user/check_user_with_role`
With key "Token" and it's value in request header

Request Body with text :
```{1:admin}```
Response is the token value


### Get all roles for a user
`GET /user/roles`
With key "Token" and it's value in request header
Response is all roles of user


# Test
Since not JUnit is allowed, the project use simple funcational testing


