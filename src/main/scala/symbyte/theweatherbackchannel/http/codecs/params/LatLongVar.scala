package symbyte.theweatherbackchannel.http.codecs.params

import scala.util.Try
import symbyte.theweatherbackchannel.domain.models.LatLong

object LatLongVar {
  def unapply(str: String): Option[LatLong] = {
    if (!str.isEmpty())
      Try {
        val lat :: long :: Nil = str.split(",").map(_.toFloat).toList
        LatLong(lat, long)
      }.toOption
    else
      None
  }
}
