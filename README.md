# README #

Backend for loopr iOS app.

## API
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
RDS_HOSTNAME   : aa12qdw9msqwuyw.cm2dxfcf18hd.ap-northeast-1.rds.amazonaws.com
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
