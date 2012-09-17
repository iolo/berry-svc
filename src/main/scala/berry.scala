package kr.iolo.berry.svc;
 
import akka.actor._
import akka.dispatch._
import akka.util.duration._
import org.joda.time._
 
class BerryEntry(val id:String, val title:String, val updated:DateTime) {
  override def toString = "[BerryEntry: id=%s, title=%s, updated=%s]".format(id, title, updated)
}

object BerryEntry {
  def apply(id:String, title:String, updated:DateTime) = new BerryEntry(id, title, updated)
}

trait BerryService {
  def addEntry(entry:BerryEntry): Unit

  def removeEntry(id:String): Unit

  def listEntries(): Future[Iterable[BerryEntry]]
}

// in-memory impl
class BerryServiceImpl extends BerryService {

  val entries = new collection.mutable.HashMap[String,BerryEntry]

  import TypedActor.dispatcher

  def addEntry(entry:BerryEntry) = {
    entries += (entry.id -> entry)
  }

  def removeEntry(id:String) = {
    entries -= id
  }

  def listEntries() = {
    Promise.successful(entries.values)
  }
}

object BerryLocalTestApp extends App {
 
  doLocalTest()
 
  def doLocalTest() {
    val system = ActorSystem("BerrySystem")
 
    val berry:BerryService = TypedActor(system).typedActorOf(TypedProps[BerryServiceImpl]())

    berry.addEntry(BerryEntry("1", "hello", DateTime.now))

    berry.addEntry(BerryEntry("2", "world", DateTime.now))

    println(Await.result(berry.listEntries(), 1 seconds))

    berry.removeEntry("1")

    println(Await.result(berry.listEntries(), 1 seconds))

    berry.removeEntry("2")

    println(Await.result(berry.listEntries(), 1 seconds))
  }
}
