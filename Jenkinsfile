pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
        jdk 'JDK 21'
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                checkout scm
            }
        }

        stage('Compile & Test') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar \
                        -Dsonar.projectKey=booking-travel-service \
                        -Dsonar.projectName="Booking Travel Service" \
                        -Dsonar.java.binaries=target/classes'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline dihentikan karena SonarQube Quality Gate Gagal: ${qg.status}"
                        }
                    }
                }
            }
        }

        stage('Build Artifact / Docker Image') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
    }
}