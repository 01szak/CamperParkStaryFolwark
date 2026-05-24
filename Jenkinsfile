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
        stage('Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'prod-db-camper_park-password', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'prod-db-root-pass', variable: 'DB_ROOT_PASSWORD')
                ]) {
                    sh '''
                        docker compose -f compose-prod.yaml down
                        docker compose -f compose-prod.yaml up --build -d
                    '''
                }
            }
        }
    }
}