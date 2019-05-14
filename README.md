# 図書管理システム(PlayFrameworkで作るCRUDのアプリケーションの例)

## 実行環境
* Scala 2.12.8
* PlayFramework 2.7.1
* Materialize 1.0
* flyway-sbt 5.2
* ScalikeJDBC 3.3.3 
* MySQL 5.7

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
