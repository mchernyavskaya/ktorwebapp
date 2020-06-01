.PHONY: up
up:
	docker-compose -p ktorwebapp -f resources/docker-compose.yml up --detach

.PHONY: down
down:
	docker-compose -p ktorwebapp -f resources/docker-compose.yml down
