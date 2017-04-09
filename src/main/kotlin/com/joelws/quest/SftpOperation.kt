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

import com.github.drapostolos.rdp4j.spi.FileElement
import com.jcraft.jsch.ChannelSftp
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class SftpOperation(private val details: SftpDetails,
                    private val sftpSession: SftpSession = SftpSession) {

    private val logger = LoggerFactory.getLogger(SftpOperation::class.java)

    fun listFiles(workingDirectory: String): Set<FileElement> {
        val channel = getChannel()
        try {

            @Suppress("UNCHECKED_CAST")
            val listOfFiles = channel
                    .ls(workingDirectory) as Vector<ChannelSftp.LsEntry>
            return listOfFiles
                    .map(::SftpFile)
                    .toSet()

        } finally {
            channel
                    .session
                    .disconnect()
        }
    }

    fun get(absoluteFileName: String, destFileName: String) {

        val channel = getChannel()

        var fos: FileOutputStream? = null

        try {
            File(destFileName).parentFile?.mkdir()
            fos = FileOutputStream(destFileName)
            channel.get(absoluteFileName, fos)
        } catch (e: IOException) {
            logger.error("Failed to write file: ", e)
        } finally {
            fos?.close()

            channel
                    .session
                    .disconnect()
        }

    }

    fun put(src: String, dest: String) {
        val channel = getChannel()

        try {
            channel.put(src, dest)
        } finally {
            channel
                    .session
                    .disconnect()
        }
    }

    private fun getChannel(): ChannelSftp {
        val session = this.sftpSession.getSession(
                details.host,
                details.userName,
                details.password
        )
        return sftpSession
                .getChannelSftp(session)
    }

}
