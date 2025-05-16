# PandaCare

## Module 9 Tutorial B

### 1. Deliverable G1

#### A. Context Diagram

![Context Diagram](./images/g1-1.png)

#### B. Container Diagram

![Container Diagram](./images/g1-2.png)

#### C. Deployment Diagram

![Deployment Diagram](./images/g1-3.png)

### 2. Deliverable G2

#### A. Risk Analysis

After analyzing our current modular monolith architecture, we've identified several potential risks that could emerge as PandaCare grows in popularity and usage:

1. **Scalability Limitations**

   - Single Spring Boot application may struggle with high user load
   - Monolithic database could become a performance bottleneck
   - Real-time chat features require different scaling patterns than other services

2. **Reliability Concerns**

   - Single point of failure in the monolithic architecture
   - Maintenance downtime affects all system features
   - No effective isolation between critical and non-critical components

3. **Development & Deployment Challenges**

   - Growing codebase complexity despite modularization
   - Deployment of the entire application for single feature updates
   - Testing becomes increasingly complex with interdependent modules

4. **Resource Efficiency Issues**
   - Inefficient resource allocation (all modules scale together)
   - Uneven resource utilization (some modules require more processing power)
   - Limited ability to optimize specific modules independently

#### B. Updated Context Diagram

![Updated Context Diagram](./images/g2-2.png)

#### C. Updated Container Diagram

![Updated Container Diagram](./images/g2-3.png)

## Deployment Information

This project uses Docker and GitHub Actions for CI/CD deployment to AWS.

### Local Development

To run the application locally:

```bash
./gradlew bootRun
```

### Docker

To build and run with Docker:

```bash
# Build the Docker image
docker build -t pandacare:latest .

# Run the container
docker run -p 8080:8080 pandacare:latest
```

Or using Docker Compose:

```bash
docker-compose up
```

### CI/CD Pipeline

The application is automatically deployed to AWS when changes are pushed to the `staging` branch. The deployment process:

1. Builds the application with Gradle
2. Runs tests
3. Creates a Docker image
4. Deploys the Docker image to AWS EC2

Required GitHub Secrets:

- `AWS_HOST`: The AWS EC2 host address
- `AWS_SSH_PRIVATE_KEY`: SSH private key for connecting to EC2
- `AWS_USERNAME`: Username for SSH connection
