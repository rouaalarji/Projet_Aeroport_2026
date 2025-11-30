package common;


public class Avion extends Thread {
    
    // Attributs de base
    private final String identifiant;      
    private final boolean estArrivee;      
    private EtatAvion etat;               
    
    
    public Avion(String identifiant, boolean estArrivee) {
        this.identifiant = identifiant;
        this.estArrivee = estArrivee;
        
        // État initial selon le type d'avion
        if (estArrivee) {
            this.etat = EtatAvion.EN_ATTENTE_ATTERRISSAGE;
        } else {
            this.etat = EtatAvion.EN_ATTENTE_DECOLLAGE;
        }
    }
    
    
    
    public String getIdentifiant() {
        return identifiant;
    }
    
    public boolean estArrivee() {
        return estArrivee;
    }
    
    public EtatAvion getEtat() {
        return etat;
    }
    
   
    
    public void setEtat(EtatAvion nouvelEtat) {
        this.etat = nouvelEtat;
        Logger.log("avion " + identifiant + " → " + nouvelEtat);
    }
    
    
    
    public void simulerAction(int duree) {
        try {
            Thread.sleep(duree);
        } catch (InterruptedException e) {
            Logger.error(identifiant + " - Interruption : " + e.getMessage());
            Thread.currentThread().interrupt(); 
        }
    }
    
    
    public void atterrir() {
        setEtat(EtatAvion.ATTERRISSAGE);
        //Faire attendre le thread
        simulerAction(2000); 
    }
    
    
    public void stationner() {
        setEtat(EtatAvion.STATIONNEMENT);
        simulerAction(3000);
    }
    
   
    public void decoller() {
        setEtat(EtatAvion.DECOLLAGE);
        simulerAction(2000); 
    }
    
  
    @Override
    public String toString() {
        String type = estArrivee ? "ARRIVÉE" : "DÉPART";
        return identifiant + " (" + type + ") - " + etat;
    }
}