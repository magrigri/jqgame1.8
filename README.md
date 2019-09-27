# jqgame1.8

JQgames1.8 est un plugin spigot regroupant des mini-jeux jouables dans un inventaire, avec un ou plusieurs joueurs. Il propose également des statistiques (victoires, défaites).


Tested with spigot version: 1.8.8
Lang: FR
Dependencies: vault

# Modes de jeux

## Machines à sous

Vous jouez seul.
Misez une somme, vous aurez une chance sûr 9 de gagner win1Multiple (voir config) fois vôtre mise, une chance de gagner win2Multiple (voir config) fois votre mise et une chance de ne rien gagner ni rien perdre. 

![alt img](https://snipboard.io/q2JCuA.jpg)

## Plus ou moins

Vous jouez à deux.

# Config
```yaml
# Nombres des inventaires
inventoryNames:
  choseAGame: "Choix un jeu"
  pomQueue: "Liste d'attente Plus Ou Moins"
  pomPlaying: "Partie en cours"
  macPlay: "choix de la mise"
  macStartPlay: "Choisissez une carte"
  stats: "Statistiques "

# préfix lorsque le plugin envoie des messages aux joueurs
prefix: "&c[Games] &a "

# Somme minimal et maximal que l'on peut parier au jeu "Plus ou moins"
Pom:
  minBet: 1
  maxBet: 100

# Facteur de multiplication des gains pour la machine à sous en cas de victoire.
Mac:
  win1Multiple: 2
  win2Multiple: 9

# Stockage des victoires et défaîtes.
Storage:
  Mac:
    uuid:
      lose: 0
      equality: 0
      win1: 0
      win2: 0
      totalMoneyWin: 0
  Pom:
    uuid:
      win: 0
      lose: 0
      totalMoneyWin: 0
```
