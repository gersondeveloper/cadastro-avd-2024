Sql Server Configuration

docker run --platform linux/amd64 -e 'ACCEPT_EULA=Y' -e "MSSQL_SA_PASSWORD=Teste_123#" --name "sql1" -p 1433:1433 -d mcr.microsoft.com/mssql/server:2022-latest
conectar a base criada com Azure Data Studio
criar database
criar usuario
dar grant ao usuario


open-api: localhost:8080/api-docs