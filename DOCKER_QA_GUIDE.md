# Data Pipeline Service — Docker Setup for QA

Standalone Docker setup so the QA team can run and test the APIs without installing Java or MongoDB locally.

---

## Option 1: Docker Compose (Recommended for QA)

Includes the service + a local MongoDB. Fully self-contained.

### Start

```powershell
cd data-pipeline-service
docker-compose up -d
```

### Wait for startup

- MongoDB: ~10 seconds
- Service: ~30–60 seconds after MongoDB is ready

### Base URL for API testing

```
http://localhost:8081
```

### Swagger UI (interactive API docs)

```
http://localhost:8081/swagger-ui/index.html
```

### Example requests (Postman / curl)

| Method | URL |
|--------|-----|
| POST   | `http://localhost:8081/api/pipeline/run` |
| POST   | `http://localhost:8081/api/pipeline/upload` (form-data: file, collectionName) |
| GET    | `http://localhost:8081/api/portfolio/{customerId}` |
| GET    | `http://localhost:8081/api/advisory/{customerId}` |

### Stop

```powershell
docker-compose down
```

To remove MongoDB data as well:

```powershell
docker-compose down -v
```

---

## Option 2: Use Existing MongoDB (Atlas)

If QA uses your MongoDB Atlas instance, run only the service:

```powershell
cd data-pipeline-service
docker build -t data-pipeline-service .
docker run -p 8081:8081 -e MONGODB_URI="mongodb+srv://user:pass@cluster.mongodb.net/" data-pipeline-service
```

Replace the URI with your Atlas connection string.

---

## Option 3: Image Only (Pre-built)

If an image is built and pushed to a registry:

```powershell
docker pull <registry>/data-pipeline-service:latest
docker run -p 8081:8081 -e MONGODB_URI="mongodb://host.docker.internal:27017" data-pipeline-service:latest
```

---

## Post-start QA Checks

1. **Health:** `GET http://localhost:8081/actuator/health` (if actuator is enabled)
2. **Upload:** Upload `customer_data_matching.csv` with `collectionName=customer_details`
3. **Upload:** Upload policy CSVs (`auto_insurance`, `life_insurance`, `health_insurance`)
4. **Portfolio:** `GET http://localhost:8081/api/portfolio/901200001`
5. **Advisory:** `GET http://localhost:8081/api/advisory/901200001`

---

## Troubleshooting

| Issue | Action |
|-------|--------|
| Port 8081 in use | Stop other apps on 8081 or change `ports: "8082:8081"` in `docker-compose` |
| Build fails | Ensure Docker is running and you are in `data-pipeline-service` |
| Service won’t start | Check logs: `docker-compose logs data-pipeline-service` |
