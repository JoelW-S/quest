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

package com.joelws.quest.handler

import com.joelws.quest.TEMP_DIR
import com.joelws.quest.VALID_ZIP
import org.slf4j.LoggerFactory
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

object MavenUploadHandler : Handler<String, Boolean> {


    private val logger = LoggerFactory.getLogger(MavenUploadHandler::class.java)


    override fun execute(input: String): Boolean {

        val pattern = Pattern.compile(VALID_ZIP)
        val matcher = pattern.matcher(input)

        return makeExecutableAndRun(matcher)

    }

    private fun makeExecutableAndRun(matcher: Matcher): Boolean {
        if (matcher.matches()) {
            val folderName = matcher.group(1)

            val mavenScriptPath = "${TEMP_DIR}${File::separator}$folderName${File::separator}mvn_upload_$folderName.sh"

            val mavenScript = File(mavenScriptPath)

            if (mavenScript.exists()) {

                mavenScript.setExecutable(true)

                return executeScript(mavenScriptPath)

            } else {
                logger.warn("Can't find maven script, skipping..")
            }
        } else {
            logger.warn("File is not a valid artefact zip, skipping...")
        }
        return false
    }

    private fun executeScript(scriptPath: String): Boolean {

        logger.info("Executing mvn deploy script...")

        val proc = ProcessBuilder(scriptPath).start()

        proc
                .inputStream
                .bufferedReader()
                .forEachLine { line ->
                    logger.info(line)
                }

        val exitCode = proc.waitFor()

        logger.info("Finished executing mvn deploy script...")

        if (exitCode > 0) {
            logger.error("Upload failed and exited with $exitCode")
            return false
        }
        return true
    }

}


