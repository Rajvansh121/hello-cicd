```groovy
pipeline {
    agent any

    tools {
        maven 'Maven-3.9.10'
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

        stage('Deploy') {
            steps {
                echo 'Deploying Spring Boot application...'

                sh '''
                    PID_FILE="/tmp/hello-cicd.pid"

                    # Stop the previous application
                    if [ -f "$PID_FILE" ]; then
                        OLD_PID=$(cat "$PID_FILE")

                        if kill -0 "$OLD_PID" 2>/dev/null; then
                            echo "Stopping old application (PID: $OLD_PID)..."
                            kill "$OLD_PID"

                            # Wait for graceful shutdown
                            for i in $(seq 1 10); do
                                if ! kill -0 "$OLD_PID" 2>/dev/null; then
                                    break
                                fi
                                sleep 1
                            done

                            # Force stop if necessary
                            if kill -0 "$OLD_PID" 2>/dev/null; then
                                echo "Force stopping old application..."
                                kill -9 "$OLD_PID"
                            fi
                        fi

                        rm -f "$PID_FILE"
                    fi

                    # Start the new application
                    echo "Starting new application..."

                    JENKINS_NODE_COOKIE=dontKillMe nohup java -jar target/hello-cicd-1.0.0.jar --server.port=8081 > app.log 2>&1 &

                    NEW_PID=$!
                    echo "$NEW_PID" > "$PID_FILE"

                    echo "New application PID: $NEW_PID"

                    # Health check
                    echo "Checking application health..."

                    for i in $(seq 1 30); do
                        if curl -s http://localhost:8081 > /dev/null; then
                            echo "Application is running successfully!"
                            curl http://localhost:8081
                            exit 0
                        fi

                        sleep 2
                    done

                    echo "Application failed to start!"
                    cat app.log
                    exit 1
                '''
            }
        }

        stage('Success') {
            steps {
                echo 'CI/CD Pipeline completed successfully!'
            }
        }
    }
}
```