package kr.iolo.berry.svc;
 
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.kernel.Bootable
import akka.event.Logging

import org.joda.time.DateTime
import akka.dispatch.Future

trait Dao[ModelType, KeyType] {
  def insert(entry:ModelType):ModelType

  def update(entry:ModelType):Unit

  def findOne(id:KeyType):ModelType

  def findAll():Iterable[ModelType]

  def delete(id:KeyType):Unit

  def countAll():Int
}

case class Entry(id:String, title:String, updated:DateTime);

class HashMapEntryDao extends Dao[Entry, String] {

  val entries = new collection.mutable.HashMap[String,Entry]

  def insert(entry:Entry):Entry = {
    val newId = java.util.UUID.randomUUID.toString
    val newEntry = entry.copy(newId)
    entries += (newId -> newEntry)
    newEntry
  }

  def update(entry:Entry) = {
    entries += (entry.id -> entry)
  }

  def findOne(id: String):Entry = {
    entries(id)
  }

  def findAll():Iterable[Entry]  = {
    entries.values
  }

  def delete(id: String) = {
    entries -= id
  }

  def countAll():Int = {
    entries.size
  }
}

sealed trait BerryMessage

case class AddEntry(entry:Entry) extends BerryMessage
case class RemoveEntry(id:String) extends BerryMessage
case class FindEntry(id:String) extends BerryMessage
case class ListEntries(offset:Int=0,limit:Int=0) extends BerryMessage

class BerryService extends Actor {
  val log = Logging(context.system, this)

  val entryDao = new HashMapEntryDao()

  def receive = {
    case AddEntry(entry) => {
      log.info("addEntry: entry=" + entry)
      sender ! entryDao.insert(entry)
    }
    case RemoveEntry(id) => {
      log.info("removeEntry: id=" + id)
      entryDao.delete(id)
    }
    case FindEntry(id) => {
      log.info("findEntry: id=" + id)
      sender ! entryDao.findOne(id)
    }
    case ListEntries(offset,limit) => {
      log.info("listEntries: offset=" + offset + ",limit=" + limit)
      sender ! entryDao.findAll()
    }
  }
}

class BerryKernel extends Bootable {
  val system = ActorSystem("berrySystem")
  def startup = {
    system.actorOf(Props[BerryService], "berryService")
  }
  def shutdown = {
    system.shutdown()
  }
}
