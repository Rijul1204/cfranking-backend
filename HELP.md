# Codeforces Ranking

#### Build Project
`./mvnw clean install`

#### Start the app
`./mvnw spring-boot:run`
#### Redis Setup
Download and Build
```
wget http://download.redis.io/releases/redis-5.0.6.tar.gz
tar xzf redis-5.0.6.tar.gz
cd redis-5.0.6
make
```

#### Start Server
```
cd src
./redis-server
```

Redis will start at `127.0.0.1:6379`

#### CLI client
Start redis CLI client
```
cd src
./redis-cli
```

It will connect to the server running in the default port.
