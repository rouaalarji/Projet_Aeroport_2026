package monitor;

import common.Logger;


 // Gestion de l'aéroport avec le mécanisme MONITOR
 
public class AeroportMonitor {
    
   
    private final int NB_PISTES_TOTAL = 2;
    private final int NB_PORTES_TOTAL = 4;
    
    private int pistesDisponibles;
    private int portesDisponibles;
    
  
    private int nbArrivesEnAttente = 0;
    private int nbDepartsEnAttente = 0;
    
  
    public AeroportMonitor() {
        this.pistesDisponibles = NB_PISTES_TOTAL;
        this.portesDisponibles = NB_PORTES_TOTAL;
        
        Logger.info("Aéroport initialisé : " + NB_PISTES_TOTAL + 
                    " pistes, " + NB_PORTES_TOTAL + " portes");
    }
    
    public synchronized void demanderPisteArrivee(String avionId) {
        nbArrivesEnAttente++; 
        
        Logger.warning(avionId + " attend une piste pour atterrir " +
                       "(Pistes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + ")");
        
        // Tant qu'il n'y a pas de piste disponible
        while (pistesDisponibles == 0) {
            try {
                wait(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.error(avionId + " - Interruption pendant l'attente");
                return;
            }
        }
        
        //  prendre une piste
        nbArrivesEnAttente--;
        pistesDisponibles--;
        
        Logger.success(avionId + " a obtenu une piste pour atterrir " +
                       "(Pistes restantes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + ")");
    }
    
    
    public synchronized void demanderPisteDepart(String avionId) {
        nbDepartsEnAttente++; // Incrémenter la file de départs
        
        Logger.warning(avionId + " attend une piste pour décoller " +
                       "(Pistes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + 
                       ", Arrivées en attente: " + nbArrivesEnAttente + ")");
        
        // Tant qu'il n'y a pas de piste OU qu'il y a des arrivées qui attendent
        while (pistesDisponibles == 0 || nbArrivesEnAttente > 0) {
            try {
                wait(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.error(avionId + " - Interruption pendant l'attente");
                return;
            }
        }
        
        //  prend une piste
        nbDepartsEnAttente--;
        pistesDisponibles--;
        
        Logger.success(avionId + " a obtenu une piste pour décoller " +
                       "(Pistes restantes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + ")");
    }
    
  
    public synchronized void libererPiste(String avionId, String typeAction) {
        pistesDisponibles++;
        
        Logger.info(avionId + " libère une piste après " + typeAction + 
                    " (Pistes: " + pistesDisponibles + "/" + NB_PISTES_TOTAL + ")");
        
        notifyAll();
    }
    
    public synchronized void demanderPorte(String avionId) {
        Logger.warning(avionId + " cherche une porte " +
                       "(Portes: " + portesDisponibles + "/" + NB_PORTES_TOTAL + ")");
        
        // Tant qu'il n'y a pas de porte disponible
        while (portesDisponibles == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.error(avionId + " - Interruption pendant l'attente d'une porte");
                return;
            }
        }
        
        portesDisponibles--;
        
        Logger.success(avionId + " a obtenu une porte " +
                       "(Portes restantes: " + portesDisponibles + "/" + NB_PORTES_TOTAL + ")");
    }
    
    
    public synchronized void libererPorte(String avionId) {
        portesDisponibles++;
        
        Logger.info(avionId + " libère une porte " +
                    "(Portes: " + portesDisponibles + "/" + NB_PORTES_TOTAL + ")");
        
        // Réveiller UN avion qui attend une porte
        notify();
    }
    
    //etat du avion
    public synchronized void afficherEtat() {
        Logger.info("ÉTAT DE L'AÉROPORT");
        System.out.println("    Pistes : " + pistesDisponibles + "/" + NB_PISTES_TOTAL);
        System.out.println("   Portes : " + portesDisponibles + "/" + NB_PORTES_TOTAL);
        System.out.println("    Arrivées en attente : " + nbArrivesEnAttente);
        System.out.println("   Départs en attente : " + nbDepartsEnAttente);
    }
}