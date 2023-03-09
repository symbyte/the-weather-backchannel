package symbyte.theweatherbackchannel.domain.services

import annotation.unused
import symbyte.theweatherbackchannel.domain.models.Failure
import symbyte.theweatherbackchannel.domain.models.Forecast
import symbyte.theweatherbackchannel.domain.models.LatLong
import symbyte.theweatherbackchannel.http.clients.NWSClient

trait WeatherService[F[_]] {
  def todaysForcast(coords: LatLong): F[Either[Failure, Forecast]]
}

class NWSBackedWeatherService[F[_]](@unused client: NWSClient[F])
    extends WeatherService[F] {
  def todaysForcast(coords: LatLong): F[Either[Failure, Forecast]] = ???
}
