pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('ai-reporting-system') {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
    }
}
