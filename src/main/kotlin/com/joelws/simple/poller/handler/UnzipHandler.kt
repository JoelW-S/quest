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

import net.lingala.zip4j.core.ZipFile
import org.slf4j.LoggerFactory
import java.io.File
import java.util.regex.Pattern

class UnzipHandler() : Handler<String, Unit> {

    companion object {
        private val logger = LoggerFactory.getLogger(UnzipHandler::class.java)
    }

    override fun execute(input: String) {


        val pattern = Pattern.compile("(.*-)(\\d+)(.zip)")
        val matcher = pattern.matcher(input)
        while (matcher.find()) {
            val folderName = matcher.group(2)

            val zipFile = File(input)

            val zip = ZipFile(zipFile)

            if (zip.isValidZipFile) {

                logger.info("Unzipping ${zip.file.name}")

                zip.extractAll("${zipFile.parent}")

                logger.info("Finished unzipping ${zip.file.name}")

                File("${zipFile.parent}/$folderName/mvn_upload_$folderName.sh").setExecutable(true)

            }
        }
    }

}
