package common;


 // Énumération des différents états possibles d'un avion

public enum EtatAvion {
    EN_ATTENTE_ATTERRISSAGE(" En attente d'atterrissage"),
    ATTERRISSAGE(" Atterrissage en cours"),
    EN_ATTENTE_PORTE("En attente d'une porte"),
    STATIONNEMENT(" À la porte (embarquement)"),
    EN_ATTENTE_DECOLLAGE(" En attente de décollage"),
    DECOLLAGE(" Décollage en cours"),
    TERMINE("Vol terminé");
    
    // Description lisible de l'état
    private final String description;
    
    // Constructeur
    EtatAvion(String description) {
        this.description = description;
    }
    
    // Obtenir la description
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}