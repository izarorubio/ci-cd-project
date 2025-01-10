pipeline {
    agent any
    
    tools {
        maven 'maven3'
    }
    
    environment{
        SCANNER_HOME = tool 'sonar-scanner'
    }

    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'dev', url: 'https://github.com/izarorubio/ci-cd-project.git'
                }
        }
    
        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        
        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        
        stage('Tryvy FS Scan') {
            steps {
                sh 'trivy fs --format table -o fs.html .'
            }
        }
    
        stage('Sonar Analysis') {
            steps {
                withSonarQubeEnv('sonar') {
                    sh ''' $SCANNER_HOME/bin/sonar-scanner -Dsonar.projectName-taskmanager -Dsonar.projectKey=taskmanager\
                    -Dsonar.java.binaries=target '''
                }
            }
        }
    
        stage('Build Application') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Publish Artifacts') {
            steps {
                withMaven(globalMavenSettingsConfig: 'settings-maven', jdk: '', maven: 'maven3', mavenSettingsConfig: '', traceability: true) {
                    sh 'mvn deploy'
                }
            }
        }
        
        stage('Docker Build & Tag') {
            steps {
                script{
                withDockerRegistry(credentialsId: 'docker-credential', toolName: 'docker') {
                    sh 'docker build -t izarorubio/taskmanager:latest .'
                }
            }
        }}
    }
}
