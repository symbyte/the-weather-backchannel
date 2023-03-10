#Running the service

This project was cloned from the http4s template available via sbt:
```
sbt new http4s/http4s.g8
```

Simply execute:
```
sbt run
```

in the root of the repo. The service will begin listing on localhost:8080. You should then be able to hit it with a request at on its lone exposed route:
```
❯ curl localhost:8080/forecast/today/35.6531,-97.4815
{"conditions":"Mostly Cloudy","temperature":"cold"}
```

And now I shall list my sins:
* I nicked the format for the lat long directly from the representation used in the National Weather Service's API. I don't love it but I didn't hate it enough to do something different ¯\\\_(ツ)\_/¯
* To say that the error handling isn't rich is bieng generious. I only put in just enough effort here to lay the groundwork for decent error handling, but didn't want to spend too much more time here. I also wouldn't ever directly put a throwables message as something a user would see in a production app. I did this mostly for my debugging convenience when bouncing off the client and didn't feel the need to undo it.
* I typically reserve monad transformers for when I know I'll need to be composing several monad stacks together, but upon eschewing them initially I decided to pull them in to avoid some of the nesting that can sometimes harm readability (particularly in the Routes code).
* I took some shortcuts with the modeling (particularly on the temperature information as recieved from the client). I normally wouldn't rely on simple strings to denote a unit-ed type like this and would have reached for something a bit more rich if I wanted to spend more time.
* I did use the tagless final pattern here which at first blush felt like Enterprise FizzBuzz levels of tomfoolery, but I did git it pretty much for free with the http4s project template mentioned above and I like/am comfortable with the pattern so I just went with it.
* Testing is on the lighter side, just doing a couple of happy/sad path examples. Would have fleshed out the error handling a lot more (as previously mentioned) and would have exersized all of the happy/sad paths were this a production app. 