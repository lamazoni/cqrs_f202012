import org.scalatest._
import Distribution._

class DistributionSpec extends FlatSpec with Matchers {

  "An Event processor" should "return SubscriptionOpen when given StartSubscription" in {
    processCommand(StartSubscription, History(List())) should be (Right(SubscriptionOpen))
  }
  it should "return DistributorSubscribed when given Subscribe" in {
    processCommand(Subscribe(UserId("1")), History(List(SubscriptionOpen))) should be (Right(DistributorSubscribed(UserId("1"))))
  }
  it should "return CannotSubscribe when not SubscriptionOpen" in {
    processCommand(Subscribe(UserId("1")), History(List())) should be (Left(CannotSubscribe))
  }
  it should "return DistributorUnsubscribed when given Unsubscribe and user is Subscribed" in {
    processCommand(Unsubscribe(UserId("1")), History(List(DistributorSubscribed(UserId("1"))))) should be (Right(DistributorUnsubscribed(UserId("1"))))
  }
  it should "return NotSubscribed when when given Unsubscribe and user is not already Subscribed" in {
    processCommand(Unsubscribe(UserId("1")), History(List())) should be (Left(NotSubscribed))
  }
  it should "return NotSubscribed when when given Unsubscribe and user is not already Subscribed with the same id" in {
    processCommand(Unsubscribe(UserId("2")), History(List(DistributorSubscribed(UserId("1"))))) should be (Left(NotSubscribed))
  }
}
