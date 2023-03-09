package symbyte.theweatherbackchannel.http.clients

import symbyte.theweatherbackchannel.domain.models.LatLong
import java.net.URI
import annotation.unused
import org.http4s.client.Client

trait ForecastResponse

trait NWSClient[F[_]] {
  def getForcastEndpoint(latLong: LatLong): F[URI]
  def getForecast(uri: URI): F[ForecastResponse]
}

class NWSClientImpl[F[_]](@unused client: Client[F]) extends NWSClient[F] {
  def getForcastEndpoint(latLong: LatLong): F[URI] = ???
  def getForecast(uri: URI): F[ForecastResponse] = ???
}
