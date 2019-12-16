# Kata Comédies

Il s'agit d'un projet sbt constitué d'une simple application en ligne de commande :
- Compiler : `sbt compile`
- Exécuter : `sbt "run <args>"` (cf. [configuration](#configuration))
- Tester : `sbt test`

# Configuration
La configuration de l'application s'effectue par les paramètres passés dans la commande d'exécution.
L'aide est disponible avec `sbt "run --help"`:
```
Usage: [--file <titles file>] [--rabbimq-uri <rabbitmq uri>] [--queue <queue name>] [--verbose]
default values: file=title.basics.tsv | rabbimq-uri=amqp://guest:guest@localhost:5672/%2F | queue=comedy_movies
(titles file will be downloaded if missing)
(verbose means printing the titles sent to RabbitMQ)
```
La configuration du server RabbitMQ est laissée à l'appelant. 

Par manque de temps et de connaisances (découverte de Akka-Stream et de RabbitMQ), la connection au serveur RabbitMQ est gérée au minimum (pas de reconnexion automatique, etc.); j'aurais peut-être dû me diriger vers une bibliothèque comme *akka-rabbitmq*.

P.S. J'ai constaté en retéléchargeant le fichier que des doublons sont *apparus* (ils n'étaient pas présent dans la version que j'avais téléchargée pour dévélopper), et je n'ai pas géré ce cas de figure.
