package semaphore;

import java.util.concurrent.Semaphore;
import common.Logger;

/**
 * Gestion de l'aéroport avec le mécanisme SEMAPHORE
 * Utilise : Semaphore.acquire() et Semaphore.release()
 */
public class AeroportSemaphore {
    
   
    private final int NB_PISTES_TOTAL = 2;
    private final int NB_PORTES_TOTAL = 4;
    
    
    private final Semaphore pistes;      
    private final Semaphore portes;    
    
    private final Semaphore mutexArrivees;  
    private final Semaphore mutexDeparts;   
    
    private int nbArrivesEnAttente = 0;
    private int nbDepartsEnAttente = 0;
    
    public AeroportSemaphore() {
        // Créer les semaphores avec le nombre de ressources
        this.pistes = new Semaphore(NB_PISTES_TOTAL);
        this.portes = new Semaphore(NB_PORTES_TOTAL);
        
        // Créer les mutex (Semaphore avec 1 seul jeton)
        this.mutexArrivees = new Semaphore(1);
        this.mutexDeparts = new Semaphore(1);
        
        Logger.info("Aéroport initialisé (SEMAPHORE) : " + NB_PISTES_TOTAL + 
                    " pistes, " + NB_PORTES_TOTAL + " portes");
    }
    
    
    
    public void demanderPisteArrivee(String avionId) {
        try {
            // 1 Incrémenter le compteur d'arrivées 
            mutexArrivees.acquire();
            nbArrivesEnAttente++;
            int nbPistesLibres = pistes.availablePermits();
            mutexArrivees.release();
            
            Logger.warning(avionId + " attend une piste pour atterrir " +
                           "(Pistes libres: " + nbPistesLibres + "/" + NB_PISTES_TOTAL + 
                           ", Arrivées en attente: " + nbArrivesEnAttente + ")");
            
            pistes.acquire();
            
            // 3 Décrémenter le compteur d'arrivées
            mutexArrivees.acquire();
            nbArrivesEnAttente--;
            mutexArrivees.release();
            
            Logger.success(avionId + " a obtenu une piste pour atterrir " +
                           "(Pistes restantes: " + pistes.availablePermits() + "/" + NB_PISTES_TOTAL + ")");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.error(avionId + " - Interruption pendant l'attente d'une piste");
        }
    }
    
    
    public void demanderPisteDepart(String avionId) {
        try {
            //  Incrémenter le compteur de départs
            mutexDeparts.acquire();
            nbDepartsEnAttente++;
            mutexDeparts.release();
            
            Logger.warning(avionId + " attend une piste pour décoller " +
                           "(Pistes libres: " + pistes.availablePermits() + "/" + NB_PISTES_TOTAL + 
                           ", Arrivées en attente: " + nbArrivesEnAttente + ")");
            
            //  Attendre qu'il n'y ait PLUS d'arrivées en attente 
            while (nbArrivesEnAttente > 0) {
                Thread.sleep(100); 
            }
            
            // Demander une piste
            pistes.acquire();
            
            //  Décrémenter le compteur de départs
            mutexDeparts.acquire();
            nbDepartsEnAttente--;
            mutexDeparts.release();
            
            Logger.success(avionId + " a obtenu une piste pour décoller " +
                           "(Pistes restantes: " + pistes.availablePermits() + "/" + NB_PISTES_TOTAL + ")");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.error(avionId + " - Interruption pendant l'attente d'une piste");
        }
    }
    
    
    public void libererPiste(String avionId, String typeAction) {
        pistes.release(); // Rendre le jeton
        
        Logger.info(avionId + " libère une piste après " + typeAction + 
                    " (Pistes: " + pistes.availablePermits() + "/" + NB_PISTES_TOTAL + ")");
    }
    
    
   
    public void demanderPorte(String avionId) {
        try {
            Logger.warning(avionId + " cherche une porte " +
                           "(Portes libres: " + portes.availablePermits() + "/" + NB_PORTES_TOTAL + ")");
            
            portes.acquire(); 
            
            Logger.success(avionId + " a obtenu une porte " +
                           "(Portes restantes: " + portes.availablePermits() + "/" + NB_PORTES_TOTAL + ")");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.error(avionId + " - Interruption pendant l'attente d'une porte");
        }
    }
  
    public void libererPorte(String avionId) {
        portes.release(); 
        
        Logger.info(avionId + " libère une porte " +
                    "(Portes: " + portes.availablePermits() + "/" + NB_PORTES_TOTAL + ")");
    }
    
   
    public void afficherEtat() {
        Logger.info("ÉTAT DE L'AÉROPORT (SEMAPHORE)");
        System.out.println("    Pistes : " + pistes.availablePermits() + "/" + NB_PISTES_TOTAL);
        System.out.println("    Portes : " + portes.availablePermits() + "/" + NB_PORTES_TOTAL);
        System.out.println("    Arrivées en attente : " + nbArrivesEnAttente);
        System.out.println("    Départs en attente : " + nbDepartsEnAttente);
    }
}