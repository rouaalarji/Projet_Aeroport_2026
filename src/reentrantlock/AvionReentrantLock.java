package reentrantlock;

import common.Avion;
import common.EtatAvion;
import common.Logger;


 // Avion qui utilise l'aéroport avec le mécanisme REENTRANTLOCK
 
public class AvionReentrantLock extends Avion {
    
    private final AeroportReentrantLock aeroport;
    
 AvionReentrantLock(String identifiant, boolean estArrivee, AeroportReentrantLock aeroport) {
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
        Logger.info("avion " + getIdentifiant() + " (ARRIVÉE) commence son cycle");
        
        //  ATTERRISSAGE
        setEtat(EtatAvion.EN_ATTENTE_ATTERRISSAGE);
        aeroport.demanderPisteArrivee(getIdentifiant());
        
        atterrir(); 
        
        aeroport.libererPiste(getIdentifiant(), "atterrissage");
        
        // STATIONNEMENT À LA PORTE
        setEtat(EtatAvion.EN_ATTENTE_PORTE);
        aeroport.demanderPorte(getIdentifiant());
        
        stationner(); 
        
        aeroport.libererPorte(getIdentifiant());
        
        //  DÉCOLLAGE
        setEtat(EtatAvion.EN_ATTENTE_DECOLLAGE);
        aeroport.demanderPisteDepart(getIdentifiant());
        
        decoller(); 
        
        aeroport.libererPiste(getIdentifiant(), "décollage");
        
        setEtat(EtatAvion.TERMINE);
        Logger.success("avion" + getIdentifiant() + " a terminé son cycle !");
    }
    
    
    private void cycleDepart() {
        Logger.info("avion" + getIdentifiant() + " (DÉPART) commence son cycle");
        
        // 1 DÉCOLLAGE DIRECT
        setEtat(EtatAvion.EN_ATTENTE_DECOLLAGE);
        aeroport.demanderPisteDepart(getIdentifiant());
        
        decoller(); 
        
        aeroport.libererPiste(getIdentifiant(), "décollage");
        
        
        setEtat(EtatAvion.TERMINE);
        Logger.success("avion " + getIdentifiant() + " a terminé son cycle !");
    }
}