package symbyte.theweatherbackchannel.http.clients.nws.models

import cats.effect.Concurrent
import io.circe.Decoder
import io.circe.generic.semiauto._
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class Period(
    shortForecast: String,
    temperature: Int,
    temperatureUnit: String
)
case class ForecastProperties(periods: List[Period])
case class ForecastResponse(properties: ForecastProperties)

object ForecastResponse {
  implicit val periodDecoder: Decoder[Period] = deriveDecoder
  implicit val propertiesDecoder: Decoder[ForecastProperties] = deriveDecoder
  implicit val forecastResponseDecoder: Decoder[ForecastResponse] =
    deriveDecoder
  implicit def forecastResponseEntityDecoder[F[_]: Concurrent]
      : EntityDecoder[F, ForecastResponse] = jsonOf
}
