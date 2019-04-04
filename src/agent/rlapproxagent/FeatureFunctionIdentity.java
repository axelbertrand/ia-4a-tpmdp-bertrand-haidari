package agent.rlapproxagent;

import environnement.Action;
import environnement.Etat;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici
 *
 * @author laetitiamatignon
 */
public class FeatureFunctionIdentity implements FeatureFunction {
    //*** VOTRE CODE

    private int featuresCount;
    private List<Pair<Etat, Action>> alreadyCalled = new ArrayList<>();

    public FeatureFunctionIdentity(int _nbEtat, int _nbAction) {
        //*** VOTRE CODE

        this.featuresCount = _nbEtat * _nbAction;
    }

    @Override
    public int getFeatureNb() {
        //*** VOTRE CODE

        return this.featuresCount;
    }

    @Override
    public double[] getFeatures(Etat e, Action a) {
        //*** VOTRE CODE

        Pair<Etat, Action> pair = new Pair<>(e, a);
        if(!this.alreadyCalled.contains(pair)) {
            this.alreadyCalled.add(pair);
        }

        int oneIndex = this.alreadyCalled.indexOf(pair) % this.featuresCount;
        double[] features = new double[this.featuresCount];
        features[oneIndex] = 1.;

        return features;
    }
}
