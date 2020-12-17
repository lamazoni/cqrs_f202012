import Distribution._

class EventStore {
  type MutableMap[K, V] = scala.collection.mutable.Map[K, V]
  var store: MutableMap[Int, List[Event]] = scala.collection.mutable.Map()

  def addEvent(aggregateId: Int, events: List[Event], version: Int): Either[Int, Int] = {
    val previousValue = store.getOrElse(aggregateId, List())
    if(previousValue.length == version) {
      store.put(aggregateId, previousValue ::: events)
      Right(0)
    } else {
      Left(1)
    }
  }

  def getEvents(aggregateId: Int): List[Event] = {
    store.getOrElse(aggregateId, List())
  }

}
