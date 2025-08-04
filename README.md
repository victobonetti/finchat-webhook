mvnd clean install package
docker build -f .\src\main\docker\Dockerfile.jvm -t victobonetti/finchat:1.1.0 .
docker push victobonetti/finchat:1.1.0