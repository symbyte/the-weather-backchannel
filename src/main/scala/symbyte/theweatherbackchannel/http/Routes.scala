package symbyte.theweatherbackchannel.http

import cats.effect.Sync
import symbyte.theweatherbackchannel.domain.services.WeatherService
import org.http4s.HttpRoutes

object Routes {
  def forecastRoutes[F[_]: Sync](
      weatherService: WeatherService[F]
  ): HttpRoutes[F] = ???
}
