package agent.rlapproxagent;

import environnement.Action;
import environnement.Etat;
import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;

/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *
 * @author laetitiamatignon
 */
public class FeatureFunctionPacman implements FeatureFunction {
    private double[] vfeatures;

    private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon
    //--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles

    public FeatureFunctionPacman() {

    }

    @Override
    public int getFeatureNb() {
        return 4;
    }

    @Override
    public double[] getFeatures(Etat e, Action a) {
        this.vfeatures = new double[4];
        StateGamePacman stategamepacman;
        //EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

        //calcule pacman resulting position a partir de Etat e
        if (e instanceof StateGamePacman) {
            stategamepacman = (StateGamePacman) e;
        } else {
            System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
            return this.vfeatures;
        }

        StateAgentPacman pacmanstate_next = stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));

        //*** VOTRE CODE

        /* feature biais */
        this.vfeatures[0] = 1;

        /* feature fantôme */
        int ghostCount = 0;
        int distPacmanGhostX;
        int distPacmanGhostY;
        for(int i = 0; i < stategamepacman.getNumberOfGhosts(); i++) {
            StateAgentPacman currentGhost = stategamepacman.getGhostState(i);
            distPacmanGhostX = Math.abs(currentGhost.getX() - pacmanstate_next.getX());
            distPacmanGhostY = Math.abs(currentGhost.getY() - pacmanstate_next.getY());
            if ((distPacmanGhostX <= 1 && distPacmanGhostY == 0) || (distPacmanGhostX == 0 && distPacmanGhostY <= 1)) {
                ghostCount++;
            }
        }
        this.vfeatures[1] = ghostCount;

        /* feature pacdot */
        boolean pacmannextOnPacdot = stategamepacman.getMaze().isFood(pacmanstate_next.getX(), pacmanstate_next.getY());
        this.vfeatures[2] = pacmannextOnPacdot ? 1 : 0;

        /* feature distance avec le pacdot le plus proche */
        double distanceClosestPacdot = stategamepacman.getClosestDot(pacmanstate_next);
        this.vfeatures[3] = distanceClosestPacdot / (stategamepacman.getMaze().getSizeX() * stategamepacman.getMaze().getSizeY());

        return this.vfeatures;
    }

    public void reset() {
        this.vfeatures = new double[4];
    }
}
