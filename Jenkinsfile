pipeline {
    agent any

    tools {
        jdk 'jdk17'   // Ensure JDK 17 is configured in Jenkins
    }

    environment {
        // Database credentials (Jenkins Credentials)
        DB_URL      = credentials('db-url')
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')

        // OpenAI
        OPENAI_KEY  = credentials('openai-api-key')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('ai-reporting-system') {
                    sh '''
                        chmod +x mvnw
                        ./mvnw clean package -DskipTests
                    '''
                }
            }
        }

        stage('Run Application') {
            steps {
                dir('ai-reporting-system') {
                    sh '''
                        echo "Stopping old application if running..."
                        pkill -f ai-reporting-system || true

                        echo "Starting Spring Boot application..."
                        nohup ./mvnw spring-boot:run \
                          > app.log 2>&1 &
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "✅ Application started successfully on EC2"
        }
        failure {
            echo "❌ Pipeline failed"
        }
    }
}
