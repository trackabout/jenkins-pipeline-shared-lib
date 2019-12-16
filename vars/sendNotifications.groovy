#!/usr/bin/env groovy

/**
 * Send notifications based on build status string
 * Thanks to Liam Newman https://jenkins.io/blog/2017/02/15/declarative-notifications/
 */
def call(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESS'

  // Default values
  def colorCode = '#770000'
  def subject = "${buildStatus}: Job `${env.JOB_NAME} [${env.BUILD_NUMBER}]`"
  def summary = "${subject} (${env.BUILD_URL})"

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    colorCode = '#FFFF00' // Yellow
  } else if (buildStatus == 'SUCCESS') {
    colorCode = '#00FF00' // Green
  } else {
    colorCode = '#770000' // Red
  }

  // Send notifications
  slackSend (color: colorCode, message: summary)
}