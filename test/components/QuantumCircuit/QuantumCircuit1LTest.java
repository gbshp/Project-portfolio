package components.QuantumCircuit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit test fixture for QuantumCircuit1L.
 */
public class QuantumCircuit1LTest {

    /**
     * Helper to create a default instance (1 qubit).
     */
    private QuantumCircuit createFromArgs(int n) {
        return new QuantumCircuit1L(n);
    }

    // --- Constructor Tests ---

    @Test
    public void testConstructorInitialState() {
        QuantumCircuit qc = this.createFromArgs(1);
        qc.run(); // Run empty circuit

        double[] probs = qc.getProbabilities();
        // Should be |0> with 100% probability
        assertEquals(1.0, probs[0], 0.001);
        assertEquals(0.0, probs[1], 0.001);
        assertEquals(1, qc.numQubits());
    }

    @Test
    public void testConstructor_Sizes() {
        QuantumCircuit qc = this.createFromArgs(3);
        qc.run();
        double[] probs = qc.getProbabilities();

        // 3 qubits = 2^3 = 8 states
        assertEquals(8, probs.length);
        assertEquals(1.0, probs[0], 0.001);
    }

    // --- Gate Logic Tests ---

    @Test
    public void testHadamardSuperposition() {
        QuantumCircuit qc = this.createFromArgs(1);
        qc.addHadamardGate(0);
        qc.run();

        double[] probs = qc.getProbabilities();
        // |0> and |1> should both be approx 0.5
        assertEquals(0.5, probs[0], 0.001);
        assertEquals(0.5, probs[1], 0.001);
    }

    @Test
    public void testBellStateEntanglement() {
        // Test the famous |Phi+> state: (|00> + |11>) / sqrt(2)
        QuantumCircuit qc = this.createFromArgs(2);

        qc.addHadamardGate(0); // Put qubit 0 in superposition
        qc.addCNotGate(0, 1); // Entangle 0 -> 1
        qc.run();

        double[] probs = qc.getProbabilities();

        // P(|00>) = 0.5 (Index 0)
        assertEquals(0.5, probs[0], 0.001);
        // P(|01>) = 0.0 (Index 1)
        assertEquals(0.0, probs[1], 0.001);
        // P(|10>) = 0.0 (Index 2)
        assertEquals(0.0, probs[2], 0.001);
        // P(|11>) = 0.5 (Index 3)
        assertEquals(0.5, probs[3], 0.001);
    }

    @Test
    public void testClear() {
        QuantumCircuit qc = this.createFromArgs(1);
        qc.addHadamardGate(0);
        qc.run();

        qc.clear(); // Should reset to |0>
        // Note: run() not needed after clear() as it resets state vector directly
        // But for safety in your implementation, let's just check probabilities directly
        // or check if it behaves like a new instance.

        double[] probs = qc.getProbabilities();
        assertEquals(1.0, probs[0], 0.001);
    }

    @Test
    public void testTransferFrom() {
        QuantumCircuit qc1 = this.createFromArgs(1);
        qc1.addHadamardGate(0);
        qc1.run();

        QuantumCircuit qc2 = this.createFromArgs(1);
        qc2.transferFrom(qc1);

        // qc2 should now have the superposition
        double[] probs = qc2.getProbabilities();
        assertEquals(0.5, probs[0], 0.001);

        // qc1 should be cleared (empty/default)
        // Note: Standard transferFrom usually restores source to initial state
        assertEquals(1, qc1.numQubits());
        double[] probs1 = qc1.getProbabilities();
        assertEquals(1.0, probs1[0], 0.001);
    }

    // --- Measure Test ---

    @Test
    public void testMeasure_Collapse() {
        // We can't deterministic test random outcomes,
        // but we can test that the result is valid.
        QuantumCircuit qc = this.createFromArgs(1);
        qc.addHadamardGate(0);
        qc.run();

        int result = qc.measure();
        assertTrue(result == 0 || result == 1);
    }
}
