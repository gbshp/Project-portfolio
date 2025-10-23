import components.standard.Standard;

/**
 * Kernel interface for a QuantumCircuit component.
 *
 * @author Pranay Kumar
 */
public interface QuantumCircuitKernel extends Standard<QuantumCircuit> {
    /**
     * Adds a Hadamard gate to the circuit.
     *
     * @param targetQubit
     *            The index of the qubit to apply the gate to (0-indexed).
     * @requires 0 <= targetQubit < $this.num_qubits
     * @updates $this.gates
     * @ensures $this.gates = #$this.gates * <Hadamard(targetQubit)>
     */
    void addHadamardGate(int targetQubit);

    /**
     * Adds a Controlled-NOT (CNOT) gate to the circuit.
     *
     * @param controlQubit
     *            The index of the control qubit (0-indexed).
     * @param targetQubit
     *            The index of the target qubit (0-indexed).
     * @requires 0 <= controlQubit < $this.num_qubits AND 0 <= targetQubit <
     *           $this.num_qubits AND controlQubit != targetQubit
     * @updates $this.gates
     * @ensures $this.gates = #$this.gates * <CNOT(controlQubit, targetQubit)>
     */
    void addCNotGate(int controlQubit, int targetQubit);

    /**
     * Simulates the circuit by applying all added gates to the state vector.
     *
     * @updates $this.state_vector
     * @clears $this.gates
     * @ensures $this.state_vector = [result of applying all gates in
     *          #$this.gates to the initial state vector] AND $this.gates = <>
     */
    void run();

    /**
     * Returns an array of measurement probabilities for each basis state.
     *
     * @return An array of doubles, where index {@code i} corresponds to the
     *         probability of measuring the basis state represented by the
     *         integer {@code i}.
     * @requires $this.gates = <> (i.e., {@code run()} has been called)
     * @ensures getProbabilities = [array 'p' of size 2^$this.num_qubits where
     *          p[i] = $this.state_vector[i].magnitudeSq()]
     */
    double[] getProbabilities();

    /**
     * Reports the number of qubits in this circuit.
     *
     * @return The number of qubits.
     * @ensures numQubits = $this.num_qubits
     */
    int numQubits();

}
