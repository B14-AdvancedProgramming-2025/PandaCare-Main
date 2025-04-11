# PandaCare

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
