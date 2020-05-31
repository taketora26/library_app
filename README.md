# 図書管理システム(PlayFrameworkで作るCRUDのアプリケーションの例)

## 実行環境
* Scala 2.13.10
* PlayFramework 2.8.2
* Materialize 1.0
* flyway-sbt 6.4.2
* ScalikeJDBC 3.4.2  
* MySQL 8

## ローカル環境での実行

MySQLを立ち上げて下記を実行

```bash
$ sh create-local-mysql.sh
```

マイグレーションを実行

```bash
$ sbt flywayMigrate
```

起動

```bash
$ sbt run
```
