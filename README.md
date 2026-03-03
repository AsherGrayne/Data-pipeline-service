# Data Pipeline Service

Enterprise-grade Spring Boot implementation (zero-code-change metadata engine):

| Layer | Implementation |
|-------|----------------|
| **POJOs** | `@Document` entities: AutoInsurance, HealthInsurance, LifeInsurance, CustomerDetails |
| **Metadata Engine** | `mapping_config.json` → `MappingDTO` → `StandardizedPolicyDTO` |
| **Data Massaging** | `DataMassagingUtil`: YYYYMMDD→LocalDate, currency, mobile normalization |
| **Stitching** | Priority 1: PAN → 2: Mobile+DOB → 3: Email+DOB |
| **Security** | AES-256 PII encryption (PAN, Mobile, Email) via Spring Security Crypto |
| **Advisory** | Product Gaps, Protection Gaps (Sum Assured), Temporal Gaps + "Why it matters" |

## Run

```bash
cd data-pipeline-service
mvn spring-boot:run
```

Runs on **port 8081**.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/pipeline/run` | Standardize + stitch; returns matched/unmatched counts |
| POST | `/api/pipeline/upload` | Upload CSV file; ingests, standardizes, stitches (see POSTMAN_UPLOAD_GUIDE.md) |
| GET | `/api/portfolio/{customerId}` | Unified portfolio for customer |
| GET | `/api/advisory/{customerId}` | Coverage advisory with gap analysis |

## Prerequisites

1. Load sample data to MongoDB Atlas (run `scripts/load_sample_to_mongo.py`)
2. MongoDB URI in `application.yaml` or env var `MONGODB_URI`

## Architecture

```
Controller → Service → Repository
    ↓           ↓
MappingConfig   MongoTemplate
SecurityUtils   AuditLogger
```
