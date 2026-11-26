
pipeline {
    agent any

    tools {
        maven 'Maven-3.9.10'
    }

    environment {
        DOCKER_PATH = "/Applications/Docker.app/Contents/Resources/bin"
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out the source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building Spring Boot application...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application...'
                sh 'mvn package'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'

                sh '''
                    export PATH="$DOCKER_PATH:$PATH"

                    echo "Checking Docker installation..."
                    docker --version

                    echo "Building Docker image..."
                    docker build -t hello-cicd:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Docker Deploy') {
            steps {
                echo 'Deploying application using Docker...'

                sh '''
                    export PATH="$DOCKER_PATH:$PATH"

                    echo "Checking Docker..."
                    docker --version

                    echo "Stopping previous container if running..."
                    docker rm -f hello-cicd-container || true

                    echo "Starting new container..."
                    docker run -d \
                        --name hello-cicd-container \
                        -p 9090:8080 \
                        hello-cicd:${BUILD_NUMBER}

                    echo "Checking application health..."

                    for i in $(seq 1 30)
                    do
                        if curl -fsS http://localhost:9090/health
                        then
                            echo "Application is running successfully!"
                            exit 0
                        fi

                        echo "Waiting for application... attempt $i"
                        sleep 2
                    done

                    echo "Application failed to start!"
                    docker logs hello-cicd-container
                    exit 1
                '''
            }
        }

        stage('Success') {
            steps {
                echo 'CI/CD Pipeline completed successfully!'
                echo 'Docker container is deployed on port 9090.'
            }
        }
    }
}
