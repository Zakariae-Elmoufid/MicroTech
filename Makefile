all: clean build run

clean:
	./mvnw clean

build:
	docker-compose build

run:
	docker-compose up

stop:
	docker-compose down

test:
	./mvnw test