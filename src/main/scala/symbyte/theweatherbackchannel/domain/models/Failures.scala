package symbyte.theweatherbackchannel.domain.models

sealed abstract class Failure(val msg: String)

case class ClientFailure(t: Throwable)
    extends Failure(
      s"There was a problem contacting the weather service: ${t.getMessage}"
    )
case object WeatherServiceFailure
    extends Failure(
      "There was a problem fulfilling your request. We're working on it!"
    )
