package symbyte.theweatherbackchannel.http.clients.nws

import cats.data.EitherT
import cats.effect.Concurrent
import cats.syntax.applicativeError._
import org.http4s.client.Client
import symbyte.theweatherbackchannel.domain.models.ClientFailure
import symbyte.theweatherbackchannel.domain.models.Failure
import symbyte.theweatherbackchannel.domain.models.LatLong
import symbyte.theweatherbackchannel.http.clients.nws.models.ForecastEndpointResponse
import symbyte.theweatherbackchannel.http.clients.nws.models.ForecastResponse

trait NWSClient[F[_]] {
  def getForcastEndpoint(latLong: LatLong): EitherT[F, Failure, String]
  def getForecast(uri: String): EitherT[F, Failure, ForecastResponse]
}

class NWSClientImpl[F[_]: Concurrent](client: Client[F]) extends NWSClient[F] {
  // LatLong needs to truncate to 4th decimal place to comply with the NWS API
  private def truncate(n: Float): String = "%.4f".format(n)
  private def toPathParams(latLong: LatLong): String =
    s"${truncate(latLong.lat)},${truncate(latLong.long)}"

  private def forecastEndpointRequest(latLong: LatLong): String =
    s"https://api.weather.gov/points/${toPathParams(latLong)}"

  def getForcastEndpoint(latLong: LatLong): EitherT[F, Failure, String] =
    EitherT(
      client
        .expect[ForecastEndpointResponse](forecastEndpointRequest(latLong))
        .attempt
    ).leftMap[Failure](ClientFailure(_))
      .map(_.properties.forecast)

  def getForecast(uri: String): EitherT[F, Failure, ForecastResponse] =
    EitherT(
      client
        .expect[ForecastResponse](uri)
        .attempt
    ).leftMap[Failure](ClientFailure(_))
}
