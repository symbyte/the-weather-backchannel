package symbyte.theweatherbackchannel.http

import cats.effect.Async
import com.comcast.ip4s._
import org.http4s.ember.client.EmberClientBuilder
import symbyte.theweatherbackchannel.domain.services.WeatherService
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger
import symbyte.theweatherbackchannel.domain.services.NWSBackedWeatherService
import symbyte.theweatherbackchannel.http.clients.nws.NWSClientImpl

object Server {
  def run[F[_]: Async]: F[Nothing] = {
    for {
      httpClient <- EmberClientBuilder.default[F].build
      nwsClient = new NWSClientImpl(httpClient)
      weatherService: WeatherService[F] = new NWSBackedWeatherService(nwsClient)
      httpApp = Routes.forecastRoutes(weatherService).orNotFound

      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <- EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(finalHttpApp)
        .build
    } yield ()
  }.useForever
}
