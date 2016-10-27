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

package com.joelws.quest.listener

import com.github.drapostolos.rdp4j.DirectoryListener
import com.github.drapostolos.rdp4j.FileAddedEvent
import com.github.drapostolos.rdp4j.FileModifiedEvent
import com.github.drapostolos.rdp4j.FileRemovedEvent
import com.joelws.quest.SftpOperation
import com.joelws.quest.TEMP_DIR
import com.joelws.quest.handler.MavenUploadHandler
import com.joelws.quest.handler.UnzipHandler
import org.slf4j.LoggerFactory
import rx.lang.kotlin.single
import rx.schedulers.Schedulers

class ZipListener(private val sftpOperation: SftpOperation,
                  private val workingDir: String,
                  private val zipHandler: UnzipHandler,
                  private val mavenUploadHandler: MavenUploadHandler) : DirectoryListener {

    private val logger = LoggerFactory.getLogger(ZipListener::class.java)

    override fun fileModified(event: FileModifiedEvent) {

    }

    override fun fileRemoved(event: FileRemovedEvent) {

    }

    override fun fileAdded(event: FileAddedEvent) {

        logger.info("We decide to go down a different path...")

        val fileName = event.fileElement.name.removeSuffix(".antivirus.scanning")

        logger.info("Found a file on the way: $fileName")

        val absoluteName = "$workingDir/$fileName"

        val tempDirFileName = "$TEMP_DIR/$fileName"

        val mavenUploadSubscription = single<Unit> {
            logger.info("Starting download of $fileName to directory[$TEMP_DIR]")

            sftpOperation.get(absoluteName, tempDirFileName)

            logger.info("Downloading of $fileName is complete")

            zipHandler.execute(tempDirFileName)

            mavenUploadHandler.execute(tempDirFileName)

        }.subscribeOn(Schedulers.io()).subscribe(
                { logger.info("Task completed, continuing...") },
                { e -> logger.error("Task encountered error: ", e) }
        )


    }

}
