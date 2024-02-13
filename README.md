# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

Phase 2 Sequence Diagram outlook

[![Sequence Diagram]] (https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHusFJV6EFWkGh1VMKbN+etxygQjLXUcTkSxv2UFq7q7dUzyJ9vlyrjz5zU6jAAGJWeIAWWNzu1B56erZHLPpNNmfDfvtMU-xffdoXdZt0S9P9fUjZcYxHKJoOzKNuh6fx4yTVN0yQv12kwPMC10AxjB0FA7UrLR9GYWtvF8TB0KbXpWz4E8kjeNJ0i7CQezyHDYJbUdenYKJJ0olVRj4tAl09VRMVfB4ohkFAEBOFBlVVCSLRgqSQOxN1niPFivjYsMYAAdQACUNN4w20mAAF5bOQtcwI9SCERgCQ9FlAMMTcmgoCiLyfJHGMGITGAUzTAoyikYBjiiAAiCjjhkGAqM87zXUBZEEHMRLczQfNtCI4tBjSj5hhgABxHlHho+t6MbZh-Nbaq3g7dItB5XitOQ0K5NAnEAqQQFzHU-90E0rM-T3PT4IEqJ7Am+NCAJfNgAQZhlotVaQHWzAYCOpjBOXDcwGEFFati0Z1mOoTxAZFycUGRxUBoS6EGuyRbsO47nv0xlXhPH4YG6spmT4SoATvR8wbqv6joBhb3KOOrKocRGHtk1rAvh2KMawe6BrQ5rMOTGB4gAaXTcHJEJ3MEBUGA0AgC7dowNbfHzYmBLCsmYAAFmTABGGBTlMC1oCQAAvFB8wIgiSqLYxsD0KBsBU+B8V8GqeQ8WiGwCFqIICqI22SDi6d6mb0FpnlWR5Yc+cGvSohGsaJu06bJp05HwJOpaJBWzn9u5zBNu24OOYYA7ea6BkztxHWUG+n3tMqOnHbKO7jrg13ZSWtBsjANPJMzh3NSx-2DJZA0jRFMU6cedVIehzUYAANWED5TjeMgYFbqH8ZQbPd2rl1XNN4TMp8+PAwDhDZ9dLGSdCcLMLFuAU4VpnVFZ9nVT2uO85d0njaiYWxYlv9pblhWivwx-CJVoxzGUyd3BgAApCBpz1soxgFAIFANaJqxtE6LRiLEU4nVraS1tr2Ao4U4AQEnFACuEM+DOwTgXdcMAPbjT6n6dO2Y5o6kXlAnaR9Q4HUjjAahnhj7h3ntGfyM8ABWf80Bl2IegTBKA26xmNqg9Bucjr52lJPHExwzjZEhqQ2aE95L0nfEed4XxQYTEhree8T4-zmSslYGykkHJOT9Mooabp2Fem0XwVeMknrSLlLiJhIBrTfTeAAD2GBIUYdNIbiJgDXIGx5NG-ACXwXRcNm6GOsiPHRjlImWPmpQ1GI9vG+KxvQummSHDSHuvdCA2BmAoLQdALGhTT7uScSolxwAZAyFiCA9+UArRpzsQIoRZSxGVKqcE5xh465fkAlUFplAW6dIScPEA5SoCrC7j3PuA8h5dOHj06ACz4ismCacWIHxmRwGECZKmbwACa4tYh8GOTZWZ6CzEbKgH0qp-sbEeU1sAVpwhGny2eUsPefz55SgGXUxCjTmmfMoO0nkqzpmVAAOQQAUNkAK8Kgn9JBVYoZ+oRmNzGZCqAkz5HD0iZUO5mzFm937oPKZpKYCIuRQJNFlMdl8D2Qco5JzzmXOuSZclsBHIMpRZQeFgL-qDLSfCKIHyvk-J5v0giGL8642leMqA3ylLyqqWvYRGFIqpkplTLGylmYHzyWAAp-SdUbyFsmQWMBWRs1vPoR+91FZ7xZmzEOscWHVICvzC+trr4IM8HfX5itn6FmIkYHQnzYyKlgMAbAmsuY9gNo1cKkCujmyMh1diGR1AoSzVK3EKk8DCGTcgMOPZbi-UkZit2paE23CUCoX6IS9RJCsKcVknKbJJGEAAIS+MaaQHajxdp7X2mAA7h02WbrpChtcZ3dt7TcmdQ6R3AWVabUMwUV7WoFlFQ1T98xAA)

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
