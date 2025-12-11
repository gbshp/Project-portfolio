package components.QuantumCircuit;

import java.util.Arrays;
import java.util.Random;

/**
 * An abstract class providing implementations for all secondary methods of the
 * QuantumCircuit component.
 *
 * @author Pranay Kumar
 */
public abstract class QuantumCircuitSecondary implements QuantumCircuit {

    /**
     * Random number generator for measurements.
     */
    private static final Random RNG = new Random();

    @Override
    public final void printMeasurementProbabilities(double threshold) {
        // Prbability threshold check has to be between 0.0 and 1.0
        assert 0.0 <= threshold
                && threshold <= 1.0 : "Contract violation: threshold must be between 0.0 and 1.0";

        double[] probabilities = this.getProbabilities();
        int nQubits = this.numQubits();

        System.out.println("Measurement Probabilities:");
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] >= threshold) {
                // Format the integer 'i' as a binary string, padded with 0s
                String basisState = String
                        .format("%" + nQubits + "s", Integer.toBinaryString(i))
                        .replace(' ', '0');
                System.out.printf("  P(|%s>) = %.3f%%\n", basisState,
                        probabilities[i] * 100);
            }
        }
    }

    @Override
    public final int measure() {

        double[] probabilities = this.getProbabilities();
        double randomPoint = RNG.nextDouble();
        double cumulativeProbability = 0.0;

        // Find the basis state corresponding to the random point
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (randomPoint < cumulativeProbability) {
                return i;
            }
        }
    }

    @Override
    public final String getStateVectorString() {

        double[] probabilities = this.getProbabilities();
        int nQubits = this.numQubits();
        StringBuilder sb = new StringBuilder();
        sb.append("Quantum State (Probabilities):\n");

        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > 1e-9) { // Display only significant amplitudes
                // Format the integer 'i' as a binary string, padded with 0s
                String basisState = String
                        .format("%" + nQubits + "s", Integer.toBinaryString(i))
                        .replace(' ', '0');
                sb.append(String.format("  P(|%s>) = %.5f\n", basisState,
                        probabilities[i]));
            }
        }
        return sb.toString();
    }

    @Override
    public final String toString() {
        return "QuantumCircuit (" + this.numQubits() + " qubits)";
    }

    @Override
    public final boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof QuantumCircuit)) {
            return false;
        }
        QuantumCircuit other = (QuantumCircuit) obj;
        if (this.numQubits() != other.numQubits()) {
            return false;
        }
        return Arrays.equals(this.getProbabilities(), other.getProbabilities());
    }

}
