pipeline {
    agent any

    environment {
        APP_NAME       = "camper-park"
        LOCAL_PATH     = "/var/www/backend/camper_park"
        EXTERNAL_PORT  = "4500"
        DB_NAME        = "camper_park"
        SPRING_PROFILE = "${params.PROFILE}"
    }

    parameters {
        string(name: 'branch_name', defaultValue: 'main', description: 'branch name to deploy')
        choice(name: 'env', choices: ['PROD', 'STAGE'], description: 'env type')
        choice(name: 'PROFILE', choices: ['prod'], description: 'Wybierz środowisko')
    }

    tools {
        maven 'maven'
        jdk 'jdk25'
    }

    stages {
        stage('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    java --version
                    mvn --version
                '''
            }
        }

        stage('Build Application') {
            steps {
                sh "mvn clean package -DskipTests -Dspring.profiles.active=${params.PROFILE}"
            }
        }

        stage('Deploy (Docker Compose)') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'camper_park_db', passwordVariable: 'DB_PASSWORD', usernameVariable: 'DB_USER'),
                    string(credentialsId: 'prod-db-root-pass', variable: 'DB_ROOT_PASSWORD'),
                    file(credentialsId: 'camper_park_RSA_private-key', variable: 'RSA_FILE'),
                    file(credentialsId: 'camper_park-RSA-key', variable: 'RSA_PUB_FILE')
                ]) {
                    sh '''
                        export RSA_PRIVATE_PATH=$RSA_FILE
                        export RSA_PUBLIC_PATH=$RSA_PUB_FILE

                        echo "Zatrzymywanie starych kontenerów..."
                        docker compose -f compose-prod.yaml down

                        echo "Budowanie i uruchamianie nowych..."
                        docker compose -f compose-prod.yaml up --build -d
                    '''
                }
            }
        }
    }

    post {
        always {
            // old images cleanup
            sh 'docker image prune -f || true'
            cleanWs()
        }
        success {
            echo "✅ Aplikacja działa w środowisku ${params.PROFILE} na porcie ${EXTERNAL_PORT}."
        }
        failure {
            echo "❌ Coś poszło nie tak... Sprawdź logi w Jenkinsie."
        }
    }
}