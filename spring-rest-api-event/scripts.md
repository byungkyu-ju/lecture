docker run --name rest-event -p 5432:5432 -e POSTGRES_PASSWORD=pass -d postgres

docker exec -i -t rest-event bash 

su - postgres

psql -d postgres -U postgres


