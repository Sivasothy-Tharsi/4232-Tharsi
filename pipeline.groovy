pipeline {
    agent any
    environment {
    DOCKERHUB_CREDENTIALS = credentials('sivasothy-dockerhub')
    }
    tools {
    git 'Git'
    }

    stages {
        stage('SCM Checkout') {
            steps{
            git 'https://github.com/Sivasothy-Tharsi/4232-Tharsi.git'
            }
        }

        stage('Build docker image') {
            steps {
                sh 'docker build -t sivasothy/nodeapp:$BUILD_NUMBER .'
            }
        }
        stage('login to dockerhub') {
            steps{
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }
        stage('push image') {
            steps{
                sh 'docker push sivasothy/nodeapp:$BUILD_NUMBER'
            }
        }
}
post {
        always {
            sh 'docker logout'
        }
    }
}

