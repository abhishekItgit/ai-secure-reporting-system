pipeline {
    agent any

    environment {
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')
        OPENAI_KEY  = credentials('openai-api-key')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                dir('ai-reporting-system') {
                    sh '''
                        chmod +x mvnw
                        ./mvnw clean package -DskipTests
                    '''
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh '''
                    export DB_USERNAME=${DB_USERNAME}
                    export DB_PASSWORD=${DB_PASSWORD}
                    export OPENAI_KEY=${OPENAI_KEY}

                    docker compose down || true
                    docker compose up -d --build
                '''
            }
        }
    }
}
