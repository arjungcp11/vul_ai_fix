pipeline {
    agent any
    stages {
        stage('git repo & clean') {
            steps {
               //bat "rmdir  /s /q vulanfixissue"
                bat "git clone https://github.com/arjungcp11/vul_ai_fix.git"
                bat "mvn clean -f vulanfixissue"
            }
        }
        stage('install') {
            steps {
                bat "mvn install -f vulanfixissue"
            }
        }
        stage('test') {
            steps {
                bat "mvn test -f vulanfixissue"
            }
        }
        stage('package') {
            steps {
                bat "mvn package -f vulanfixissue"
            }
        }
    }
}
