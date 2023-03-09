package symbyte.theweatherbackchannel.domain.models

import enumeratum._

sealed trait TempFeels extends EnumEntry.Lowercase
object TempFeels extends Enum[TempFeels] with CirceEnum[TempFeels] {
  val values = findValues
  case object Hot extends TempFeels
  case object Warm extends TempFeels
  case object Cool extends TempFeels
  case object Cold extends TempFeels
}

case class Forecast(conditions: String, temperature: TempFeels)
