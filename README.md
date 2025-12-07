# Projet_Aeroport_2026
Gestion concurrente d'un aéroport - Java
##Objectif du projet

L'objectif du projet est de concevoir et d'implémenter une simulation concurrente d'un aéroport afin d'étudier et comparer les comportements de différents mécanismes de synchronisation en Java. 

Le système doit :
- gérer un nombre limité de pistes (atterrissage/décollage) et de portes d'embarquement ;
- simuler des avions en arrivée (doivent obtenir une piste puis une porte), des avions en stationnement (occupent une porte) et des **avions en départ** (doivent obtenir une piste pour décoller) ;
- assurer qu'une piste/porte n'est utilisée que par un avion à la fois ;
- garantir que les arrivées ont la priorité sur les départs ;
- permettre la comparaison de deux mécanismes de synchronisation (parmi : Semaphore, Moniteur `synchronized`/`wait`/`notify`, ReentrantLock+Condition) en mesurant comportement, sécurité (absence de conditions de course / deadlocks) et performances (débit, latence) ;
- fournir une interface graphique commune affichant l'état des pistes/portes, les files d'attente et un journal d'événements, ainsi que des contrôles pour ajouter des avions et configurer les paramètres (nombre de pistes/portes, vitesse de simulation).

#Description du Projet

Ce projet simule le fonctionnement d’un aéroport avec des ressources limitées (pistes et portes d’embarquement). 
Plusieurs avions concourent pour accéder à ces ressources, ce qui crée des problèmes classiques de concurrence 
et de synchronisation. Le système gère les avions en arrivée, en stationnement et en départ tout en garantissant 
la priorité des arrivées sur les départs.

Le projet comporte trois implémentations :  
- Une version utilisant des sémaphores**  
- Une version utilisant des moniteurs (synchronized, wait, notify)** 
-une version utilisat Reentrantlock ( lock et unlock) 
Une interface graphique commune permet d’observer l’état des pistes, des portes et des files d’attente.

##Architecture de projet

Le dépôt est organisé comme suit :

Package Common:
-Avion.java
-EtatAvion.java
-Logger.java
Package gui:
-AreoportGui
Package monitor:
-AeroportMonitor
-AvionMonitor
Package semaphore:
-AeroportSemaphore.java
-AvionSemaphore.java
Package Reentrantlock:
-AeroportReentrantlock.java
-AvionReentrantlock.java

# Fonctionnalités

- Gestion concurrente des pistes d’atterrissage et de décollage
- Gestion des portes d’embarquement
- Priorité donnée aux avions en arrivée
- Files d’attente pour les avions (arrivées / départs)
- Logs des événements (atterrissages, stationnements, décollages)
- Ajout d’avions via l’interface graphique
- Choix du mécanisme de synchronisation : Semaphore ou Monitor

## Exécution

1. Ouvrir le projet dans  Eclipse.
3. Lancer la classe `AeroportGUI.java`.
4. L’interface graphique s’ouvre automatiquement.

## Comparaison des trois mécanismes de synchronisation
Ce projet implémente trois versions de la gestion concurrente d’un aéroport :
Sémaphores (Semaphore)
Moniteur Java (synchronized, wait/notify)
ReentrantLock + Condition
émaphore
##Comparaison des trois implémentations Sémaphore vs Moniteur vs ReentrantLock
-Sémaphore
Ressources représentées par des compteurs (acquire() / release()).
L’avion prend un jeton → utilise une piste ou une porte.
Fonctionne très bien pour limiter un nombre fixe de ressources.
Gestion des priorités = plus difficile (doit être codée manuellement).
-Moniteur (synchronized + wait/notify)
Verrou implicite sur l'objet → plus simple.
Le thread attend avec wait() et est réveillé avec notify() / notifyAll().
Priorité entre arrivées/départs facilement respectée.
- ReentrantLock + Condition
Version avancée d’un moniteur.
Verrou manuel lock()/unlock().
Conditions séparées → files d’attente distinctes.
Très bon contrôle, idéal pour les priorités complexes.

