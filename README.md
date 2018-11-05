# README #

Backend for loopr iOS app.

## API

config only supports three params: language, currency, lrc_fee_ratio.

### GET user config by account_token
```
curl -X GET \
  'https://www.loopring.mobi/api/v1/users?account_token=12345611'
```
Response
```
// If exist
{
    "deleted": false,
    "updated_at": "2018-10-31 22:36:18.803871",
    "lrc_fee_ratio": 0.01231,
    "account_token": "12345611",
    "created_at": "2018-10-31 22:36:06.523953",
    "language": "zh-hans",
    "currency": "usd",
    "id": 4,
    "config": "{\"wallet1\":\"95\",\"cat\":\"WP\"}"  // json string. You need to parse the string to JSON
}

// If no exist
{
    "success": false,
    "message": "no account token"
}
```

### POST user config
We don't support PUT method. If you want to update a config of an account_token, please also use POST method. The config will be updated.
```
curl -X POST \
  https://www.loopring.mobi/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{
	"account_token": "12345611",
	"language": "zh-Hans",
	"currency": "USD",
	"lrc_fee_ratio": 0.00231,
	"config": "{\"phonetype\":\"N95\",\"cat\":\"WP\"}"  // json string. You need to cast JSON to json string.
}'
```

Response
```
{
    "success": true
}
```

### DELETE user config by account_token
```
curl -X DELETE \
  http://www.loopring.mobi/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{
	"account_token": "12345611"
}'
```
Response
```
{
    "success": true
}
```


### GET app versions
```
curl -X GET \
  http://www.loopring.mobi/api/v1/app_versions 
```

```
{
    "count": 1,
    "app_versions": [
        {
            "updated_at": "2018-10-30 16:11:50.849532",
            "must_update": false,
            "created_at": "2018-10-30 16:11:50.849532",
            "description": "New version",
            "id": 7,
            "version": "0.9.15"
        }
    ]
}
```

## Environment variables
To run on local machine:

* RDS_HOSTNAME
* RDS_PORT
* RDS_DB_NAME
* RDS_USERNAME
* RDS_PASSWORD
```
========== RDS INFO ==========
RDS_HOSTNAME   : localhost
RDS_PORT       : 5432
RDS_DB_NAME    : ebdb
RDS_USERNAME   : loopring
RDS_PASSWORD   : 
```

## How to start
```
gradle build
java -jar build/libs/loopring-ios-backend-0.1.0.jar
```
