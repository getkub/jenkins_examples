import groovy.json.JsonOutput;

pipeline {
   agent any

    environment {
        GITHUB_CREDS = credentials('some-github-credentials')
        GITHUB_CREDS_USERNAME = "${env.GITHUB_CREDS_USER}"
        GITHUB_CREDS_PASSWORD = "${env.GITHUB_CREDS_PASSWD}"
    }
    
   stages {
      stage('DisableBranchProtection') {
         steps {
            script {
                def repos = [
                "my-repo-1",
                "my-repo-2"                
                ]
                def branches = [
                    "master",
                    "dev"
                ]
                def data = [
                    required_pull_request_reviews: [
                     dismiss_stale_reviews: true,
                     require_code_owner_reviews: false,
                     required_approving_review_count: 1
                    ],
                    enforce_admins: false,
                    restrictions: null,
                    required_status_checks: null
                ]
                def branchProtection= JsonOutput.toJson(data)
                repos.each { repo ->
                    try {
                        branches.each { branch ->
                            print repo + " " + branch
                            try {
                                httpRequest authentication: 'some-github-credentials', contentType: 'APPLICATION_JSON', httpMode: 'PUT', ignoreSslErrors: true, consoleLogResponseBody: false, requestBody: branchProtection ,customHeaders: [[name: 'Accept',value: 'application/vnd.github.luke-cage-preview+json' ]],url: "https://api.github.com/repos/my-org/${repo}/branches/${branch}/protection", validResponseCodes: '200'
                            } catch(e) {
                                println "Issue with : REPO:${repo} - Branch:${branch}"
                            }
                        }   
                    } catch(e) {
                        println "Something wrong with REPO - ${repo}"
                    }
                }
            }
         }
      }
   }
}
