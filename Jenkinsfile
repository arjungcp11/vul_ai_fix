pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'JDK17'
    }

    stages {
        stage('clean') {
            steps {
                bat 'mvn clean'
            }
        }

        stage('install') {
            steps {
                bat 'mvn install'
            }
        }

           }
}
