package ccf.session

import ccf.transport.{Connection, Request, Response, ConnectionException}

case class Session(connection: Connection, version: Version, clientId: ClientId, seqId: Int, channels: Set[ChannelId]) {
  def next(channels: Set[ChannelId]) = Session(connection, version, clientId, seqId + 1, channels)
  def send(msg: Message): Session = msg.send(this) match {
    case (session: Session, Some(response)) => session
    case (session: Session, None)           => session
  }
}
