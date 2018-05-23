/**
 *
 * Copyright (c) 2018 Cisco and/or its affiliates.
 *
 * This software is licensed to you under the terms of the Cisco Sample
 * Code License, Version 1.0 (the "License"). You may obtain a copy of the
 * License at
 *
 *                https://developer.cisco.com/docs/licenses
 *
 * All use of the material herein must be in accordance with the terms of
 * the License. All rights not expressly granted by the License are
 * reserved. Unless required by applicable law or agreed to separately in
 * writing, software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 */

package com.example

import java.io.BufferedWriter
import java.nio.charset.Charset.defaultCharset
import java.nio.file.{Files, Paths}
import java.nio.file.StandardOpenOption.{APPEND, CREATE}

import org.dsa.iot.dslink.node.value.SubscriptionValue
import org.dsa.iot.dslink.util.handler.Handler
import org.dsa.iot.dslink.{DSLink, DSLinkFactory, DSLinkHandler}
import org.slf4j.LoggerFactory

/**
  * Created by nshimaza on 2018/05/22.
  */
object Appender {
  def main(args: Array[String]): Unit = {
    val writer = Files.newBufferedWriter(Paths.get(args(1)), defaultCharset, CREATE, APPEND)
    DSLinkFactory.start(args.drop(2), new AppenderDSLinkHandler(args(0), writer))
  }
}


class AppenderDSLinkHandler(nodePath: String, writer: BufferedWriter) extends DSLinkHandler {
  private val log = LoggerFactory.getLogger(getClass)
  override val isRequester = true

  override def onRequesterInitialized(link: DSLink): Unit =  log.info("Appender initialized")

  override def onRequesterConnected(link: DSLink): Unit = {
    log.info("Appender connected")

    link.getRequester.subscribe(nodePath, new Handler[SubscriptionValue] {
      def handle(event: SubscriptionValue): Unit = writer.write(event.getValue.toString)
    })
  }
}
