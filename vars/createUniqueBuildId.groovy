#!/usr/bin/env groovy
def call()
{
    def now = new Date()
    return now.format("yyyyMMddHHmmssSSS")
}