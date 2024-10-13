pipeline {
    agent any
    tools {
        jdk "JAVA_HOME"
        maven "M2_HOME"
    }
    stages {
        stage('Git Clone') {
            steps {
                git 'https://github.com/DVSR1411/onlinebookstore.git'
            }
        }
        stage('Maven Build') {
            steps {
                sh "mvn clean install package"
            }
        }
        stage('Nexus Artifact Upload') {
            steps{
                nexusArtifactUploader artifacts: [[artifactId: 'onlinebookstore', classifier: '', file: 'target/onlinebookstore.war', type: 'war']], credentialsId: 'nexus', groupId: 'onlinebookstore', nexusUrl: '15.207.107.124:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'onlinebookstore', version: '0.0.1-SNAPSHOT'
            }
        }
        stage('Docker Build') {
            steps {
                sh "sudo docker build -t dvsr1411/onlinebookstore:v1 ."
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([string(credentialsId: 'demo', variable: 'dockercred')]) {
                    sh "sudo docker login -u dvsr1411 -p $dockercred"
                    sh "sudo docker push dvsr1411/onlinebookstore:v1" 
                }
            }
        }
    }
    post {
        always {
            script {
                try {
                    emailext (
                        subject: "Build ${currentBuild.currentResult}: ${env.JOB_NAME} #${env.BUILD_ID}",
                        body: """
                            <html>
                            <body>
                            <p>Build was ${currentBuild.currentResult}!</p>
                            <p><b>Build ID:</b> ${env.BUILD_ID}</p>
                            <p><b>Job Name:</b> ${env.JOB_NAME}</p>
                            <p><b>Jenkins Home:</b> ${env.JENKINS_HOME}</p>
                            <p>Check the <a href="${env.BUILD_URL}">Console Output</a></p>
                            </body>
                            </html>
                        """,
                        to: 'd.v.sathwikreddy@gmail.com',
                        from: 'd.v.sathwikreddy1411@gmail.com',
                        mimeType: 'text/html'
                    )
                    echo "Email sent successfully!"
                } 
                catch (Exception e) {
                    echo "Failed to send email: ${e.message}"
                }
            }
        }
    }
}
