// vars/myPipeline.groovy

def call(String branch) {
    pipeline {
        agent any

        stages {
            stage('Checkout') {
                steps {
                    script {
                        checkout([$class: 'GitSCM', branches: [[name: branch]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/ryb9696/assignment1.git']]])
                    }
                }
            }

            stage('Build') {
                steps {
                    script {
                        sh "chmod +x deploy.sh"
                        sh "./deploy.sh"
                        echo "Build successful"
                    }
                }
            }

            stage('Test') {
                when {
                    expression { branch ==~ /^(qa|uat|prod)$/ }
                }
                steps {
                    script {
                        sh "chmod +x deploy.sh"
                        sh "./deploy.sh"
                        echo "Test successful"
                    }
                }
            }

            stage('Deploy') {
                when {
                    expression { branch ==~ /^(uat|prod)$/ }
                }
                steps {
                    script {
                        sh "chmod +x deploy.sh"
                        sh "./deploy.sh"
                        echo "Deploy successful"
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
}
