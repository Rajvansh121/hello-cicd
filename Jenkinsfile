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
                    JENKINS_NODE_COOKIE=dontKillMe nohup java -jar target/hello-cicd-1.0.0.jar --server.port=8081 > app.log 2>&1 &
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
