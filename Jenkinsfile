pipeline {
    agent any

    parameters {
        choice(name: 'PROFILE', choices: ['stage', 'prod'], description: 'Wybierz środowisko do wdrożenia')
    }

    tools {
        maven 'maven'
        jdk 'jdk25'
    }

    environment {
        APP_NAME       = "camper_park"
        SPRING_PROFILE = "${params.PROFILE}"

        EXTERNAL_PORT  = "${params.PROFILE == 'prod' ? '4500' : '4501'}"

        LOCAL_PATH     = "/var/www/backend/camper_park_v2-${params.PROFILE}"
        DB_NAME        = "camper_park_${params.PROFILE}"
    }

    stages {
        stage('Initialize') {
            steps {
                sh '''
                    echo "Wybrane środowisko: ${SPRING_PROFILE}"
                    echo "Port zewnętrzny: ${EXTERNAL_PORT}"
                    echo "Ścieżka bazy danych: ${LOCAL_PATH}"
                    java --version
                    mvn --version
                '''
            }
        }

        stage('Build Application') {
            steps {
            //using hardcoded profile forces spring to use application-prod.properties
                sh "mvn clean package -DskipTests -Dspring.profiles.active=prod
            }
        }

        stage('Deploy (Docker Compose)') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: "camper_park_db_${SPRING_PROFILE}", passwordVariable: 'DB_PASSWORD', usernameVariable: 'DB_USER'),
                    string(credentialsId: "${SPRING_PROFILE}-db-root-pass", variable: 'DB_ROOT_PASSWORD'),
                    file(credentialsId: "${SPRING_PROFILE}-camper_park_RSA_private-key", variable: 'RSA_FILE'),
                    file(credentialsId: "${SPRING_PROFILE}-camper_park-RSA-key", variable: 'RSA_PUB_FILE')
                ]) {
                    sh '''
                        export RSA_PRIVATE_PATH=$RSA_FILE
                        export RSA_PUBLIC_PATH=$RSA_PUB_FILE

                        echo "Zarządzanie środowiskiem przez Docker Compose..."

                        docker compose -p "camper-park-${SPRING_PROFILE}" -f compose-prod.yaml down
                        docker compose -p "camper-park-${SPRING_PROFILE}" -f compose-prod.yaml up --build -d
                    '''
                }
            }
        }
    }

    post {
        always {
            sh 'docker image prune -f || true'
            cleanWs()
        }
        success {
            echo "✅ Aplikacja działa w środowisku ${SPRING_PROFILE} na porcie ${EXTERNAL_PORT}."
        }
        failure {
            echo "❌ Wdrożenie nie powiodło się... Sprawdź logi w Jenkinsie."
        }
    }
}