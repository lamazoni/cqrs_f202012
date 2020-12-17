object Distribution extends App {

  trait Event
  trait Command
  trait Error

  case class UserId(value: String)

  case object StartSubscription extends Command
  case class Subscribe(userId: UserId) extends Command
  case class Unsubscribe(userId: UserId) extends Command

  case object SubscriptionOpen extends Event
  case class DistributorSubscribed(userId: UserId) extends Event
  case class DistributorUnsubscribed(userId: UserId) extends Event

  case object CannotSubscribe extends Error
  case object NotSubscribed extends Error

  case class History(events: List[Event])

  def processCommand(command: Command, history: History): Either[Error, Event] = {
    val users = getSubscribedDistributor(history)
    command match {
      case StartSubscription => Right(SubscriptionOpen)
      case Subscribe(userId) => {
        if (isSubscriptionOpen(history)) Right(DistributorSubscribed(userId)) else Left(CannotSubscribe)
      }
      case Unsubscribe(userId) => {
        if(isSubscribed(history,userId)) Right(DistributorUnsubscribed(userId)) else Left(NotSubscribed)
      }
    }

  }

  private def isSubscriptionOpen(history: History): Boolean = {
    history.events.contains(SubscriptionOpen)
  }

  private def isSubscribed(history: History, userId: UserId): Boolean = {
    history.events.lastIndexOf(DistributorSubscribed(userId)) > history.events.lastIndexOf(DistributorUnsubscribed(userId))
  }

  private def getSubscribedDistributor(history: History): List[UserId] = {
//        history.events.fold(List(): List[UserId]) ((event: Event, distributor: List[UserId]) => {
//          event match {
//            case DistributorSubscribed(userId) => userId :: distributor
//            case DistributorUnsubscribed(userId) => distributor.filter(_ == userId)
//          }
//        })

    //    history.events.fold(List(): List[UserId]) ((e1: Event, e2: Event) => {
    //      (e1,e2) match {
    //        case DistributorSubscribed(userId) => userId
    ////          userId :: distributor
    //        case DistributorUnsubscribed(userId) => Nil
    //      }
    //    })
    //

//        history.events.filter( event => event match {
//          case DistributorSubscribed(userId) => true
//          case _ => false
//        }).map( disSubsEvent => { disSubsEvent match {
//          case DistributorSubscribed(userId) => userId
//          case _ => 0
//        }}).filter( _ != 0 ).map( x=> UserId(x))

    //      for {
    //        event <- history.events
    //        if (event match {
    //          case  DistributorSubscribed(userId) => true
    //          case _ => false
    //        })
    //      } yield event

//    history.events.filter( event => event match {
//      case DistributorSubscribed(userId) => true
//      case _ => false
//    }).map( disSubsEvent => { disSubsEvent match {
//      case DistributorSubscribed(userId: UserId) => userId
//      case _ => UserId("0")
//    }}).filter( _ != UserId("0"))

    history.events.foldLeft(List(): List[UserId]) ((distributor: List[UserId], event: Event) => {
      event match {
        case DistributorSubscribed(userId) => userId :: distributor
        case DistributorUnsubscribed(userId) => distributor.filter(_ == userId)
        case _ => distributor
      }
    })

//    List(1, 2, 3);
//
//    1.toString();
//
//    List(1, 2, 3)
//      .foldLeft("")((acc: String, value: Int) => {
//        acc.toString().concat(value.toString())
//      });

  }
}