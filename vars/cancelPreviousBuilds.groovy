#!/usr/bin/env groovy

// This is untested. Been trying a number of ways of
// aborting the AWS_Master_Tests job if another run kicks off.
// see https://stackoverflow.com/questions/40760716/jenkins-abort-running-build-if-new-one-is-started
def cancelPreviousBuilds() {
    def buildNumber = env.BUILD_NUMBER as int
    if (buildNumber > 1) milestone(buildNumber - 1)
    milestone(buildNumber)
}