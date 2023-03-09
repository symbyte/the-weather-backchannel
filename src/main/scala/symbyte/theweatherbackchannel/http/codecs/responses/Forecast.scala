package symbyte.theweatherbackchannel.http.codecs.responses

import io.circe.Encoder
import io.circe.generic.semiauto._
import symbyte.theweatherbackchannel.domain.models.Forecast
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

object ForecastCodec {
  implicit val forecastEncoder: Encoder[Forecast] = deriveEncoder
  implicit def forcecastEntityEncoder[F[_]]: EntityEncoder[F, Forecast] =
    jsonEncoderOf
}
