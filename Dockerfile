# Use the SQL Server 2022 image as the base
FROM mcr.microsoft.com/mssql/server:2022-latest

# Set environment variables
ENV ACCEPT_EULA=Y
ENV MSSQL_SA_PASSWORD=Teste_123#

# Expose port 1433
EXPOSE 1433
