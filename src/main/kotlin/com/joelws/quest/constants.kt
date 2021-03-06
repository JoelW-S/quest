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

const val POLLING_INTERVAL = 15L

const val HOST_CHECKING_KEY = "StrictHostKeyChecking"

const val HOST_CHECKING_VALUE = "no"

const val CHANNEL_TYPE = "sftp"

const val SFTP_PORT = 22

const val TEMP_DIR = "tmp"

const val VALID_ZIP = "(HTS_.+)(.zip)"

const val ZIP_SUFFIX = "$VALID_ZIP(.antivirus.scanning)"
