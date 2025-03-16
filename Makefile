APP_DIR = crypto-wallet-api
APP_NAME = crypto-wallet-api
JAR_NAME = $(APP_DIR)/target/$(APP_NAME)-0.0.1-SNAPSHOT.jar
PROFILE = dev

# ==== MAVEN ====

build:
	cd $(APP_DIR) && ./mvnw clean package -DskipTests

run-local: build
	java -jar $(JAR_NAME)

# ==== DOCKER ====

docker-up-dev:
	docker compose -f $(APP_DIR)/docker-compose-dev.yml up --build

docker-down-dev:
	docker compose -f $(APP_DIR)/docker-compose-dev.yml down

docker-restart-dev: docker-down-dev docker-up-dev

# ==== UTILS ====

clean:
	cd $(APP_DIR) && ./mvnw clean

logs:
	docker compose -f $(APP_DIR)/docker-compose-dev.yml logs -f
