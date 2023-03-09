package symbyte.theweatherbackchannel

import cats.effect.{IO, IOApp}
import symbyte.theweatherbackchannel.http.Server

object Main extends IOApp.Simple {
  val run = Server.run[IO]
}
