#!/usr/bin/env groovy
def cancelPreviousBuilds() {
    def buildNumber = env.BUILD_NUMBER as int
    if (buildNumber > 1) milestone(buildNumber - 1)
    milestone(buildNumber)
}