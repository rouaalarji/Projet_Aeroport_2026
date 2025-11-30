package monitor;

import common.Avion;
import common.EtatAvion;
import common.Logger;


 // Avion qui utilise l'aéroport avec le mécanisme MONITOR

 
public class AvionMonitor extends Avion {
    
    private final AeroportMonitor aeroport;
    
   
    public AvionMonitor(String identifiant, boolean estArrivee, AeroportMonitor aeroport) {
        super(identifiant, estArrivee); // Appel du constructeur parent
        this.aeroport = aeroport;
    }
   
    @Override
    public void run() {
        try {
            if (estArrivee()) {
                cycleArrivee();
            } else {
                cycleDepart();
            }
        } catch (Exception e) {
            Logger.error(getIdentifiant() + " - Erreur : " + e.getMessage());
        }
    }
    
    private void cycleArrivee() {
        Logger.info("avion" + getIdentifiant() + " (ARRIVÉE) commence son cycle");
        
        setEtat(EtatAvion.EN_ATTENTE_ATTERRISSAGE);
        aeroport.demanderPisteArrivee(getIdentifiant());
        
        atterrir(); // Simule 2 secondes
        
        aeroport.libererPiste(getIdentifiant(), "atterrissage");
        
        // 2️STATIONNEMENT À LA PORTE
        setEtat(EtatAvion.EN_ATTENTE_PORTE);
        aeroport.demanderPorte(getIdentifiant());
        
        stationner(); 
        
        aeroport.libererPorte(getIdentifiant());
        
        // DÉCOLLAGE
        setEtat(EtatAvion.EN_ATTENTE_DECOLLAGE);
        aeroport.demanderPisteDepart(getIdentifiant());
        
        decoller(); 
        
        aeroport.libererPiste(getIdentifiant(), "décollage");
        
        
        setEtat(EtatAvion.TERMINE);
        Logger.success("✅ " + getIdentifiant() + " a terminé son cycle !");
    }
    
    
    private void cycleDepart() {
        Logger.info("avion"+getIdentifiant() + " (DÉPART) commence son cycle");
        
        // 1️⃣ DÉCOLLAGE DIRECT
        setEtat(EtatAvion.EN_ATTENTE_DECOLLAGE);
        aeroport.demanderPisteDepart(getIdentifiant());
        
        decoller(); // Simule 2 secondes
        
        aeroport.libererPiste(getIdentifiant(), "décollage");
        
        
        setEtat(EtatAvion.TERMINE);
        Logger.success("avion " + getIdentifiant() + " a terminé son cycle !");
    }
}