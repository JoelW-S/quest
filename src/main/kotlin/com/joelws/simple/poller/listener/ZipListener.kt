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

package com.joelws.simple.poller.listener

import com.github.drapostolos.rdp4j.DirectoryListener
import com.github.drapostolos.rdp4j.FileAddedEvent
import com.github.drapostolos.rdp4j.FileModifiedEvent
import com.github.drapostolos.rdp4j.FileRemovedEvent
import com.joelws.simple.poller.SftpOperation
import kotlinx.coroutines.async
import org.slf4j.LoggerFactory

class ZipListener(private val sftpOperation: SftpOperation, private val workingDir: String) : DirectoryListener {

    private val logger = LoggerFactory.getLogger(ZipListener::class.java)

    override fun fileModified(event: FileModifiedEvent) {
        logger.info(event.fileElement.name)
    }

    override fun fileRemoved(event: FileRemovedEvent) {
        logger.info("File removed: ${event.fileElement.name}")
    }

    override fun fileAdded(event: FileAddedEvent) {

        logger.info("File added: ${event.fileElement.name}")

        val absoluteName = "$workingDir/${event.fileElement.name}"

        async<Unit> {
            logger.info("Starting download of ${event.fileElement.name}")

            await(sftpOperation.get(absoluteName, event.fileElement.name))

            logger.info("Downloading of ${event.fileElement.name} is complete")
        }


    }

}
