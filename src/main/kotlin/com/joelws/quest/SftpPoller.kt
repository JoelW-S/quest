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

package com.joelws.quest

import com.github.drapostolos.rdp4j.DirectoryPoller
import com.github.drapostolos.rdp4j.RegexFileFilter
import com.joelws.quest.handler.MavenUploadHandler
import com.joelws.quest.handler.UnzipHandler
import com.joelws.quest.listener.SftpListener
import java.util.concurrent.TimeUnit

object SftpPoller {

    fun bootstrap() {

        val host = getEnvVar("SFTP_HOST")

        val workingDir = getEnvVar("SFTP_POLL_DIR")

        val username = getEnvVar("SFTP_USER")

        val password = getEnvVar("SFTP_PASS")

        val details = SftpDetails(host, username, password)

        val root = SftpOperation(details)

        val polledDirectory = SftpDirectory(root, workingDir)

        DirectoryPoller
                .newBuilder()
                .addPolledDirectory(polledDirectory)
                .addListener(
                        SftpListener(
                                root,
                                workingDir,
                                UnzipHandler,
                                MavenUploadHandler
                        ))
                .setPollingInterval(POLLING_INTERVAL, TimeUnit.SECONDS)
                .setDefaultFileFilter(RegexFileFilter(ZIP_SUFFIX))
                .start()
    }

    private fun getEnvVar(envVar: String): String {
        return System.getenv()[envVar] ?: throw RuntimeException("SFTP details are not set!")
    }

}
