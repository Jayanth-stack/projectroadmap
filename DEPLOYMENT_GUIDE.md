# Deployment Guide - URL Shortener Project

A comprehensive guide to deploy your Spring Boot URL Shortener application to various cloud platforms.

## 📋 Table of Contents

1. [Deployment Platforms Comparison](#deployment-platforms-comparison)
2. [Pre-Deployment Checklist](#pre-deployment-checklist)
3. [Platform-Specific Guides](#platform-specific-guides)
4. [Database Setup for Each Platform](#database-setup-for-each-platform)
5. [Cost Estimation](#cost-estimation)
6. [Monitoring & Maintenance](#monitoring--maintenance)

---

## Deployment Platforms Comparison

### Quick Reference Table

| Platform | Cost | Ease | Scalability | Free Tier | Best For |
|----------|------|------|-------------|-----------|----------|
| **Heroku** | Low-Mid | ⭐⭐⭐⭐⭐ | Medium | 7 days trial | Quick prototypes, Small apps |
| **Railway** | Low | ⭐⭐⭐⭐⭐ | Medium | $5/month | Hobby projects |
| **Render** | Low | ⭐⭐⭐⭐⭐ | Medium | Yes | Small to medium apps |
| **AWS** | Variable | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | 12 months | Enterprise, High scale |
| **Azure** | Variable | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | 12 months | Enterprise, Windows |
| **Google Cloud** | Variable | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | 12 months | Machine learning, Analytics |
| **DigitalOcean** | Low-Mid | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | No | Developers, Full control |
| **Fly.io** | Low | ⭐⭐⭐⭐⭐ | Medium | Yes | Global edge deployment |
| **AWS Elastic Beanstalk** | Low-Mid | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | 12 months free | Java apps, AWS ecosystem |

---

## Pre-Deployment Checklist

Before deploying to any platform, ensure:

- [ ] Build succeeds: `./mvnw clean package`
- [ ] Tests pass: `./mvnw test`
- [ ] JAR file created: `target/projectroadmap-0.0.1-SNAPSHOT.jar`
- [ ] Dockerfile created (for containerized deployment)
- [ ] `.gitignore` configured properly
- [ ] Git repository initialized and committed
- [ ] Environment variables documented
- [ ] MongoDB cluster ready (cloud instance)
- [ ] Application properties updated for production

---

## Platform-Specific Guides

### 1. Heroku (Easiest Option) ⭐⭐⭐⭐⭐

**Pros:**
- Simplest deployment
- Built-in git integration
- Free tier available (limited)
- Good documentation

**Cons:**
- Paid after free trial (started Nov 2022)
- Can be expensive at scale
- Limited customization

**Step-by-Step Deployment:**

#### 1.1 Prerequisites
```bash
# Install Heroku CLI
# macOS
brew tap heroku/brew && brew install heroku

# Windows
# Download from https://devcenter.heroku.com/articles/heroku-cli

# Login to Heroku
heroku login
```

#### 1.2 Create Heroku App
```bash
# Create new Heroku app
heroku create your-url-shortener-app

# Or create with specific region
heroku create your-url-shortener-app --region eu
```

#### 1.3 Set Environment Variables
```bash
heroku config:set SPRING_DATA_MONGODB_URI=your-mongodb-atlas-uri
heroku config:set JAVA_TOOL_OPTIONS="-Xmx300m -Xss512k -XX:CICompilerCount=2"
```

#### 1.4 Deploy
```bash
# Make sure you're in the project directory
cd /Users/jayanthalapati/Projects/projectroadmap

# Add Heroku remote (if not added)
heroku git:remote -a your-url-shortener-app

# Deploy
git push heroku main

# View logs
heroku logs --tail
```

#### 1.5 Verify Deployment
```bash
# Test the application
curl https://your-url-shortener-app.herokuapp.com/api/urls
```

**Cost:** Free tier ended, now starts at $5-50/month depending on dyno type

---

### 2. Railway (Recommended for Simplicity) ⭐⭐⭐⭐⭐

**Pros:**
- Very simple deployment
- GitHub integration
- Database templates available
- $5 free credits monthly
- Pay-as-you-go pricing

**Cons:**
- Relatively new platform
- Smaller community than Heroku

**Step-by-Step Deployment:**

#### 2.1 Prerequisites
```bash
# Create account at https://railway.app
# Install Railway CLI
npm install -g @railway/cli

# Login
railway login
```

#### 2.2 Create Project on Railway
```bash
# In your project directory
cd /Users/jayanthalapati/Projects/projectroadmap

# Initialize Railway project
railway init

# Select "Create a new project"
# Enter project name: "url-shortener"
```

#### 2.3 Add MongoDB
```bash
# Add MongoDB plugin
railway add
# Select "MongoDB" from the list
```

#### 2.4 Configure Environment Variables
```bash
railway variables set SPRING_DATA_MONGODB_URI="your-mongodb-uri"
```

#### 2.5 Deploy
```bash
# Deploy to Railway
railway up

# View logs
railway logs
```

#### 2.6 Get Public URL
```bash
railway open
```

**Cost:** $5/month free credits (usually sufficient for hobby projects)

---

### 3. Render (Best Free Tier) ⭐⭐⭐⭐⭐

**Pros:**
- Truly free tier available
- GitHub integration
- Easy deployment
- MongoDB support
- No credit card required

**Cons:**
- Free tier has limitations
- Spins down after 15 minutes of inactivity

**Step-by-Step Deployment:**

#### 3.1 Prerequisites
- GitHub account with your project
- Render account (https://render.com)

#### 3.2 Create Dockerfile
```bash
# Create Dockerfile in project root
cat > Dockerfile << 'EOF'
FROM openjdk:17-slim

WORKDIR /app

COPY target/projectroadmap-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-jar", "app.jar"]
EOF
```

#### 3.3 Create render.yaml
```bash
cat > render.yaml << 'EOF'
services:
  - type: web
    name: url-shortener
    runtime: docker
    plan: free
    healthCheckPath: /api/urls
    envVars:
      - key: SPRING_DATA_MONGODB_URI
        scope: run
      - key: PORT
        value: 8080
EOF
```

#### 3.4 Build JAR
```bash
./mvnw clean package -DskipTests
```

#### 3.5 Push to GitHub
```bash
git add .
git commit -m "Add Dockerfile and render configuration"
git push origin main
```

#### 3.6 Deploy on Render
1. Go to https://dashboard.render.com
2. Click "New Web Service"
3. Connect your GitHub repository
4. Select the branch (main)
5. Fill in deployment details:
   - **Name:** url-shortener
   - **Runtime:** Docker
   - **Plan:** Free
6. Add environment variables:
   - **SPRING_DATA_MONGODB_URI:** Your MongoDB Atlas URI
7. Click "Deploy"

**Cost:** Free tier available (with limitations)

---

### 4. AWS Elastic Beanstalk (Best for Enterprise)

**Pros:**
- Excellent scalability
- 12 months free tier
- AWS ecosystem integration
- Professional support available
- Auto-scaling capabilities

**Cons:**
- Steeper learning curve
- More complex setup
- Pricing can be complex

**Step-by-Step Deployment:**

#### 4.1 Prerequisites
```bash
# Install AWS CLI
# macOS
brew install awscli

# Configure AWS credentials
aws configure
# Enter: Access Key ID, Secret Access Key, Region, Output format
```

#### 4.2 Install Elastic Beanstalk CLI
```bash
# Install EB CLI
pip install awsebcli --upgrade --user

# Verify installation
eb --version
```

#### 4.3 Initialize Elastic Beanstalk
```bash
cd /Users/jayanthalapati/Projects/projectroadmap

# Initialize EB application
eb init -p "Java 17 running on 64bit Amazon Linux 2" \
       --instance-type t2.micro \
       url-shortener-app
```

#### 4.4 Create Environment
```bash
# Create environment
eb create url-shortener-env

# Set environment variables
eb setenv SPRING_DATA_MONGODB_URI="your-mongodb-uri"
```

#### 4.5 Deploy
```bash
# Build and deploy
./mvnw clean package -DskipTests
eb deploy

# View logs
eb logs

# Monitor application
eb status
```

#### 4.6 Get Application URL
```bash
eb open
# This opens your application in the browser
```

**Cost:** 12 months free tier, then variable based on usage

---

### 5. Google Cloud Run (Serverless) ⭐⭐⭐⭐

**Pros:**
- Serverless (no server management)
- Pay per use
- Scales automatically
- 12 months free tier ($300 credit)

**Cons:**
- Requires Docker image
- Cold start times
- Learning curve

**Step-by-Step Deployment:**

#### 5.1 Prerequisites
```bash
# Install Google Cloud CLI
# macOS
brew install google-cloud-sdk

# Initialize gcloud
gcloud init

# Set your project
gcloud config set project YOUR_PROJECT_ID
```

#### 5.2 Create Dockerfile
```bash
cat > Dockerfile << 'EOF'
FROM openjdk:17-slim
WORKDIR /app
COPY target/projectroadmap-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
```

#### 5.3 Build JAR
```bash
./mvnw clean package -DskipTests
```

#### 5.4 Build and Push Docker Image
```bash
# Build image
gcloud builds submit --tag gcr.io/YOUR_PROJECT_ID/url-shortener

# Verify image
gcloud container images list
```

#### 5.5 Deploy to Cloud Run
```bash
gcloud run deploy url-shortener \
  --image gcr.io/YOUR_PROJECT_ID/url-shortener \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars SPRING_DATA_MONGODB_URI="your-mongodb-uri"
```

#### 5.6 Get Application URL
```bash
# Display service details
gcloud run services describe url-shortener --region us-central1

# Test endpoint
curl https://url-shortener-xxxxx.run.app/api/urls
```

**Cost:** 12 months free tier ($300 credit), then pay-per-use

---

### 6. DigitalOcean App Platform ⭐⭐⭐⭐

**Pros:**
- Simple and predictable pricing
- Git integration
- Database templates included
- Good documentation
- No free tier but affordable

**Cons:**
- No free tier
- Starts at $5/month

**Step-by-Step Deployment:**

#### 6.1 Prerequisites
- DigitalOcean account (https://digitalocean.com)
- GitHub repository with your code

#### 6.2 Create Dockerfile
```bash
cat > Dockerfile << 'EOF'
FROM openjdk:17-slim
WORKDIR /app
COPY target/projectroadmap-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
```

#### 6.3 Build and Push to GitHub
```bash
git add Dockerfile
git commit -m "Add Dockerfile"
git push origin main
```

#### 6.4 Deploy on DigitalOcean
1. Go to https://cloud.digitalocean.com/apps
2. Click "Create App"
3. Select GitHub and authorize
4. Select your repository and branch
5. Configure:
   - **Name:** url-shortener
   - **Source Type:** Docker
   - **Port:** 8080
6. Add database:
   - Click "Add Component"
   - Select "MongoDB Database"
7. Set environment variables
8. Click "Create Resources"

**Cost:** Starts at $5/month for basic tier

---

### 7. Fly.io (Global Edge Deployment) ⭐⭐⭐⭐

**Pros:**
- Global edge deployment (low latency)
- Free tier available
- Docker-native platform
- Good for distributed apps

**Cons:**
- Newer platform
- Smaller community

**Step-by-Step Deployment:**

#### 7.1 Prerequisites
```bash
# Install Fly CLI
# macOS
brew install flyctl

# Login
fly auth login
```

#### 7.2 Launch Application
```bash
cd /Users/jayanthalapati/Projects/projectroadmap

# Launch on Fly
fly launch

# Select organization and app name
# Choose regions (e.g., sin, sjc, ord)
# Would you like to set up a Postgresql database? → no
# Would you like to set up an Upstash Redis database? → no
```

#### 7.3 Set Environment Variables
```bash
fly secrets set SPRING_DATA_MONGODB_URI="your-mongodb-uri"
```

#### 7.4 Deploy
```bash
fly deploy
```

#### 7.5 Get Application URL
```bash
fly status
# Your app is available at: https://your-app-name.fly.dev
```

**Cost:** Free tier available with limitations

---

## Database Setup for Each Platform

### MongoDB Atlas (Recommended)

**Why:** Free tier available, managed service, works with any platform

#### Setup Steps:
```bash
# 1. Go to https://www.mongodb.com/cloud/atlas
# 2. Create account
# 3. Create free cluster
# 4. Configure IP whitelist (allow all: 0.0.0.0/0 for development)
# 5. Create database user
# 6. Get connection string
# 7. Use connection string in your application

# Connection string format:
mongodb+srv://username:password@cluster.mongodb.net/projectroadmapdb
```

### AWS DocumentDB
```bash
# For AWS-based deployments
# 1. Go to AWS DocumentDB console
# 2. Create new cluster
# 3. Configure master username and password
# 4. Get connection endpoint
# 5. Update security groups to allow connection
```

### Azure Cosmos DB
```bash
# For Azure-based deployments
# 1. Create Cosmos DB account
# 2. Create MongoDB database
# 3. Get connection string
# 4. Configure firewall rules
```

---

## Cost Estimation

### Small Project (1000-5000 monthly requests)

| Platform | Service | Cost | Notes |
|----------|---------|------|-------|
| **Render** | Web + MongoDB | Free-$10/mo | Free tier sufficient |
| **Railway** | Web + MongoDB | $5-15/mo | $5 monthly credits |
| **Heroku** | Web + MongoDB | $25-50/mo | Basic dyno required |
| **AWS EB** | Web + RDS | Free-$20/mo | 12 months free |
| **GCP** | Cloud Run + Firestore | Free-$10/mo | $300 free credits |

### Medium Project (50,000+ monthly requests)

| Platform | Service | Cost | Notes |
|----------|---------|------|-------|
| **AWS EB** | Web + RDS | $50-100/mo | Auto-scaling |
| **Azure** | App Service + Cosmos | $40-80/mo | Enterprise support |
| **DigitalOcean** | App + Database | $30-50/mo | Simple pricing |
| **Google Cloud** | Cloud Run + Firestore | $20-60/mo | Pay-per-use |

---

## Production Checklist

Before deploying to production:

- [ ] Enable HTTPS/SSL
- [ ] Add authentication (JWT tokens)
- [ ] Set up logging and monitoring
- [ ] Configure error tracking (Sentry)
- [ ] Enable rate limiting
- [ ] Set up CI/CD pipeline
- [ ] Configure automated backups
- [ ] Add health check endpoint
- [ ] Set up alerts and notifications
- [ ] Document deployment process
- [ ] Create rollback plan
- [ ] Set up database encryption
- [ ] Configure VPN/security groups
- [ ] Document API rate limits
- [ ] Set up performance monitoring

---

## Monitoring & Maintenance

### Application Monitoring

#### 1. Add Actuator for Health Checks
```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### 2. Update application.properties
```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

#### 3. Monitor Endpoints
```bash
# Health check
curl https://your-app.com/actuator/health

# Application info
curl https://your-app.com/actuator/info

# Metrics
curl https://your-app.com/actuator/metrics
```

### Logging & Monitoring Services

**Recommended Tools:**
- **Datadog** - Comprehensive monitoring ($15+/mo)
- **New Relic** - APM and monitoring ($19+/mo)
- **LogRocket** - Frontend monitoring ($99+/mo)
- **Sentry** - Error tracking (Free tier available)
- **ELK Stack** - Elasticsearch, Logstash, Kibana (Self-hosted)

### Database Backups

**MongoDB Atlas:**
```bash
# Automated backups included with paid tier
# Manual backup commands:
mongodump --uri "mongodb+srv://user:pass@cluster.mongodb.net/db"
mongorestore --uri "mongodb+srv://user:pass@cluster.mongodb.net/db" dump/
```

---

## Quick Deployment Comparison

```
┌─────────────────────────────────────────────────────────┐
│ IF YOU WANT...              → CHOOSE...                 │
├─────────────────────────────────────────────────────────┤
│ Simplest setup              → Render or Railway         │
│ Most features               → Heroku or AWS             │
│ Best free tier              → Render or GCP             │
│ Best performance            → Fly.io                    │
│ Best enterprise support     → AWS or Azure              │
│ Most cost-effective         → Railway or DigitalOcean   │
│ Global deployment           → Fly.io                    │
│ Serverless (auto-scale)     → Google Cloud Run          │
└─────────────────────────────────────────────────────────┘
```

---

## Troubleshooting Deployment Issues

### Application won't start
```bash
# Check logs
heroku logs --tail    # Heroku
railway logs          # Railway
eb logs               # AWS EB

# Check environment variables
heroku config        # Heroku
railway variables    # Railway
```

### MongoDB connection fails
```bash
# Verify connection string
# Check IP whitelist in MongoDB Atlas
# Ensure DATABASE_URL environment variable is set
# Test connection:
mongo "your-connection-string"
```

### Port binding issues
```bash
# Use PORT environment variable
export PORT=8080
java -jar app.jar
```

### Memory issues
```bash
# Increase heap size
export JAVA_OPTS="-Xmx256m -Xms128m"
java $JAVA_OPTS -jar app.jar
```

---

## Next Steps

1. **Choose Platform:** Based on your needs and budget
2. **Set Up MongoDB:** Use MongoDB Atlas
3. **Create Application:** Build and test locally
4. **Deploy:** Follow platform-specific guide
5. **Monitor:** Set up logging and alerts
6. **Scale:** Add resources as needed

---

## Recommended Deployment Path

**For First Time:**
```
Local Development → Railway (Simple & Free) → Render (Scaling) → AWS (Enterprise)
```

**Specific Recommendation for This Project:**
```
Start with Railway ($5/month)
  ↓
If traffic grows → Render or Fly.io
  ↓
If enterprise needed → AWS or Azure
```

---

## Additional Resources

- [Spring Boot Deployment Documentation](https://spring.io/guides/gs/deploying-spring-boot-app-to-azure/)
- [Docker & Containerization Guide](https://docs.docker.com/)
- [MongoDB Atlas Documentation](https://docs.atlas.mongodb.com/)
- [12 Factor App Methodology](https://12factor.net/)

---

**Need help with a specific platform? Check the detailed section above!**
