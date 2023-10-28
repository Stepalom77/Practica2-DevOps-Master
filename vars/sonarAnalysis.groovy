def call(boolean abortPipeline = false, def scannerHome) {
    withSonarQubeEnv(credentialsId: 'sonar-token') {
        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=Threepoints_test \
            -Dsonar.sources=. \
            -Dsonar.host.url=http://localhost:9000 \
            -Dsonar.token=sqp_efae856826fa4b699a9c2ddbe39c2f613a492734"
        timeout(time: 5, unit: 'MINUTES') {
            script {
                def qualityGate = waitForQualityGate()
                if(qualityGate.status != 'OK') {
                    if(abortPipeline) {
                        error "Gate failed ${qualityGate.status}"
                    }
                }
            }
        }
    }
}