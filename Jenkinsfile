pipeline {
    agent any

    environment {
        DB_URL      = credentials('db-url')
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')
        OPENAI_KEY  = credentials('openai-api-key')
        APP_NAME    = "ai-reporting"
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

        stage('Build Docker Image') {
            steps {
                dir('ai-reporting-system') {
                    sh '''
                        docker build -t $APP_NAME:latest .
                    '''
                }
            }
        }

        stage('Deploy Container') {
            steps {
                sh '''
                    docker stop $APP_NAME || true
                    docker rm $APP_NAME || true

                    docker run -d \
                      --name $APP_NAME \
                      -p 8080:8080 \
                      -e DB_URL=$DB_URL \
                      -e DB_USERNAME=$DB_USERNAME \
                      -e DB_PASSWORD=$DB_PASSWORD \
                      -e OPENAI_API_KEY=$OPENAI_KEY \
                      $APP_NAME:latest
                '''
            }
        }
    }
}
