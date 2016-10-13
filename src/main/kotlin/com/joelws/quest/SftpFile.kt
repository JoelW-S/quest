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
import com.jcraft.jsch.ChannelSftp.LsEntry

class SftpFile(private val lsEntry: LsEntry) : FileElement {

    override fun isDirectory(): Boolean = lsEntry.attrs.isDir

    override fun getName(): String = lsEntry.filename

    override fun lastModified(): Long = lsEntry.attrs.mTime.toLong()

}
