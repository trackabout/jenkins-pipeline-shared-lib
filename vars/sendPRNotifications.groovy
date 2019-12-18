#!/usr/bin/env groovy
/* Gather the changlog from SCM and boil it down to a string.
Thanks to https://support.cloudbees.com/hc/en-us/articles/217630098/comments/207595368
*/
def getChangelog() {
    MAX_MSG_LEN = 100
    def changeString = ""

    echo "Gathering SCM changes"
    def changeLogSets = currentBuild.rawBuild.changeSets
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            truncated_msg = entry.msg.take(MAX_MSG_LEN)
            changeString += " - ${truncated_msg} [${entry.author}]\n"
        }
    }

    if (!changeString) {
        changeString = " - No new changes"
    }
    return changeString
}

/**
 * Send notifications based on build status string
 * Thanks to Liam Newman https://jenkins.io/blog/2017/02/15/declarative-notifications/
 */
def call(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESS'

  // Default values
  def colorCode = '#770000'
  def changelog = getChangelog()
  def subject = "${buildStatus}: Job `${env.JOB_NAME} [${env.BUILD_NUMBER}]`"
  def slackMessage = """
${subject}
${env.BUILD_URL}
<${env.GITHUB_PR_URL}|PR #${GITHUB_PR_NUMBER}: ${GITHUB_PR_TITLE}>
PR Author: ${GITHUB_PR_TRIGGER_SENDER_AUTHOR}
Changes: ${changelog}
"""

  // Override default values based on build status
  def githubState = 'PENDING'
  if (buildStatus == 'STARTED') {
    githubState = 'PENDING'
    colorCode = '#FFFF00' // Yellow
  } else if (buildStatus == 'SUCCESS') {
    githubState = 'SUCCESS'
    colorCode = '#00FF00' // Green
  } else {
    githubState = 'FAILURE'
    colorCode = '#770000' // Red
  }

  // Send notifications
  slackSend (color: colorCode, message: slackMessage)
  setGitHubPullRequestStatus (context: 'Jenkins/GitHub custom integration', message: subject, state: githubState)
}