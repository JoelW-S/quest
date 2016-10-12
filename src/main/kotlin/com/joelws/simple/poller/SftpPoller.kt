/*
Copyright 2016 Joel Whittaker-Smith

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.joelws.simple.poller

import com.github.drapostolos.rdp4j.DirectoryPoller
import com.github.drapostolos.rdp4j.RegexFileFilter
import com.joelws.simple.poller.listener.ZipListener
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class SftpPoller {

    companion object {
        const val ZIP_SUFFIX = ".*\\.zip"
        private val logger = LoggerFactory.getLogger(SftpPoller::class.java)
    }

    fun bootstrap() {

        val host = getEnvVar("SFTP_HOST")

        val workingDir = getEnvVar("SFTP_POLL_DIR")

        val username = getEnvVar("SFTP_USER")

        val password = getEnvVar("SFTP_PASS")

        val details = SftpDetails(host, username, password)

        val root = SftpOperation(details)

        val polledDirectory = SftpDirectory(root, workingDir)

        val dp = DirectoryPoller.newBuilder()
                .addPolledDirectory(polledDirectory)
                .addListener(ZipListener(root, workingDir))
                .setPollingInterval(15, TimeUnit.SECONDS)
                .setDefaultFileFilter(RegexFileFilter(ZIP_SUFFIX))
                .start()

        TimeUnit.MINUTES.sleep(10)

        dp.stop()

    }

    private fun getEnvVar(envVar: String): String {
        return System.getenv()[envVar] ?: throw RuntimeException("SFTP details are not set!")
    }

}
