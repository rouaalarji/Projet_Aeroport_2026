package common;


public class TestClassesBase {
    
    public static void main(String[] args) {
        
        Logger.titre("TEST DES CLASSES DE BASE");
        System.out.println();
        
        //  Créer des avions =
        Logger.info("Test 1 : Création d'avions");
        
        Avion avion1 = new Avion("AF123", true);  
        Avion avion2 = new Avion("LH456", false); 
        Avion avion3 = new Avion("BA789", true); 
        
        System.out.println("   " + avion1);
        System.out.println("   " + avion2);
        System.out.println("   " + avion3);
        System.out.println();
        
        //  Changer l'état d'un avion
        Logger.info("Test 2 : Changement d'états");
        
        avion1.setEtat(EtatAvion.ATTERRISSAGE);
        avion1.setEtat(EtatAvion.EN_ATTENTE_PORTE);
        avion1.setEtat(EtatAvion.STATIONNEMENT);
        System.out.println();
        
        // Simuler des actions 
        Logger.info("Test 3 : Simulation d'actions (attendez 7 secondes)");
        
        long debut = System.currentTimeMillis();
        
        Logger.log("▶️  AF123 commence l'atterrissage...");
        avion1.atterrir();
        Logger.success("AF123 a atterri !");
        
        Logger.log("▶️  AF123 se dirige vers la porte...");
        avion1.stationner();
        Logger.success("AF123 a terminé l'embarquement !");
        
        Logger.log("▶️  AF123 commence le décollage...");
        avion1.decoller();
        Logger.success("AF123 a décollé !");
        
        long fin = System.currentTimeMillis();
        long duree = (fin - debut) / 1000;
        
        Logger.info("Durée totale : " + duree + " secondes");
        System.out.println();
        
      
        Logger.info("Test 4 : Différents types de messages");
        
        Logger.info("Message d'information");
        Logger.success("Message de succès");
        Logger.warning("Message d'avertissement");
        Logger.error("Message d'erreur");
        System.out.println();
        
        Logger.info("Test 5 : Liste de tous les états possibles");
        
        for (EtatAvion etat : EtatAvion.values()) {
            System.out.println("   " + etat);
        }
        System.out.println();
       
    }
}