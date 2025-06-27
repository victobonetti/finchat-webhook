mvnd clean install package
docker build -f .\src\main\docker\Dockerfile.jvm -t victobonetti/finchat .
docker push victobonetti/finchat:latest