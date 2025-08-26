pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'  // Configure this in Jenkins (Manage Jenkins → Global Tool Configuration)
        jdk 'JAVA_HOME'     // Configure JDK version in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // This will automatically pull your repo (branch configured in Jenkins job)
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
            echo 'Archiving build logs and reports...'
            archiveArtifacts artifacts: '**/target/**/*.log', allowEmptyArchive: true
        }
        success {
            echo 'Build and Tests successful ✅'
        }
        failure {
            echo 'Build failed ❌'
            // Uncomment if you want email alerts
            // mail to: 'sasikumar@qaoncloud.com',
            //      subject: "Jenkins Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            //      body: "Check Jenkins console output at ${env.BUILD_URL}"
        }
    }
}
