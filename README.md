# PackagesAPI

To run your PackagesAPI app from a command line in a Terminal window you can execute the
following command

`java -jar api-0.0.1-SNAPSHOT.jar`

Endpoints

To retrieve all the packages
`GET http://localhost:8080/packages`

To retrieve a single package
`GET http://localhost:8080/packages/{id}`

To retrieve a single package in specific currency
`GET http://localhost:8080/packages/{id}?currency=GBP`

To update the existing package
`PUT http://localhost:8080/packages/{id}`

Request Body for update -
```
{
    "name": "pkg1",
    "description": "sdadsadasd",
    "id": "c3f7bc116032472f8df184fd025c06cb",
    "products": [
        {
            "name": "Shield",
            "usdPrice": 1149,
            "id": "VqKb4tyj9V6i"
        },
        {
            "name": "Helmet",
            "usdPrice": 999,
            "id": "DXSQpv6XVeJm"
        }
    ]
}
```

To delete the existing package
`DELETE http://localhost:8080/packages/{id}`

To create a new package
`POST http://localhost:8080/packages`

Request Body for create -
```
{
    "name": "pkg1",
    "description": "sdadsadasd",
    "products": [
        {
            "name": "Shield",
            "usdPrice": 1149,
            "id": "VqKb4tyj9V6i"
        },
        {
            "name": "Helmet",
            "usdPrice": 999,
            "id": "DXSQpv6XVeJm"
        }
    ]
}
```




