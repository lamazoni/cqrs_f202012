import Distribution._

//class HandlerEventGeneric ( event : Event)

object DistributorCounter {
  type MutableMap[K, V] = scala.collection.mutable.Map[K, V]
  val countMap: MutableMap[Int, Int] = scala.collection.mutable.Map()

  def increment(id: Int) = {
    val previousValue = countMap.getOrElse(id, 0)
    countMap.put(id, previousValue + 1)
  }

  def decrement(id: Int) = {
    val previousValue = countMap.getOrElse(id, 0)
    countMap.put(id, previousValue - 1)
  }

  def HandlerEvent(id: Int, event : Event) = {
    event match {
      case DistributorSubscribed(userid) => DistributorCounter.increment(id)
      case DistributorUnsubscribed(userid) => DistributorCounter.decrement(id)
      case _ =>
    }
    ()
  }
}
