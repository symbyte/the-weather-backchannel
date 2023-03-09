package symbyte.theweatherbackchannel.domain.services

import symbyte.theweatherbackchannel.domain.models.Forecast
import symbyte.theweatherbackchannel.domain.models.LatLong
import symbyte.theweatherbackchannel.http.clients.NWSClient
import annotation.unused

trait WeatherService[F[_]] {
  def todaysForcast(coords: LatLong): F[Forecast]
}

class NWSBackedWeatherService[F[_]](@unused client: NWSClient[F])
    extends WeatherService[F] {
  def todaysForcast(coords: LatLong): F[Forecast] = ???
}
