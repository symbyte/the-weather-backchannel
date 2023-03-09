package symbyte.theweatherbackchannel.http

import cats.effect.Sync
import cats.syntax.flatMap._
import symbyte.theweatherbackchannel.domain.services.WeatherService
import symbyte.theweatherbackchannel.http.codecs.params.LatLongVar
import symbyte.theweatherbackchannel.http.codecs.responses.ForecastCodec
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object Routes {
  def forecastRoutes[F[_]: Sync](
      weatherService: WeatherService[F]
  ): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    import ForecastCodec._
    HttpRoutes.of[F] {
      case GET -> Root / "forecast" / "today" / LatLongVar(latLong) =>
        weatherService.todaysForcast(latLong).flatMap(Ok(_))
    }
  }
}
