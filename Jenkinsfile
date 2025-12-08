pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'JDK17'
    }

    stages {
        stage('git repo & clean') {
            steps {
                bat 'git clone https://github.com/arjungcp11/vul_ai_fix.git'
                bat 'mvn clean -f vul_ai_fix/pom.xml'
            }
        }

        stage('install') {
            steps {
                bat 'mvn install -f vul_ai_fix/pom.xml'
            }
        }

        stage('test') {
            steps {
                bat 'mvn test -f vul_ai_fix/pom.xml'
            }
        }

        stage('package') {
            steps {
                bat 'mvn package -f vul_ai_fix/pom.xml'
            }
        }
    }
}
