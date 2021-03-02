# ShareMyCar
## Description du projet
Cette application a pour but de mettre en relation les autostoppeurs avec les conducteurs disponibles qui font le même trajet. 
L'idée première de l'application de mettre en relation des personnes qui font des trajets récurrents avec des personnes faisant un trajet similaire.
Par exemple, tous les jours nous faisons le trajet maison/travail et passons devant des personnes, qui certes n'ont pas le même point de départ ni d'arrivé mais qui peuvent avoir des points d'arrivé assez proche.

Notre application permet donc d'aider les personnes n'ayant pas de voiture à faire leur trajet récurrent plus facilement. Mais aussi à diminuer la polution en favorisant le covoiturage, en effet si 2 personnes faisant un trajet similaire tous les jours, même court, alors ils pourront faire ce trajet ensemble tous les jours.


## Architecture du projet

### Composants du système 

Notre projet est divisé en 3 parties :

* L'application mobile : Elle est développé en Android natif avec Kotlin. C'est grâce à elle que les utilisateurs peuvent interragir avec le système.
* Le serveur REST : Il est developpé en NodeJS avec la libraire express. Il sert à conserver les données des utilisateurs et aussi les requêtes de trajet faite par les utilisateurs.
* Le système de matching : Il est également développer avec NodeJS. Il est responsable de trouver les requête qui sont compatible et de vérifier si les deux utilisateurs acceptent leur mise en relation.

([Description détaillé des composants du système côté serveur](https://docs.google.com/document/d/1xlsPp1bcTXnB1CCAtbtuMFrXB9PuXW7gw-bDo9m_Ses/edit?usp=sharing))

<br/> <br/>

### Schèma d'architecture
 
![Architecture-share-my-car](https://user-images.githubusercontent.com/66921801/109696440-59ac4780-7b8d-11eb-912e-36edc4abbdf9.jpg)


La description détaillé de l'architecture est disponible sur drive ([description de l'architecture](https://docs.google.com/document/d/1aptt75FcNgb30m615O3gp2lGy6nLYolsbvnyeDFJg9I/edit?usp=sharing))

## Documentation 
Vous retrouverez toute la documation dans ce dossier : 
https://drive.google.com/drive/folders/1i3-OPlh671Cuol1BuC9QGARCj2TkAEOE?usp=sharing

## Étudiants

Hoareau Grégory et Vouillon Benjamin
