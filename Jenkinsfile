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
                echo 'Building the project...'
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Run API Tests') {
            steps {
                echo 'Running API tests...'
                sh 'mvn test'
            }
        }

        stage('Publish Test Reports') {
            steps {
                echo 'Publishing JUnit reports...'
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }

    post {
        always {
            echo 'Archiving logs and reports...'
            archiveArtifacts artifacts: '**/target/**/*.log', allowEmptyArchive: true
        }
        success {
            echo '✅ Build and Tests successful'
        }
        failure {
            echo '❌ Build failed – check console output'
        }
    }
}
