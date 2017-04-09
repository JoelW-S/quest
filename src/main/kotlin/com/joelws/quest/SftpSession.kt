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

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session

object SftpSession {

    private val factory: JSch

    init {
        this.factory = JSch()
    }

    fun getSession(host: String, userName: String, password: String): Session {
        return this.factory
                .getSession(userName, host, SFTP_PORT)
                .apply {
                    setPassword(password)
                    setConfig(HOST_CHECKING_KEY, HOST_CHECKING_VALUE)
                }

    }

    fun getChannelSftp(session: Session): ChannelSftp {

        session.connect()

        val channel = session.openChannel(CHANNEL_TYPE)

        channel.connect()

        return channel as ChannelSftp
    }
}
