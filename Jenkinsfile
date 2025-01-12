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
        
        stage('Trivy Image Scan') {
            steps {
                sh 'trivy image --format table -o image.html izarorubio/taskmanager:latest'
            }
        }
        
        stage('Docker Push') {
            steps {
                script{
                withDockerRegistry(credentialsId: 'docker-credential', toolName: 'docker') {
                    sh 'docker push izarorubio/taskmanager:latest'
                }
            }
        }}
        
        stage('K8 Deploy') {
            steps {
                withKubeConfig(caCertificate: '', clusterName: 'devopsshack-cluster', contextName: '', credentialsId: 'k8-token', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://B663F0FF9AE71AEA2D7776C7CB809DF3.gr7.eu-north-1.eks.amazonaws.com') {
                    sh 'kubectl apply -f deployment-service.yml'
                    sleep 30
                }
            }
        }
        
        stage('Verify K8 Deployment') {
            steps {
                withKubeConfig(caCertificate: '', clusterName: 'devopsshack-cluster', contextName: '', credentialsId: 'k8-token', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://B663F0FF9AE71AEA2D7776C7CB809DF3.gr7.eu-north-1.eks.amazonaws.com') {
                    sh 'kubectl get pods -n webapps'
                    sh 'kubectl get svc -n webapps'
                }
            }
        }
    }
}