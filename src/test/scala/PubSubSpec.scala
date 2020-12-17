import org.scalatest._
import Distribution._
import DistributorCounter._

class PubSubSpec  extends FlatSpec with Matchers  {

  "A PubSub" should "let a handler subscribe" in {
    val eventStore = new EventStore
    val pubSub = new PubSub(eventStore)
    pubSub.subscribe((id: Int, event: Event) => ())
    pubSub.handlers.length should be (1)
  }
  it should "add event to storage" in {
    val eventStore = new EventStore
    val pubSub = new PubSub(eventStore)
    pubSub.publish(SubscriptionOpen, 1, 0)
    eventStore.getEvents(1).length should be (1)
  }
  it should "call the handler" in {
    val pubSub = new PubSub(new EventStore)
    var events: List[Event] = List()
    pubSub.subscribe((id: Int, event: Event) => {
      events = events :+ event
      ()
    })
    pubSub.publish(SubscriptionOpen, 2, 0)
    events.length should be (1)
  }
  it should "display updated projection when send command" in {
    //setup
    val aggregateId = 1
    val eventStore = new EventStore
    val pubSub = new PubSub(eventStore)
    eventStore.addEvent(aggregateId, List(SubscriptionOpen), 0)
    pubSub.subscribe(HandlerEvent)
    // service applicatif
    val history = eventStore.getEvents(aggregateId)
    val someEvent = Distribution.processCommand(Subscribe(UserId("1")), History(history))
    someEvent match {
      case Right(value) => pubSub.publish(value, aggregateId, history.length)
      case _ =>
    }
    //test (vue)
    countMap(1) should be (1)
  }
}
