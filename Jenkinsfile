pipeline {
    agent any

    environment {
        DB_URL      = credentials('db-url')
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
                        pkill -f ai-reporting-system || true
                        nohup ./mvnw spring-boot:run > app.log 2>&1 &
                    '''
                }
            }
        }
    }
}
