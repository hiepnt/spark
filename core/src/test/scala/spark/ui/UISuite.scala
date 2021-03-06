/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spark.ui

import scala.util.{Failure, Success, Try}
import java.net.ServerSocket
import org.scalatest.FunSuite
import org.eclipse.jetty.server.Server

class UISuite extends FunSuite {
  test("jetty port increases under contention") {
    val startPort = 33333
    val server = new Server(startPort)
    server.start()
    val (jettyServer1, boundPort1) = JettyUtils.startJettyServer("localhost", startPort, Seq())
    val (jettyServer2, boundPort2) = JettyUtils.startJettyServer("localhost", startPort, Seq())

    assert(boundPort1 === startPort + 1)
    assert(boundPort2 === startPort + 2)
  }

  test("jetty binds to port 0 correctly") {
    val (jettyServer, boundPort) = JettyUtils.startJettyServer("localhost", 0, Seq())
    assert(jettyServer.getState === "STARTED")
    assert(boundPort != 0)
    Try {new ServerSocket(boundPort)} match {
      case Success(s) => fail("Port %s doesn't seem used by jetty server".format(boundPort))
      case Failure  (e) =>
    }
  }
}
