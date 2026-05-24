pipeline {
    agent any
    environment {
        SPRING_PROFILE = 'prod'
        DB_NAME        = 'camper_park_db'
        DB_USER        = 'camper_park_db_user'
        LOCAL_PATH     = '/opt/camper_park/mysql_data'
        EXTERNAL_PORT  = '80'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Miejsce na zbudowanie aplikacji (np. Maven/Gradle) i obrazu Docker
                // sh './mvnw clean package -DskipTests'
                // sh "docker build -t ${DOCKER_IMAGE} ."
                echo "Zbudowano aplikację i obraz Docker..."
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'prod-db-camper_park-password', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'prod-db-root-pass', variable: 'DB_ROOT_PASSWORD')
                ]) {
                    // Dodanie flag '--pull always' (jeśli pobierasz obraz z rejestru)
                    // oraz 'down' przed 'up', aby upewnić się, że ładujemy nową konfigurację
                    sh '''
                        docker compose -f compose-prod.yaml down
                        docker compose -f compose-prod.yaml up -d
                    '''
                }
            }
        }
    }

    post {
        always {
            // Opcjonalne czyszczenie starych, nieużywanych obrazów (dangling images)
            // zapobiega zapychaniu się dysku na serwerze Jenkinsa
            sh 'docker image prune -f'
        }
        success {
            echo "Wdrożenie zakończone sukcesem! Aplikacja powinna być dostępna na porcie ${EXTERNAL_PORT}."
        }
        failure {
            echo "Wdrożenie nie powiodło się. Sprawdź logi Jenkinsa."
        }
    }
}