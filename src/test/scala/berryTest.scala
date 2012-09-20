package kr.iolo.berry.svc;
 
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props

import akka.util.duration._
import akka.util.Timeout

import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import akka.testkit.EventFilter

import akka.pattern._

import akka.dispatch.Await

import com.typesafe.config.ConfigFactory

import org.joda.time.DateTime

class berryServiceSpec(_system: ActorSystem) extends TestKit(_system)
    with ImplicitSender with FlatSpec with ShouldMatchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("berryServiceSpec"))

  "berry service" should "respond" in {
    val berryService = system.actorOf(Props[BerryService])

    implicit val timeout = Timeout(1 seconds)

    berryService ! AddEntry(Entry("1", "hello", DateTime.now))

    berryService ! AddEntry(Entry("2", "world", DateTime.now))

    println(Await.result(berryService ? FindEntry("1"), timeout.duration))

    println(Await.result(berryService ? FindEntry("2"), timeout.duration))

    println(Await.result(berryService ? ListEntries(), timeout.duration))

    berryService ! RemoveEntry("1")

    println(Await.result(berryService ? ListEntries(), timeout.duration))

    berryService ! RemoveEntry("2")

    println(Await.result(berryService ? ListEntries(), timeout.duration))
  }
}
