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

import com.github.drapostolos.rdp4j.spi.FileElement
import com.github.drapostolos.rdp4j.spi.PolledDirectory
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.SftpException
import org.slf4j.LoggerFactory

class SftpDirectory(private val sftpOperation: SftpOperation, private val workingDirectory: String) : PolledDirectory {

    private val logger = LoggerFactory.getLogger(SftpDirectory::class.java)

    override fun listFiles(): Set<FileElement> {

        return try {

            sftpOperation.listFiles(workingDirectory)

        } catch (e: JSchException) {
            logger.error("Something went wrong: ", e)
            emptySet<FileElement>()

        } catch(e: SftpException) {
            logger.error("Something went wrong: ", e)
            emptySet<FileElement>()
        }
    }
}
