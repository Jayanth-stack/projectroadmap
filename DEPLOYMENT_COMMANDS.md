# Deployment Quick Reference Commands

## 🚀 Quick Start Commands

### Local Development with Docker Compose
```bash
# Build and run with MongoDB
docker-compose up --build

# Run in background
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down

# Remove volumes (clean database)
docker-compose down -v
```

### Test Deployment
```bash
# Access application
curl http://localhost:8080/api/urls

# Create a test URL
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"originalURL": "https://www.example.com/long-url"}'
```

---

## 🚀 Platform-Specific Deployment Commands

### Heroku Deployment

```bash
# Login to Heroku
heroku login

# Create Heroku app
heroku create your-app-name

# Add MongoDB connection
heroku config:set SPRING_DATA_MONGODB_URI="your-mongodb-uri"

# Deploy
git push heroku main

# View logs
heroku logs --tail

# Open app in browser
heroku open

# Check status
heroku ps

# Scale dynos
heroku ps:scale web=1

# Destroy app
heroku apps:destroy your-app-name
```

---

### Railway Deployment

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login
railway login

# Initialize project
railway init

# Add MongoDB plugin
railway add

# Set environment variables
railway variables set SPRING_DATA_MONGODB_URI="your-mongodb-uri"

# Deploy
railway up

# View logs
railway logs

# Get public URL
railway open

# Check status
railway status
```

---

### Render Deployment

```bash
# Prerequisites
# - GitHub account with repository
# - Render account (https://render.com)

# Steps:
# 1. Push code to GitHub
git push origin main

# 2. Go to Render dashboard
# https://dashboard.render.com

# 3. Click "New Web Service"
# 4. Connect GitHub repository
# 5. Configure:
#    - Runtime: Docker
#    - Plan: Free
#    - Add environment variables

# View logs in Render dashboard
# Redeploy: Click "Manual Deploy" or push to main branch
```

---

### Google Cloud Run Deployment

```bash
# Login to GCP
gcloud auth login

# Set project
gcloud config set project YOUR_PROJECT_ID

# Build locally
./mvnw clean package -DskipTests

# Build Docker image
gcloud builds submit --tag gcr.io/YOUR_PROJECT_ID/url-shortener

# Deploy to Cloud Run
gcloud run deploy url-shortener \
  --image gcr.io/YOUR_PROJECT_ID/url-shortener \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars SPRING_DATA_MONGODB_URI="your-mongodb-uri"

# View logs
gcloud run logs read url-shortener

# Get service URL
gcloud run services describe url-shortener

# Update service
gcloud run deploy url-shortener \
  --image gcr.io/YOUR_PROJECT_ID/url-shortener \
  --region us-central1 \
  --update-env-vars SPRING_DATA_MONGODB_URI="your-mongodb-uri"

# Delete service
gcloud run services delete url-shortener
```

---

### AWS Elastic Beanstalk Deployment

```bash
# Install EB CLI
pip install awsebcli

# Configure AWS
aws configure

# Initialize EB
eb init -p "Java 17 running on 64bit Amazon Linux 2" \
        --instance-type t2.micro \
        url-shortener-app

# Create environment
eb create url-shortener-env

# Set environment variables
eb setenv SPRING_DATA_MONGODB_URI="your-mongodb-uri"

# Build and deploy
./mvnw clean package -DskipTests
eb deploy

# Monitor deployment
eb status

# View logs
eb logs

# SSH into instance
eb ssh

# Terminate environment
eb terminate url-shortener-env
```

---

### DigitalOcean App Platform Deployment

```bash
# Prerequisites
# - DigitalOcean account
# - GitHub repository

# Steps (using web dashboard):
# 1. Go to https://cloud.digitalocean.com/apps
# 2. Click "Create App"
# 3. Select GitHub repository
# 4. Configure components
# 5. Set environment variables
# 6. Click "Create Resources"

# Using doctl CLI:
doctl apps create --spec app.yaml
doctl apps update [APP_ID] --spec app.yaml
```

---

### Fly.io Deployment

```bash
# Install Fly CLI
brew install flyctl

# Login
fly auth login

# Create account if needed
fly auth signup

# Initialize project
fly launch

# Set secrets
fly secrets set SPRING_DATA_MONGODB_URI="your-mongodb-uri"

# Deploy
fly deploy

# Monitor deployment
fly status
fly logs

# SSH into instance
fly ssh console

# Scale app
fly scale count 2

# Destroy app
fly destroy
```

---

## 📊 Monitoring Commands

### View Application Logs

```bash
# Docker Compose
docker-compose logs -f app

# Heroku
heroku logs --tail

# Railway
railway logs

# Render (Dashboard only)

