build:
	mvn clean install -P compose
	docker build -t potter/budget:0.1.0-SNAPSHOT -f budget-app/Dockerfile budget-app

stop:
	docker compose down --remove-orphans

run: build stop
	docker compose run --service-ports -d mysql
	docker compose run --service-ports -d redis
	sleep 10
	docker compose run --service-ports -d budget

test: 
	cd budget-tests && mvn test
