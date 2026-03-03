# Postman Guide: Testing the Upload Endpoint

## Prerequisites

1. **Start the application** from the `data-pipeline-service` folder:

   ```powershell
   cd data-pipeline-service
   mvn spring-boot:run
   ```

2. Wait until you see `Tomcat started on port(s): 8081` (or similar). The service runs on **port 8081** by default.

3. Ensure MongoDB is running and reachable (as configured in `application.properties`).

---

## Upload Endpoint

| Setting | Value |
|--------|-------|
| **Method** | `POST` |
| **URL** | `http://localhost:8081/api/pipeline/upload` |

---

## Request Setup

1. Open Postman.
2. Create a new request.
3. Set the **Method** to **POST**.
4. Enter the **URL**: `http://localhost:8081/api/pipeline/upload`.

### Body Configuration

1. Go to the **Body** tab.
2. Select **form-data** (not raw, x-www-form-urlencoded, etc.).
3. Add the following keys:

| Key | Type | Value |
|-----|------|-------|
| `file` | File | Click **Select Files** and choose your CSV (e.g. `Auto_Insurance.csv`, `Auto_Insurance_sample.csv`) |
| `collectionName` | Text | `auto_insurance` (or `life_insurance`, `health_insurance` depending on your file) |

### Example Configuration

- **file**: `C:\...\Datasets\Auto_Insurance_sample.csv`
- **collectionName**: `auto_insurance`

---

## Send the Request

Click **Send**.

---

## Expected Response (200 OK)

```json
{
  "message": "File uploaded and pipeline executed",
  "collectionName": "auto_insurance",
  "totalProcessed": 6,
  "matched": 4,
  "unmatched": 2
}
```

- **totalProcessed**: Number of policy records processed.
- **matched**: Records successfully linked to `customer_details` (e.g. via PAN/Mobile).
- **unmatched**: Records that could not be stitched to a customer.

---

## Troubleshooting

| Issue | Possible Cause |
|-------|----------------|
| Connection refused | App not running. Run `mvn spring-boot:run`. |
| 404 Not Found | Wrong URL or port. Use `http://localhost:8081/api/pipeline/upload`. (Service runs on port **8081**, not 8080.) |
| 400 Bad Request | Missing `file` or `collectionName`, or invalid `collectionName` (no mapping). |
| Empty file error | CSV has no data rows. |

---

## Valid Collection Names

Use one of the mapped collections:

- `auto_insurance`
- `life_insurance`
- `health_insurance`

(Exact names depend on your `MappingConfig` setup.)
