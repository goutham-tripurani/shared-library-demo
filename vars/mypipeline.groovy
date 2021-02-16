pipeline {
    agent any
    parameters{
        string(name: 'SCM_URL' description: 'Give URL of SCM to be build')
        string(name: 'SCM_BRANCH' description: 'Give branch name of SCM to be build')
    }
    stages {
        stage('checkout git') {
            steps {
                echo "checkout git stage"
                git branch: "${SCM_BRANCH}", credentialsId: 'goutham-tripurani:Goutham@1259', url: "${SCM_URL}"
            }
        }

        stage('build') {
            steps {
                echo "build stage"
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage ('test') {
            steps {
                parallel (
                    "unit tests": { sh 'mvn test' },
                    "integration tests": { sh 'mvn integration-test' }
                )
            }
        }
      /*
        stage('deploy development'){
            steps {
                deploy(developmentServer, serverPort)
            }
        }

        stage('deploy staging'){
            steps {
                deploy(stagingServer, serverPort)
            }
        }

        stage('deploy production'){
            steps {
                deploy(productionServer, serverPort)
            }
        } */
    }
    post {
        failure {
            //mail to: 'team@example.com', subject: 'Pipeline failed', body: "${env.BUILD_URL}"
            echo "pipeline failed"
        }
    }
}
