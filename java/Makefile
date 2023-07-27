build:
	mvn clean install -P compose
	docker build -t potter/budget:0.1.0-SNAPSHOT -f budget-app/Dockerfile budget-app

stop:
	docker compose down --remove-orphans

start: build stop
	docker compose up -d

test: 
	cd budget-tests && mvn test

make clean: stop
	mvn clean

make restart: start