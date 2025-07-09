mvnd clean install package
docker build -f .\src\main\docker\Dockerfile.jvm -t victobonetti/finchat:1.0.x .
docker push victobonetti/finchat:1.0.x