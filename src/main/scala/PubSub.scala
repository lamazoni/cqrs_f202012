import Distribution._

class PubSub {

  var storage : List[Event] = List()
  var handlers: List[(Int, Event) => Unit] = List()

  def subscribe(handler: (Int, Event) => Unit) = {
    handlers = handlers :+ handler
  }

  def publish (event : Event, id: Int) = {
    storage = storage :+ event
    handlers.map(handler => handler.apply(id, event))
  }
}
