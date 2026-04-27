def trimTrailingSlash(String value) {
    if (value == null) {
        return ''
    }
    return value.replaceAll('/+$', '')
}

pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    parameters {
        booleanParam(name: 'PUSH_IMAGES', defaultValue: false, description: 'Push Docker images after building them.')
        booleanParam(name: 'DEPLOY_TO_K8S', defaultValue: false, description: 'Deploy the newly built images to Kubernetes.')
        string(name: 'DOCKER_REGISTRY', defaultValue: '', description: 'Optional Docker registry prefix, for example 123456789012.dkr.ecr.us-west-2.amazonaws.com/final-project')
        string(name: 'DOCKER_IMAGE_TAG', defaultValue: '', description: 'Optional image tag override. Defaults to the Jenkins build number.')
        string(name: 'DOCKER_CREDENTIALS_ID', defaultValue: 'docker-registry-creds', description: 'Jenkins username/password credential used for docker login when PUSH_IMAGES is enabled.')
        string(name: 'KUBECONFIG_CREDENTIALS_ID', defaultValue: 'kubeconfig-final-project', description: 'Jenkins file credential containing a kubeconfig for the target cluster.')
        string(name: 'K8S_NAMESPACE', defaultValue: 'final-project', description: 'Kubernetes namespace used by the manifests in k8s/app.yaml.')
        string(name: 'ORDER_IMAGE_REPO', defaultValue: 'order-service', description: 'Repository name for the order-service image.')
        string(name: 'PAYMENT_IMAGE_REPO', defaultValue: 'payment-service', description: 'Repository name for the payment-service image.')
        string(name: 'FRONTEND_IMAGE_REPO', defaultValue: 'frontend', description: 'Repository name for the frontend image.')
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
    }

    stages {
        stage('Prepare') {
            steps {
                script {
                    env.RESOLVED_IMAGE_TAG = params.DOCKER_IMAGE_TAG?.trim() ? params.DOCKER_IMAGE_TAG.trim() : env.BUILD_NUMBER
                    env.RESOLVED_REGISTRY = trimTrailingSlash(params.DOCKER_REGISTRY?.trim())
                    env.REGISTRY_PREFIX = env.RESOLVED_REGISTRY ? "${env.RESOLVED_REGISTRY}/" : ''
                    env.ORDER_IMAGE = "${env.REGISTRY_PREFIX}${params.ORDER_IMAGE_REPO}:${env.RESOLVED_IMAGE_TAG}"
                    env.PAYMENT_IMAGE = "${env.REGISTRY_PREFIX}${params.PAYMENT_IMAGE_REPO}:${env.RESOLVED_IMAGE_TAG}"
                    env.FRONTEND_IMAGE = "${env.REGISTRY_PREFIX}${params.FRONTEND_IMAGE_REPO}:${env.RESOLVED_IMAGE_TAG}"
                    currentBuild.displayName = "#${env.BUILD_NUMBER} ${env.RESOLVED_IMAGE_TAG}"
                }

                sh 'mkdir -p .m2/repository'
                sh 'printenv | sort | grep -E "BUILD_NUMBER|BRANCH_NAME|CHANGE_ID|RESOLVED_IMAGE_TAG|RESOLVED_REGISTRY|ORDER_IMAGE|PAYMENT_IMAGE|FRONTEND_IMAGE" || true'
            }
        }

        stage('Build and Test') {
            parallel {
                stage('Backend') {
                    agent {
                        docker {
                            image 'maven:3.9.9-eclipse-temurin-17'
                            reuseNode true
                        }
                    }
                    steps {
                        sh 'java -version'
                        sh 'mvn -version'
                        sh 'mvn test'
                    }
                    post {
                        always {
                            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                        }
                    }
                }

                stage('Frontend') {
                    agent {
                        docker {
                            image 'node:22-alpine'
                            reuseNode true
                        }
                    }
                    steps {
                        dir('frontend') {
                            sh 'node --version'
                            sh 'npm --version'
                            sh 'npm ci'
                            sh 'npm run build'
                            sh 'npm test -- --watch=false'
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh "docker build -t ${env.ORDER_IMAGE} -f order-service/Dockerfile ."
                sh "docker build -t ${env.PAYMENT_IMAGE} -f payment-service/Dockerfile ."
                sh "docker build -t ${env.FRONTEND_IMAGE} -f frontend/Dockerfile ."
            }
        }

        stage('Push Docker Images') {
            when {
                expression { params.PUSH_IMAGES }
            }
            steps {
                script {
                    if (!env.RESOLVED_REGISTRY) {
                        error('PUSH_IMAGES is enabled but DOCKER_REGISTRY is empty.')
                    }
                    if (!params.DOCKER_CREDENTIALS_ID?.trim()) {
                        error('PUSH_IMAGES is enabled but DOCKER_CREDENTIALS_ID is empty.')
                    }
                }

                withCredentials([usernamePassword(credentialsId: params.DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh 'echo "$DOCKER_PASSWORD" | docker login "$RESOLVED_REGISTRY" --username "$DOCKER_USERNAME" --password-stdin'
                    sh "docker push ${env.ORDER_IMAGE}"
                    sh "docker push ${env.PAYMENT_IMAGE}"
                    sh "docker push ${env.FRONTEND_IMAGE}"
                    sh 'docker logout "$RESOLVED_REGISTRY"'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { params.DEPLOY_TO_K8S }
            }
            steps {
                script {
                    if (!params.KUBECONFIG_CREDENTIALS_ID?.trim()) {
                        error('DEPLOY_TO_K8S is enabled but KUBECONFIG_CREDENTIALS_ID is empty.')
                    }
                }

                withCredentials([file(credentialsId: params.KUBECONFIG_CREDENTIALS_ID, variable: 'KUBECONFIG')]) {
                    sh 'kubectl version --client'
                    sh "kubectl apply -f k8s/app.yaml"
                    sh "kubectl -n ${params.K8S_NAMESPACE} set image deployment/order-service order-service=${env.ORDER_IMAGE}"
                    sh "kubectl -n ${params.K8S_NAMESPACE} set image deployment/payment-service payment-service=${env.PAYMENT_IMAGE}"
                    sh "kubectl -n ${params.K8S_NAMESPACE} set image deployment/frontend frontend=${env.FRONTEND_IMAGE}"
                    sh "kubectl -n ${params.K8S_NAMESPACE} rollout status deployment/order-service --timeout=180s"
                    sh "kubectl -n ${params.K8S_NAMESPACE} rollout status deployment/payment-service --timeout=180s"
                    sh "kubectl -n ${params.K8S_NAMESPACE} rollout status deployment/frontend --timeout=180s"
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts allowEmptyArchive: true, artifacts: 'order-service/target/*.jar,payment-service/target/*.jar,frontend/dist/**,k8s/*.yaml'
        }
    }
}
