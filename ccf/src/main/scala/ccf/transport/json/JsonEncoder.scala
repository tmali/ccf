/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ccf.transport.json

import ccf.transport.Encoder
import ccf.transport.{TransportRequest, TransportResponse}
import com.twitter.json.Json

object JsonEncoder extends Encoder {
  def encodeRequest(r: TransportRequest): String = Json.build(toMap(r.headers, r.content)).toString
  def encodeResponse(r: TransportResponse): String = Json.build(toMap(r.headers, r.content)).toString
  private def toMap(headers: Map[String, String], content: Option[Any]) = content match {
    case Some(c) => Map("headers" -> headers, "content" -> c)
    case None    => Map("headers" -> headers)
  }
}
