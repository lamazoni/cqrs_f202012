import Distribution._

class PubSub(eventStore: EventStore) {

  //var storage : List[Event] = List()
  var handlers: List[(Int, Event) => Unit] = List()

  def subscribe(handler: (Int, Event) => Unit) = {
    handlers = handlers :+ handler
  }

  def publish (event : Event, id: Int, version: Int) = {
    eventStore.addEvent(id, List(event), version)
    handlers.map(handler => handler.apply(id, event))
  }
}