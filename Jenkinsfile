pipeline {
    agent any

    parameters {
        choice(name: 'PROFILE', choices: ['stage', 'prod', 'migration-test'], description: 'Wybierz środowisko do wdrożenia')
    }

    tools {
        maven 'maven'
        jdk 'jdk25'
    }

    environment {
        APP_NAME       = "camper_park"
        SPRING_PROFILE = "${params.PROFILE}"

        EXTERNAL_PORT  = "${params.PROFILE == 'prod' ? '2000' : '2001'}"

        LOCAL_PATH     = "/var/www/backend/camper_park_v2-${params.PROFILE}"
        DB_NAME        = "camper_park_${params.PROFILE}"
        
        DUMPS_DIR      = "/home/camper_park/backups"
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
                sh "mvn clean package -DskipTests -Dspring.profiles.active=prod"
            }
        }

        stage('Prepare Migration Dump') {
            when { expression { params.PROFILE == 'migration-test' } }
            steps {
                script {
                    def latestFolder = sh(
                        script: """
                            ls -1 ${DUMPS_DIR} | \
                            grep -E '^[0-9]{2}-[0-9]{2}-[0-9]{4}_[0-9]{2}-[0-9]{2}-[0-9]{2}\$' | \
                            awk -F'[-_]' '{print \$3\$2\$1\$4\$5\$6 \" \" \$0}' | \
                            sort -rn | head -n 1 | cut -d' ' -f2-
                        """, 
                        returnStdout: true
                    ).trim()

                    if (latestFolder == "") {
                        error "Nie znaleziono folderu z backupem w ${DUMPS_DIR}!"
                    }

                    def latestFile = sh(
                        script: "ls ${DUMPS_DIR}/${latestFolder}/*.sql.gz | head -n 1",
                        returnStdout: true
                    ).trim()

                    if (latestFile == "") {
                        error "Nie znaleziono pliku .sql.gz w folderze ${latestFolder}!"
                    }

                    env.DB_DUMP_SOURCE = latestFile
                    echo "Wybrany plik dumpa: ${env.DB_DUMP_SOURCE}"
                }
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
                        # Domyślny backup dla prod/stage, jeśli nie ustawiono DB_DUMP_SOURCE
                        export DB_DUMP_SOURCE=${DB_DUMP_SOURCE:-"/home/kacper/database_backup/2026-05-24_12-00-01/2026-05-24_12-00-01-camper_park.sql.gz"}
                        
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
