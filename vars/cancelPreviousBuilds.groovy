def call() {
    // Check for other instances of this particular build, cancel any that are older than the current one
    def jobName = env.JOB_NAME
    def currentBuildNumber = env.BUILD_NUMBER.toInteger()
    def currentJob = Jenkins.instance.getItemByFullName(jobName)

    // Loop through all instances of this particular job/branch
    for (def build : currentJob.builds) {
        if (build.isBuilding() && (build.number.toInteger() < currentBuildNumber)) {
            echo "Older build still queued. Sending kill signal to build number: ${build.number}"
            build.doStop()
        }
    }
}