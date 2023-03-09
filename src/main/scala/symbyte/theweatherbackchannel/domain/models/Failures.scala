package symbyte.theweatherbackchannel.domain.models

sealed trait Failure

case object EndpointFailure extends Failure
case object ClientFailure extends Failure
case object WeatherServiceFailure extends Failure
