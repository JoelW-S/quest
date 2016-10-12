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
package com.joelws.simple.poller

import java.util.regex.Matcher
import java.util.regex.Pattern

fun executeIfMatches(str: String, execute: (matcher: Matcher) -> Unit) {
    val pattern = Pattern.compile("(.*-)(\\d+)(.zip)")
    val matcher = pattern.matcher(str)

    if (matcher.matches()) {
        execute(matcher)
    }
}

