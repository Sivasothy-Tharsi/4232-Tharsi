pipeline {
    agent any
    environment {
    DOCKERHUB_CREDENTIALS = credentials('sivasothy-dockerhub')
    }
    tools {
    git 'Git'
    }

    stages {
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
                sh 'docker run -d -p 3001:3000 sivasothytharsi/nodeapp:12'
            }
        }
}
post {
        always {
            sh 'docker logout'
        }
    }
}

