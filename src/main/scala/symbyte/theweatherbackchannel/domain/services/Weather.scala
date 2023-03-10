package symbyte.theweatherbackchannel.domain.services

import cats.data.EitherT
import cats.effect.Sync
import cats.syntax.applicative._
import symbyte.theweatherbackchannel.domain.models.Failure
import symbyte.theweatherbackchannel.domain.models.Forecast
import symbyte.theweatherbackchannel.domain.models.LatLong
import symbyte.theweatherbackchannel.domain.models.TempFeels
import symbyte.theweatherbackchannel.domain.models.WeatherServiceFailure
import symbyte.theweatherbackchannel.http.clients.nws.NWSClient
import symbyte.theweatherbackchannel.http.clients.nws.models.ForecastResponse

trait WeatherService[F[_]] {
  def todaysForcast(coords: LatLong): F[Either[Failure, Forecast]]
}

class NWSBackedWeatherService[F[_]: Sync](client: NWSClient[F])
    extends WeatherService[F] {
  private def toDomain(fr: ForecastResponse): Either[Failure, Forecast] =
    (for {
      conditions <- fr.properties.periods.headOption.map(_.shortForecast)
      tempFeels <- fr.properties.periods.headOption.flatMap(p =>
        TempFeels.fromTemperature(p.temperature, p.temperatureUnit)
      )
    } yield Forecast(conditions, tempFeels)).toRight(WeatherServiceFailure)

  def todaysForcast(coords: LatLong): F[Either[Failure, Forecast]] = (for {
    forecastEndpoint <- client.getForcastEndpoint(coords)
    clientForecast <- client.getForecast(forecastEndpoint)
    forecast <- EitherT(toDomain(clientForecast).pure[F])
  } yield forecast).value
}
