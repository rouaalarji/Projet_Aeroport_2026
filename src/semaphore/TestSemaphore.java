package semaphore;

import common.Logger;

/**
 * Programme de test pour la version SEMAPHORE
 */
public class TestSemaphore {
    
    public static void main(String[] args) {
        
        Logger.titre("TEST VERSION SEMAPHORE");
        System.out.println();
        
        // Créer l'aéroport
        AeroportSemaphore aeroport = new AeroportSemaphore();
        System.out.println();
        
        Logger.info("TEST 1 : 2 arrivées en même temps (pas de conflit)");
        Logger.separateur();
        
        AvionSemaphore avion1 = new AvionSemaphore("AF123", true, aeroport);
        AvionSemaphore avion2 = new AvionSemaphore("LH456", true, aeroport);
        
        avion1.start(); 
        avion2.start();
        
        // Attendre que les 2 avions terminent
        try {
            avion1.join(); 
            avion2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println();
        aeroport.afficherEtat();
        System.out.println();
        
        Logger.info("TEST 2 : 5 arrivées en même temps (conflit attendu)");
        Logger.separateur();
        
        AvionSemaphore[] avions = new AvionSemaphore[5];
        
        avions[0] = new AvionSemaphore("BA789", true, aeroport);
        avions[1] = new AvionSemaphore("EK999", true, aeroport);
        avions[2] = new AvionSemaphore("QR111", true, aeroport);
        avions[3] = new AvionSemaphore("TK222", true, aeroport);
        avions[4] = new AvionSemaphore("SQ333", true, aeroport);
        
        // Lancer tous les avions
        for (AvionSemaphore avion : avions) {
            avion.start();
        }
        
        // Attendre que tous terminent
        try {
            for (AvionSemaphore avion : avions) {
                avion.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println();
        aeroport.afficherEtat();
        System.out.println();
        
        // Arrivées + Départs 
        Logger.info("TEST 3 : 3 arrivées + 2 départs (test de priorité)");
        Logger.separateur();
        
        AvionSemaphore arr1 = new AvionSemaphore("AF444", true, aeroport);
        AvionSemaphore arr2 = new AvionSemaphore("LH555", true, aeroport);
        AvionSemaphore arr3 = new AvionSemaphore("BA666", true, aeroport);
        AvionSemaphore dep1 = new AvionSemaphore("EK777", false, aeroport);
        AvionSemaphore dep2 = new AvionSemaphore("QR888", false, aeroport);
        
        // Lancer tous en même temps
        arr1.start();
        dep1.start();
        arr2.start();
        dep2.start();
        arr3.start();
        
        try {
            arr1.join();
            arr2.join();
            arr3.join();
            dep1.join();
            dep2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println();
        aeroport.afficherEtat();
        System.out.println();
        
        Logger.titre("TOUS LES TESTS SONT TERMINÉS !");
    }
}
