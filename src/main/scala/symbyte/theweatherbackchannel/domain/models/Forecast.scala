package symbyte.theweatherbackchannel.domain.models

import cats.syntax.option._
import enumeratum._

sealed trait TempFeels extends EnumEntry.Lowercase
object TempFeels extends Enum[TempFeels] with CirceEnum[TempFeels] {
  val values = findValues
  case object Hot extends TempFeels
  case object Warm extends TempFeels
  case object Cool extends TempFeels
  case object Cold extends TempFeels

  // doing a simpler pure integer conversion here, no need to be
  // as precise due to the mapping done below
  private def celsiusToFahrenheit(t: Int): Int = (t * 2) + 32

  def fromTemperature(temp: Int, unit: String): Option[TempFeels] = {
    val tempInFahrenheit: Option[Int] = unit.toLowerCase match {
      case "f" => temp.some
      case "c" => celsiusToFahrenheit(temp).some
      case _   => none
    }

    tempInFahrenheit map {
      case t if t > 89 => TempFeels.Hot
      case t if t > 68 => TempFeels.Warm
      case t if t > 40 => TempFeels.Cool
      case _           => TempFeels.Cold
    }
  }
}

case class Forecast(conditions: String, temperature: TempFeels)
