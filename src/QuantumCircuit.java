/**
 * This interface defines the operations for a quantum circuit, including the
 * principles of superposition and entanglement.
 *
 * @author Pranay Kumar
 */
public interface QuantumCircuit extends QuantumCircuitKernel {

    /**
     * Prints the measurement probabilities of the quantum state.
     *
     * @param threshold
     *            The minimum probability to display.
     * @requires $this.gates = <> (i.e., {@code run()} has been called) AND 0.0
     *           <= threshold <= 1.0
     * @ensures The probabilities for all basis states threshold are printed to
     *          the console
     */
    void printMeasurementProbabilities(double threshold);

    /**
     * Simulates a single measurement, collapsing the quantum state.
     *
     * @return The integer representation of the measured basis state (e.g., for
     *         a 2-qubit system, measuring |10> returns 2).
     * @requires $this.gates = <> (i.e., {@code run()} has been called)
     * @updates $this.state_vector
     * @ensures The quantum state collapses to the measured basis state and
     *          measure = [integer corresponding to the measured basis state]
     */
    int measure();

    /**
     * Returns a string representation of the current state vector.
     *
     * @return A string formatted to show the amplitude for each basis state.
     * @requires $this.gates = <> (i.e., {@code run()} has been called)
     * @ensures getStateVectorString = [string representation of
     *          $this.state_vector]
     */
    String getStateVectorString();
}
