pipeline {
    agent any
    environment {
    DOCKERHUB_CREDENTIALS = credentials('sivasothy-dockerhub')
    GIT_ACCESS_TOKEN = credentials('sivasothy-github')
    }
    tools {
    git 'Git'
    }

    stages {
        stage('Fetch code') {
            steps {
                script {
                    git credentialsId: 'your-access-token-id', url: 'https://github.com/Sivasothy-Tharsi/4232-Tharsi.git'
                }
            }
        }
        stage('Build docker image') {
            steps {
                sh 'docker build -t sivasothytharsi/nodeapp:$BUILD_NUMBER .'
            }
        }
        stage('login to dockerhub') {
            steps{
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }
        stage('push image') {
            steps{
                sh 'docker push sivasothytharsi/nodeapp:$BUILD_NUMBER'
            }
        }
         stage('Run container') {
            steps {
                sh 'docker run -d -p 3001:3000 sivasothytharsi/nodeapp:$BUILD_NUMBER'
            }
        }
}
post {
        always {
            sh 'docker logout'
        }
    }
}

