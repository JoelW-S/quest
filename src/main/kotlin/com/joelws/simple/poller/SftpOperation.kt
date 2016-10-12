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
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.SftpException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.CompletableFuture

class SftpOperation(private val details: SftpDetails,
                    private val sftpSession: SftpSession = SftpSession) {


    fun listFiles(workingDirectory: String): Set<FileElement> {
        val channel = getChannel()
        try {

            @Suppress("UNCHECKED_CAST")
            val listOfFiles = channel.ls(workingDirectory) as Vector<ChannelSftp.LsEntry>
            return listOfFiles.map { SftpFile(it) }.toSet()

        } finally {
            channel.session.disconnect()
        }
    }

    fun get(absoluteFileName: String, destFileName: String): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync {
            val channel = getChannel()

            val fos = FileOutputStream(destFileName)

            try {

                channel.get(absoluteFileName, fos)

            } catch (e: SftpException) {

                throw IOException("Failed to perform write operation", e)

            } finally {
                fos.close()
                channel.session.disconnect()
            }
        }
    }

    private fun getChannel(): ChannelSftp {
        val session = this.sftpSession.getSession(details.host, details.userName, details.password)
        return sftpSession.getChannelSftp(session)
    }

}
