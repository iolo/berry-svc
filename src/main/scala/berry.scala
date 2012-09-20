package kr.iolo.berry.svc;
 
import akka.actor._

import akka.dispatch._

import akka.kernel._

import akka.util.duration._

import akka.event.Logging

import org.joda.time.DateTime

import com.typesafe.config.ConfigFactory
 
class Entry(val id:String, val title:String, val updated:DateTime) {
  override def toString = "[BerryEntry: id=%s, title=%s, updated=%s]".format(id, title, updated)
}

object Entry {
  def apply(id:String, title:String, updated:DateTime) = new Entry(id, title, updated)
}

sealed trait BerryMessage

case class AddEntry(entry:Entry) extends BerryMessage
case class RemoveEntry(id:String) extends BerryMessage
case class FindEntry(id:String) extends BerryMessage
case class ListEntries(offset:Int=0,limit:Int=0) extends BerryMessage

class BerryService extends Actor {
  val log = Logging(context.system, this)

  val entries = new collection.mutable.HashMap[String,Entry]

  def receive = {
    case AddEntry(entry) => {
      log.info("addEntry: entry=" + entry)
      entries += (entry.id -> entry)
    }
    case RemoveEntry(id) => {
      log.info("removeEntry: id=" + id)
      entries -= id
    }
    case FindEntry(id) => {
      log.info("findEntry: id=" + id)
      sender ! entries(id)
    }
    case ListEntries(offset,limit) => {
      log.info("listEntries: offset" + offset + ",limit=" + limit)
      sender ! entries.values
    }
  }
}

class BerryKernel extends Bootable {
  val config = ConfigFactory.parseString("""
akka {
  loglevel = "DEBUG"
  stdout-loglevel = "DEBUG"
}
""")
  val system = ActorSystem("berrySystem", ConfigFactory.load(config))
  def startup = {
    system.actorOf(Props[BerryService], "berryService")
  }
  def shutdown = {
    system.shutdown()
  }
}
