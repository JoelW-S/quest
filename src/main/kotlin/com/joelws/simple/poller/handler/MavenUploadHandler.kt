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

package com.joelws.simple.poller.handler

import com.joelws.simple.poller.TEMP_DIR
import com.joelws.simple.poller.executeIfMatches
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

class MavenUploadHandler : Handler<String, CompletableFuture<Unit>> {


    private val logger = LoggerFactory.getLogger(MavenUploadHandler::class.java)


    override fun execute(input: String): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync {
            executeIfMatches(input) { matcher ->

                val folderName = matcher.group(2)

                logger.info("Executing mvn deploy script...")

                val proc = ProcessBuilder("$TEMP_DIR/$folderName/mvn_upload_$folderName.sh").start()

                proc.inputStream.bufferedReader().forEachLine {
                    logger.info(it)
                }

                val exitCode = proc.waitFor()

                logger.info("Finished executing mvn deploy script...")

                if (exitCode > 0) {
                    throw IllegalStateException("Upload failed and exited with $exitCode")
                }

            }
        }
    }
}
