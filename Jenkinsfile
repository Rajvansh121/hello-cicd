
pipeline {
    agent any

    tools {
        maven 'Maven-3.9.10'
    }

    environment {
        DOCKER_PATH = "/Applications/Docker.app/Contents/Resources/bin"
    }

    stages {

        stage('Build') {
            steps {
                echo 'Building Spring Boot application...'
                sh 'mvn -B -ntp clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn -B -ntp test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application...'
                sh 'mvn -B -ntp package'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh '''
                    export PATH="$DOCKER_PATH:$PATH"
                    docker --version
                    docker build -t hello-cicd:${BUILD_NUMBER} .
                '''
            }
        }

        stage('Docker Deploy') {
            steps {
                echo 'Deploying application using Docker...'
                sh '''
                    export PATH="$DOCKER_PATH:$PATH"

                    docker rm -f hello-cicd-container || true

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
                echo 'Application URL: http://localhost:9090/'
            }
        }
    }
}
