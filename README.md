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
{
    "deleted": false,
    "updated_at": "2018-10-31 22:36:18.803871",
    "lrc_fee_ratio": 0.01231,
    "account_token": "12345611",
    "created_at": "2018-10-31 22:36:06.523953",
    "language": "zh-hans",
    "currency": "usd",
    "id": 4,
    "config": "{}"
}
```

### POST user config

```
curl -X POST \
  https://www.loopring.mobi/api/v1/users \
  -H 'Content-Type: application/json' \
  -d '{
	"account_token": "12345611",
	"language": "zh-Hans",
	"currency": "USD",
	"lrc_fee_ratio": 0.002
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


Get app versions
[http://loopring-env.aqtsrpazpa.ap-northeast-1.elasticbeanstalk.com/api/v1/app_versions](http://loopring-env.aqtsrpazpa.ap-northeast-1.elasticbeanstalk.com/api/v1/app_versions)

```
{
    "count": 3,
    "app_versions": [
        {
            "updated_at": "2018-07-13 08:24:22.790371",
            "must_update": false,
            "created_at": "2018-07-13 08:24:22.790371",
            "description": "Wallet management is ready.",
            "id": 1,
            "version": "0.0.1"
        },
        {
            "updated_at": "2018-07-13 08:24:36.402759",
            "must_update": false,
            "created_at": "2018-07-13 08:24:36.402759",
            "description": "Market is ready.",
            "id": 2,
            "version": "0.0.2"
        },
        {
            "updated_at": "2018-07-13 08:24:52.587705",
            "must_update": false,
            "created_at": "2018-07-13 08:24:52.587705",
            "description": "You can trade tokens now.",
            "id": 3,
            "version": "0.0.3"
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
