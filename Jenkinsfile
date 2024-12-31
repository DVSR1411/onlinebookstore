pipeline {
    agent any
    tools {
        jdk 'JDK'
        maven 'Maven'
        git 'Git'
        dockerTool 'docker'
    }          
    environment {
        DOCKER_REGISTRY = 'your-registry'
        DOCKER_IMAGE = 'your-image'
        DOCKER_TAG = 'latest'
    }
    stages {
        stage('Clone Repository') {
            steps {
                cleanWs()
                git branch: 'master', url: 'https://github.com/dvsr1411/onlinebookstore.git'
            }
        }
        stage('Build with Maven') {
            steps {
                sh 'mvn clean install package'
            }
        }
        stage('Push to Nexus') {
            steps {
                nexusArtifactUploader artifacts: [[artifactId: 'onlinebookstore', classifier: '', file: 'target/onlinebookstore.war', type: 'war']], credentialsId: 'demo', groupId: 'onlinebookstore', nexusUrl: 'NEXUS_URL', nexusVersion: 'nexus3', protocol: 'http', repository: 'onlinebookstore', version: '0.0.1-SNAPSHOT'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://${DOCKER_REGISTRY}', 'docker-credentials-id') {
                        docker.image("${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
