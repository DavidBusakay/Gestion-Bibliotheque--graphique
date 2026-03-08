# Gestion Bibliothèque (Graphique)

## Description
Ce projet est une application Java graphique de gestion de bibliothèque, permettant d'ajouter des documents, des utilisateurs, et de gérer les emprunts via une interface utilisateur.

## Structure du projet
- `Main.java` : Point d'entrée de l'application.
- `gui/` : Interface graphique (boîtes de dialogue, panneaux).
- `models/` : Modèles métier (documents, utilisateurs).

## Compilation
Pour compiler tous les fichiers Java :

1. Ouvre un terminal dans le dossier racine du projet.
2. Lance la commande suivante :

```bash
javac -d bin -cp . Main.java gui/dialogsBox/*.java gui/panels/*.java models/documents/*.java models/utilisateurs/*.java
```

- Cette commande compile tous les fichiers Java et place les fichiers `.class` dans le dossier `bin`.

## Exécution
Pour lancer l'application :

```
java -cp bin Main
```

## Dépannage
- Si une erreur "cannot be resolved to a type" apparaît, assurez-vous que tous les fichiers sont bien compilés et présents dans le dossier `bin`.
- Vérifiez que le classpath utilisé lors de l'exécution inclut le dossier `bin`.

## Auteur
[David Busakay](https://davidbusakay.vercel.app/)
