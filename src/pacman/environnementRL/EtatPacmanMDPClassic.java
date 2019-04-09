package pacman.environnementRL;

import environnement.Etat;
import pacman.elements.StateGamePacman;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire
 */
public class EtatPacmanMDPClassic implements Etat, Cloneable {
    private int xPacman;
    private int yPacman;
    private List<Integer> xGhosts = new ArrayList<>();
    private List<Integer> yGhosts = new ArrayList<>();
    private int distanceClosestPacdot;
    private int dimensions;

    public EtatPacmanMDPClassic(StateGamePacman _stategamepacman) {
        this.xPacman = _stategamepacman.getPacmanState(0).getX();
        this.yPacman = _stategamepacman.getPacmanState(0).getY();

        for(int i = 0; i < _stategamepacman.getNumberOfGhosts(); i++) {
            this.xGhosts.add(_stategamepacman.getGhostState(i).getX());
            this.yGhosts.add(_stategamepacman.getGhostState(i).getY());
        }

        this.distanceClosestPacdot = _stategamepacman.getClosestDot(_stategamepacman.getPacmanState(0));
        this.dimensions = _stategamepacman.getMaze().getSizeX() * _stategamepacman.getMaze().getSizeY() * 5;
    }

    public int getDimensions() {
        return this.dimensions;
    }

    @Override
    public String toString() {
        return "";
    }


    public Object clone() {
        EtatPacmanMDPClassic clone = null;
        try {
            // On recupere l'instance a renvoyer par l'appel de la
            // methode super.clone()
            clone = (EtatPacmanMDPClassic) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implementons
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }

        // on renvoie le clone
        return clone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.xPacman, this.yPacman, this.xGhosts, this.yGhosts, this.distanceClosestPacdot);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) obj;
        return this.xPacman == that.xPacman &&
                this.yPacman == that.yPacman &&
                Objects.equals(this.xGhosts, that.xGhosts) &&
                Objects.equals(this.yGhosts, that.yGhosts) &&
                this.distanceClosestPacdot == that.distanceClosestPacdot;
    }
}
