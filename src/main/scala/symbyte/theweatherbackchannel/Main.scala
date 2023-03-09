package symbyte.theweatherbackchannel

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run = TheweatherbackchannelServer.run[IO]
}
