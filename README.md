# TodoAPP - Backend

This project is backend side of  [todo-frontend]

### Dockerize

```sh
gradle build

docker build -t .

```

And also you need mysql database for project.

you can use mysql 8 docker and insert scripts and sql folder.

```sh

docker run --name=mysql8 -d \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=password \
    mysql:8.0.13 \
    --default_authentication_plugin=mysql_native_password

```

[todo-frontend]: <https://github.com/ertugrulyilmaz/todo-frontend>