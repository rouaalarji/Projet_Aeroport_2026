package reentrantlock;

import common.Logger;


public class TestReentrantLock {
    
    public static void main(String[] args) {
        
        Logger.titre("TEST VERSION REENTRANTLOCK");
        System.out.println();
        
        // Créer l'aéroport
        AeroportReentrantLock aeroport = new AeroportReentrantLock();
        System.out.println();
        
        Logger.info("TEST 1 : 2 arrivées en même temps (pas de conflit)");
        Logger.separateur();
        
        AvionReentrantLock avion1 = new AvionReentrantLock("AF123", true, aeroport);
        AvionReentrantLock avion2 = new AvionReentrantLock("LH456", true, aeroport);
        
        avion1.start(); 
        avion2.start();
        
        try {
            avion1.join(); 
            avion2.join(); // Attend que avion2 termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println();
        aeroport.afficherEtat();
        System.out.println();
        
        Logger.info("TEST 2 : 5 arrivées en même temps (conflit attendu)");
        Logger.separateur();
        
        AvionReentrantLock[] avions = new AvionReentrantLock[5];
        
        // Créer 5 avions
        avions[0] = new AvionReentrantLock("BA789", true, aeroport);
        avions[1] = new AvionReentrantLock("EK999", true, aeroport);
        avions[2] = new AvionReentrantLock("QR111", true, aeroport);
        avions[3] = new AvionReentrantLock("TK222", true, aeroport);
        avions[4] = new AvionReentrantLock("SQ333", true, aeroport);
        
        // Lancer tous les avions
        for (AvionReentrantLock avion : avions) {
            avion.start();
        }
        
        // Attendre que tous terminent
        try {
            for (AvionReentrantLock avion : avions) {
                avion.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println();
        aeroport.afficherEtat();
        System.out.println();
        
        Logger.info("TEST 3 : 3 arrivées + 2 départs (test de priorité)");
        Logger.separateur();
        
        AvionReentrantLock arr1 = new AvionReentrantLock("AF444", true, aeroport);
        AvionReentrantLock arr2 = new AvionReentrantLock("LH555", true, aeroport);
        AvionReentrantLock arr3 = new AvionReentrantLock("BA666", true, aeroport);
        AvionReentrantLock dep1 = new AvionReentrantLock("EK777", false, aeroport);
        AvionReentrantLock dep2 = new AvionReentrantLock("QR888", false, aeroport);
        
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
