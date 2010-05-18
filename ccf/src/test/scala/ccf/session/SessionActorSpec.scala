package ccf.session

import ccf.transport.{Connection, Response, ConnectionException}
import org.specs.Specification
import org.specs.mock.Mockito

object SessionActorSpec extends Specification with Mockito {
  val connection = mock[Connection]
  val clientId = ClientId.randomId
  val version = Version(1, 2)
  val channelId = ChannelId.randomId
  "SessionActor on Join" should {
    val session = newSession(0, Set())
    val joinMessage = Join(channelId)
    val joinRequest = JoinRequest(channelId)(session)
    "reply with Success(...) when server returns valid response request" in {
      val sa = new SessionActor(connection, clientId, version)
      connection.send(joinRequest) returns Some(Response(joinRequest.headers, None))
      sa !? joinMessage must equalTo(Right(Success(joinMessage, None)))
    }
    "reply with Success(...) when server returns no response to request" in {
      val sa = new SessionActor(connection, clientId, version)
      connection.send(joinRequest) returns None
      sa !? joinMessage must equalTo(Right(Success(joinMessage, None)))
    }
  }
  "SessionActor on Part" should {
    val session = newSession(1, Set(channelId))
    val partMessage = Part(channelId)
    val partRequest = PartRequest(channelId)(session)
    "reply with Success(...) when server returns valid response to request" in {
      val sa = new SessionActor(connection, clientId, version, session)
      connection.send(partRequest) returns Some(Response(partRequest.headers, None))
      sa !? partMessage must equalTo(Right(Success(partMessage, None)))
    }
    "reply with Success(...) when server returns no response to request" in {
      val sa = new SessionActor(connection, clientId, version)
      connection.send(partRequest) returns None
      sa !? partMessage must equalTo(Right(Success(partMessage, None)))
    }
  }
  "SessionActor on connection failure" should {
    val session = newSession(0, Set())
    val message = Join(channelId)
    "reply with Failure(...) when message send fails" in {
      val sa = new SessionActor(connection, clientId, version)
      doThrow(new ConnectionException("Error")).when(connection).send(JoinRequest(channelId)(session))
      sa !? message must equalTo(Left(Failure(message, "ccf.transport.ConnectionException: Error")))
    }
  }
  private def newSession(seqId: Int, channels: Set[ChannelId]) = Session(connection, version, clientId, seqId, channels)
}