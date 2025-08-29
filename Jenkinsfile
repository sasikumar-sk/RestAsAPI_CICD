pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'MAVEN_HOME'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo 'Building the project...'
                bat "mvn clean compile -DskipTests"
            }
        }
        stage('Run API Tests') {
            steps {
                echo 'Running API tests...'
                bat "mvn test"
            }
        }
        stage('Publish Test Reports') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }

    post {
        always {
            echo "Archiving logs and reports..."
            archiveArtifacts artifacts: '**/target/surefire-reports/*.xml', allowEmptyArchive: true
        }
        failure {
            echo "❌ Build failed – check console output"
        }
    }
}
