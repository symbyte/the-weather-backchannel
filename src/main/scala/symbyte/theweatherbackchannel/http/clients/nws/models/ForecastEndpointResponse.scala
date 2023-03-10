package symbyte.theweatherbackchannel.http.clients.nws.models

import cats.effect.Concurrent
import io.circe.Decoder
import io.circe.generic.semiauto._
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class Properties(forecast: String)
case class ForecastEndpointResponse(properties: Properties)
object ForecastEndpointResponse {
  implicit val propertiesDecoder: Decoder[Properties] = deriveDecoder
  implicit val forecastEndpointResponseDecoder
      : Decoder[ForecastEndpointResponse] = deriveDecoder
  implicit def forecastEndpointResponseEntityDecoder[F[_]: Concurrent]
      : EntityDecoder[F, ForecastEndpointResponse] = jsonOf
}
