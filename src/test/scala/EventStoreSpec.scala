import Distribution._
import org.scalatest._

class EventStoreSpec extends FlatSpec with Matchers {

  "Persistence" should "return all events of the aggregate instance" in {
    val myStore = new EventStore
    val myEvents = List(
      SubscriptionOpen,
      DistributorSubscribed(UserId("1")),
      DistributorSubscribed(UserId("2")),
      DistributorUnsubscribed(UserId("2")),
    )
    myStore.addEvent(1, myEvents, 0)
    myStore.getEvents(1) should be (myEvents)
  }
  it should "return only the events of the aggregate instance" in {
    val myStore = new EventStore
    val myEvents1 = List(
      SubscriptionOpen,
      DistributorSubscribed(UserId("1")),
      DistributorSubscribed(UserId("2")),
      DistributorUnsubscribed(UserId("2")),
    )
    val myEvents2 = List(
      SubscriptionOpen,
      DistributorSubscribed(UserId("3")),
      DistributorSubscribed(UserId("4")),
      DistributorUnsubscribed(UserId("3")),
    )
    myStore.addEvent(1, myEvents1, 0)
    myStore.addEvent(2, myEvents2, 0)
    myStore.getEvents(1) should be (myEvents1)
    myStore.getEvents(2) should be (myEvents2)
  }
  it should "check the last known version of the EventStore" in  {
    val myStore = new EventStore
    val myEvents1 = List(
      SubscriptionOpen,
      DistributorSubscribed(UserId("1")),
      DistributorSubscribed(UserId("2")),
      DistributorUnsubscribed(UserId("2")),
    )
    val myEvents2 = List(
      SubscriptionOpen,
      DistributorSubscribed(UserId("3")),
      DistributorSubscribed(UserId("4")),
      DistributorUnsubscribed(UserId("3")),
    )
    val version1 = myStore.getEvents(1).length
    myStore.addEvent(1, myEvents1, version1)
    val version2 = myStore.getEvents(1).length
    myStore.addEvent(1, myEvents2, 1) should be (Left(1))
    myStore.addEvent(1, myEvents2, version2) should be (Right(0))
  }

}
