jobs:
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_DB: appdb
          POSTGRES_USER: app
          POSTGRES_PASSWORD: secret
        ports: [ "5432:5432" ]
        options: >-
          --health-cmd="pg_isready -U app -d appdb"
          --health-interval=5s --health-timeout=5s --health-retries=20
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
          cache: maven
      - name: Run tests
        env:
          SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/appdb
          SPRING_DATASOURCE_USERNAME: app
          SPRING_DATASOURCE_PASSWORD: secret
        run: mvn -B -ntp verify