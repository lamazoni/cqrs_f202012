import Distribution._
import org.scalatest._


class DistributorCounterSpec extends FlatSpec with Matchers {

  "A DistributorCounter" should "increment it's counter when given DistributorSubscribed" in {
    DistributorCounter.HandlerEvent(1, DistributorSubscribed(UserId("1")))
    DistributorCounter.HandlerEvent(1, DistributorSubscribed(UserId("2")))
    DistributorCounter.countMap(1) should be (2)
  }
  it should "decrement it's counter when given DistributorUnsubscribed" in {
    DistributorCounter.HandlerEvent(2, DistributorSubscribed(UserId("1")))
    DistributorCounter.HandlerEvent(2, DistributorSubscribed(UserId("2")))
    DistributorCounter.HandlerEvent(2, DistributorUnsubscribed(UserId("1")))
    DistributorCounter.countMap(2) should be (1)
  }
}
