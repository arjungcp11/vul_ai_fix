pipeline {
    agent{
        label 'Docker_Container'
    }

    tools {
        maven 'M3'
        jdk 'JDK17'
    }

    stages {
        stage('A') {
            steps {
                git branch: 'main', credentialsId: 'ar', url: 'https://github.com/arjungcp11/vul_ai_fix.git'
            }
        }

        stage('install') {
            steps {
                bat 'mvn install'
            }
        }

           }
}
