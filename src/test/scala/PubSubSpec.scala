import org.scalatest._
import Distribution._
import DistributorCounter._

class PubSubSpec extends FlatSpec with Matchers  {

  "A PubSub" should "let a handler subscribe" in {
    val pubSub = new PubSub
    pubSub.subscribe((id: Int, event: Event) => ())
    pubSub.handlers.length should be (1)
  }
  it should "add event to storage" in {
    val pubSub = new PubSub
    pubSub.publish(SubscriptionOpen, 1)
    pubSub.storage.length should be (1)
  }
  it should "call the handler" in {
    val pubSub = new PubSub
    var events: List[Event] = List()
    pubSub.subscribe((id: Int, event: Event) => {
      events = events :+ event
      ()
    })
    pubSub.publish(SubscriptionOpen, 2)
    events.length should be (1)
  }
  it should "display updated projection when send command" in {
    //setup
    val pubSub = new PubSub
    pubSub.subscribe(HandlerEvent)
    // service applicatif
    val someEvent = Distribution.processCommand(Subscribe(UserId("1")), History(List(SubscriptionOpen)))
    someEvent match {
      case Right(value) => pubSub.publish(value, 1)
      case _ =>
    }
    //test (vue)
    countMap(1) should be (1)
  }
}
