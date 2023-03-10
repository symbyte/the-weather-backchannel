package symbyte.theweatherbackchannel

import cats.effect.IO
import cats.syntax.either._
import cats.syntax.applicative._
import munit.CatsEffectSuite
import symbyte.theweatherbackchannel.http.clients.nws.NWSClient
import cats.data.EitherT
import symbyte.theweatherbackchannel.domain.models.{Failure, LatLong}
import symbyte.theweatherbackchannel.http.clients.nws.models.ForecastResponse
import org.http4s.{Method, Request, Status}
import org.http4s.implicits._
import symbyte.theweatherbackchannel.domain.services.NWSBackedWeatherService
import symbyte.theweatherbackchannel.http.Routes
import symbyte.theweatherbackchannel.http.clients.nws.models.ForecastProperties
import symbyte.theweatherbackchannel.http.clients.nws.models.Period
import symbyte.theweatherbackchannel.domain.models.ClientFailure

class ForecastSpec extends CatsEffectSuite {
  def happyClient(
      shortForecast: String,
      temperature: Int,
      temperatureUnit: String
  ): NWSClient[IO] =
    new NWSClient[IO] {
      def getForcastEndpoint(latLong: LatLong): EitherT[IO, Failure, String] =
        EitherT("some-route".asRight[Failure].pure[IO])

      def getForecast(uri: String): EitherT[IO, Failure, ForecastResponse] =
        EitherT(
          ForecastResponse(
            ForecastProperties(
              List(
                Period(
                  shortForecast,
                  temperature,
                  temperatureUnit
                )
              )
            )
          ).asRight[Failure].pure[IO]
        )
    }

  def sadClient(f: Failure): NWSClient[IO] =
    new NWSClient[IO] {
      def getForcastEndpoint(latLong: LatLong): EitherT[IO, Failure, String] =
        EitherT("some-route".asRight[Failure].pure[IO])

      def getForecast(uri: String): EitherT[IO, Failure, ForecastResponse] =
        EitherT(
          f.asLeft[ForecastResponse].pure[IO]
        )
    }

  val happyRequest = Request[IO](Method.GET, uri"/forecast/today/99.99,-99.99")
  val sadRequest = Request[IO](Method.GET, uri"/forecast/today/blahblah")

  val happyRoutes = makeRoutes(happyClient("Sunny", 70, "F")) _
  val sadRoutes = makeRoutes(
    sadClient(ClientFailure(new Throwable("oh no!")))
  ) _

  test("happy path - retuns 200") {
    assertIO(
      happyRoutes(happyRequest)
        .map(_.status),
      Status.Ok
    )
  }

  test("happy path - retuns forecast") {
    assertIO(
      happyRoutes(happyRequest)
        .flatMap(_.as[String]),
      """{"conditions":"Sunny","temperature":"moderate"}"""
    )
  }

  test("sad path - malformed lat long returns 404") {
    assertIO(
      happyRoutes(sadRequest)
        .map(_.status),
      // since the lat long is a path param, this is technically correct
      // otherwise a 400 would make more sense
      Status.NotFound
    )
  }

  test("sad path - return 500 if client request fails") {
    assertIO(
      sadRoutes(happyRequest)
        .map(_.status),
      Status.InternalServerError
    )
  }

  def makeRoutes(c: NWSClient[IO])(r: Request[IO]) = {
    val service = new NWSBackedWeatherService(c)
    Routes.forecastRoutes(service).orNotFound(r)
  }

}
