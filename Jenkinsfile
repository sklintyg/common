#!groovy

node {
    def buildVersion = "3.16.1.${BUILD_NUMBER}"

    stage('checkout') {
        git url: "https://github.com/sklintyg/common.git", branch: GIT_BRANCH
        util.run { checkout scm }
    }

    stage('build') {
        try {
            shgradle11 "--refresh-dependencies clean build -x Test -DgruntColors=false -DbuildVersion=${buildVersion}"
        } finally {
            publishHTML allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/reports/allTests', \
                reportFiles: 'index.html', reportName: 'JUnit results'
        }
    }

    stage('tag and upload') {
        shgradle11 "--debug uploadArchives tagRelease -DbuildVersion=${buildVersion}"
    }

    stage('notify') {
        util.notifySuccess()
    }
}
