def call(boolean abortPipeline = false, def scannerHome) {
    withSonarQubeEnv(credentialsId: 'Sonar Token') {
        sh "${scannerHome}/bin/sonar-scanner"
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