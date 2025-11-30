package semaphore;

import common.Avion;
import common.EtatAvion;
import common.Logger;


 // Avion qui utilise l'aéroport avec le mécanisme SEMAPHORE

public class AvionSemaphore extends Avion {
    
    private final AeroportSemaphore aeroport;
    
    
    public AvionSemaphore(String identifiant, boolean estArrivee, AeroportSemaphore aeroport) {
        super(identifiant, estArrivee); // Appel du constructeur parent
        this.aeroport = aeroport;
    }
    
  
    @Override
    public void run() {
        try {
            if (estArrivee()) {
                // Cycle de vie d'un avion en ARRIVÉE
                cycleArrivee();
            } else {
                // Cycle de vie d'un avion en DÉPART
                cycleDepart();
            }
        } catch (Exception e) {
            Logger.error(getIdentifiant() + " - Erreur : " + e.getMessage());
        }
    }
    
    
     //Cycle de vie d'un avion en ARRIVÉE
    
    private void cycleArrivee() {
        Logger.info("avion " + getIdentifiant() + " (ARRIVÉE) commence son cycle");
        
        // ATTERRISSAGE
        setEtat(EtatAvion.EN_ATTENTE_ATTERRISSAGE);
        aeroport.demanderPisteArrivee(getIdentifiant());
        
        atterrir(); 
        
        aeroport.libererPiste(getIdentifiant(), "atterrissage");
        
        //  STATIONNEMENT À LA PORTE
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
    
 
     // Cycle de vie d'un avion en DÉPART
  
    private void cycleDepart() {
        Logger.info("avion " + getIdentifiant() + " (DÉPART) commence son cycle");
        
        //  DÉCOLLAGE DIRECT
        setEtat(EtatAvion.EN_ATTENTE_DECOLLAGE);
        aeroport.demanderPisteDepart(getIdentifiant());
        
        decoller(); 
        
        aeroport.libererPiste(getIdentifiant(), "décollage");
        
       
        setEtat(EtatAvion.TERMINE);
        Logger.success("✅ " + getIdentifiant() + " a terminé son cycle !");
    }
}