# Google Cloud Run
gcloud run logs read [SERVICE_NAME]

# AWS EB
eb logs

# DigitalOcean (Dashboard only)

# Fly.io
fly logs
```

---

### Health Checks

```bash
# Basic health check
curl http://your-app-url/api/urls

# Health endpoint (if Actuator configured)
curl http://your-app-url/actuator/health

# Check response time
curl -w "@curl-format.txt" -o /dev/null -s http://your-app-url/api/urls

# Load testing
ab -n 100 -c 10 http://your-app-url/api/urls/test
```

---

## 🔐 Environment Variables Configuration

### Common Variables to Set

```bash
# MongoDB URI (Atlas)
SPRING_DATA_MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/projectroadmapdb

# Server Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=production

# Java Options
JAVA_OPTS=-Xmx256m -Xms128m

# Application Name
SPRING_APPLICATION_NAME=projectroadmap
```

### Set Variables by Platform

**Heroku:**
```bash
heroku config:set VAR_NAME=value
```

**Railway:**
```bash
railway variables set VAR_NAME=value
```

**AWS EB:**
```bash
eb setenv VAR_NAME=value
```

**GCP Cloud Run:**
```bash
gcloud run deploy [SERVICE] --set-env-vars VAR_NAME=value
```

**Fly.io:**
```bash
fly secrets set VAR_NAME=value
```

---

## 🔄 CI/CD Automation

### GitHub Actions Workflow

```yaml
name: Deploy to Railway

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'openjdk'
      
      - name: Build with Maven
        run: ./mvnw clean package -DskipTests
      
      - name: Deploy to Railway
        uses: railway-app/actions@v1
        with:
          token: ${{ secrets.RAILWAY_TOKEN }}
```

---

## 🐛 Troubleshooting Commands

### Check Port Binding

```bash
# Linux/Mac
lsof -i :8080

# Windows
netstat -ano | findstr :8080
```

### Container Issues

```bash
# Check Docker containers
docker ps -a

# Check Docker images
docker images

# Remove unused resources
docker system prune

# View container logs
docker logs [CONTAINER_ID]

# Inspect container
docker inspect [CONTAINER_ID]
```

### MongoDB Connection Issues

```bash
# Test MongoDB connection
mongosh "your-connection-string"

# Check connection string format
# mongodb+srv://username:password@cluster.mongodb.net/database
```

### Java Memory Issues

```bash
# Increase heap size
export JAVA_OPTS="-Xmx512m -Xms256m"

# Check current JVM memory
java -XshowSettings:vm -version
```

---

## 📈 Scaling Commands

### Horizontal Scaling (Add Instances)

**Heroku:**
```bash
heroku ps:scale web=2
```

**AWS EB:**
```bash
eb scale 3
```

**Fly.io:**
```bash
fly scale count 3
```

### Vertical Scaling (Increase Instance Size)

**Heroku:**
```bash
# Change dyno type
heroku dyno:type web=Standard-1X
```

**AWS EB:**
```bash
eb config
# Edit instance type to larger one
```

---

## 🚨 Rollback Commands

### Heroku Rollback
```bash
# View releases
heroku releases

# Rollback to previous version
heroku rollback
```

### AWS EB Rollback
```bash
# View environment history
eb appversion

# Rollback to previous version
eb abort
```

### Git Rollback
```bash
# Revert to previous commit
git revert [COMMIT_HASH]

# Reset to previous state (use with caution)
git reset --hard [COMMIT_HASH]
```

---

## 📝 Deployment Checklist

Before deploying to production:

- [ ] Code committed to Git
- [ ] Tests passing (`./mvnw test`)
- [ ] Build successful (`./mvnw clean package`)
- [ ] Environment variables configured
- [ ] Database migration completed
- [ ] HTTPS/SSL configured
- [ ] Monitoring set up
- [ ] Logging configured
- [ ] Backups configured
- [ ] Rollback plan ready
- [ ] Documentation updated
- [ ] Security review completed

---

## 🆘 Getting Help

For platform-specific help:
- **Heroku:** https://devcenter.heroku.com
- **Railway:** https://docs.railway.app
- **Render:** https://docs.render.com
- **GCP:** https://cloud.google.com/docs
- **AWS:** https://docs.aws.amazon.com
- **DigitalOcean:** https://docs.digitalocean.com
- **Fly.io:** https://fly.io/docs

---

## 📚 Additional Resources

- [Spring Boot Deployment Guide](https://spring.io/guides/gs/deploying-spring-boot-app-to-azure/)
- [Docker Documentation](https://docs.docker.com/)
- [MongoDB Atlas Documentation](https://docs.atlas.mongodb.com/)
- [12 Factor App](https://12factor.net/)

---

**Last Updated:** November 22, 2025
