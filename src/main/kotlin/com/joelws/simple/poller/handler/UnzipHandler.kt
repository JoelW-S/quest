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
import net.lingala.zip4j.core.ZipFile
import org.slf4j.LoggerFactory
import java.io.File

class UnzipHandler() : Handler<String, Unit> {


    private val logger = LoggerFactory.getLogger(UnzipHandler::class.java)


    override fun execute(input: String) {

        executeIfMatches(input) { matcher ->
            val folderName = matcher.group(2)

            val zip = ZipFile(File(input))

            if (zip.isValidZipFile) {

                logger.info("Unzipping ${zip.file.name}")

                zip.extractAll(TEMP_DIR)

                logger.info("Finished unzipping ${zip.file.name}")

                File("$TEMP_DIR/$folderName/mvn_upload_$folderName.sh").setExecutable(true)
            }
        }
    }

}
