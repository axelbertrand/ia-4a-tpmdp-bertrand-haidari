package agent.rlapproxagent;


import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;

/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur :
 * approximation lineaire de fonctions caracteristiques
 *
 * @author laetitiamatignon
 */
public class QLApproxAgent extends QLearningAgent {

    private FeatureFunction featureFunction;
    private double[] weights;

    public QLApproxAgent(double alpha, double gamma, Environnement _env, FeatureFunction _featurefunction) {
        super(alpha, gamma, _env);
        //*** VOTRE CODE

        this.featureFunction = _featurefunction;
        this.weights = new double[this.featureFunction.getFeatureNb()];
    }

    @Override
    public double getQValeur(Etat e, Action a) {
        //*** VOTRE CODE

        double[] features = this.featureFunction.getFeatures(e, a);

        double qApprox = 0.;
        for(int i = 0; i < features.length; i++) {
            qApprox += this.weights[i] * features[i];
        }

        return qApprox;
    }

    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        if (RLAgent.DISPRL) {
            System.out.println("QL: mise a jour poids pour etat \n" + e + " action " + a + " etat' \n" + esuivant + " r " + reward);
        }
        //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode
        //arrete episode lq etat courant absorbant

        //*** VOTRE CODE

        double maxQApprox = Double.NEGATIVE_INFINITY;
        for(Action b : this.getActionsLegales(esuivant)) {
            maxQApprox = Math.max(maxQApprox, this.getQValeur(esuivant, b));
        }

        double[] features = this.featureFunction.getFeatures(e, a);
        for(int i = 0; i < features.length; i++) {
            this.weights[i] += this.alpha * (reward + this.gamma * maxQApprox - this.getQValeur(e, a)) * features[i];
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.qvaleurs.clear();

        //*** VOTRE CODE

        this.weights = new double[this.featureFunction.getFeatureNb()];

        this.episodeNb = 0;
        this.notifyObs();
    }
}
