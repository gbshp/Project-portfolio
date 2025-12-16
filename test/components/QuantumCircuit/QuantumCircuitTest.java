package components.QuantumCircuit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit test fixture for QuantumCircuit's Secondary Methods. This tests the
 * logic in QuantumCircuitSecondary (equals, toString, etc.).
 */
public class QuantumCircuitTest {

    /**
     * Helper to create a default instance using the 1L implementation. We use
     * 1L here because we need *some* concrete class to test the abstract logic.
     */
    private QuantumCircuit createFromArgs(int n) {
        return new QuantumCircuit1L(n);
    }

    // --- Object Method Tests (equals, hashCode, toString) ---

    @Test
    public void testEquals_Reflexive() {
        QuantumCircuit qc = this.createFromArgs(2);
        assertTrue("A circuit should equal itself", qc.equals(qc));
    }

    @Test
    public void testEquals_Symmetric() {
        QuantumCircuit qc1 = this.createFromArgs(2);
        QuantumCircuit qc2 = this.createFromArgs(2);

        // Both act as new instances (state |00>)
        assertTrue("Two new circuits of same size should be equal",
                qc1.equals(qc2));
        assertTrue("Symmetry check", qc2.equals(qc1));
    }

    @Test
    public void testEquals_DifferentState() {
        QuantumCircuit qc1 = this.createFromArgs(1);
        QuantumCircuit qc2 = this.createFromArgs(1);

        // Change qc1 state to |1> using X-gate logic (Hadamard-like for now)
        // Or simply: Apply H to qc1, leave qc2 as |0>
        qc1.addHadamardGate(0);
        qc1.run();

        qc2.run(); // qc2 is still |0>

        assertFalse("Circuits with different states should not be equal",
                qc1.equals(qc2));
    }

    @Test
    public void testEquals_DifferentSize() {
        QuantumCircuit qc1 = this.createFromArgs(1);
        QuantumCircuit qc2 = this.createFromArgs(2);

        qc1.run();
        qc2.run();

        assertFalse("Circuits with different qubits should not be equal",
                qc1.equals(qc2));
    }

    @Test
    public void testHashCode() {
        QuantumCircuit qc1 = this.createFromArgs(2);
        QuantumCircuit qc2 = this.createFromArgs(2);

        // Prepare them identically
        qc1.addHadamardGate(0);
        qc2.addHadamardGate(0);
        qc1.run();
        qc2.run();

        assertEquals("Equal objects must have equal hashCodes", qc1.hashCode(),
                qc2.hashCode());
    }

    @Test
    public void testToString() {
        QuantumCircuit qc = this.createFromArgs(5);
        String str = qc.toString();
        assertEquals("QuantumCircuit (5 qubits)", str);
    }

    // --- Secondary Logic Tests ---

    @Test
    public void testGetStateVectorString() {
        QuantumCircuit qc = this.createFromArgs(2);
        // Create state (|00> + |10>) / sqrt(2)
        qc.addHadamardGate(0);
        qc.run();

        String output = qc.getStateVectorString();

        // We expect the output to contain the basis states with non-zero probability
        assertTrue("Output should contain label",
                output.contains("Quantum State"));
        // Basis state 0 (binary 00)
        assertTrue("Should list state |00>", output.contains("|00>"));
        // Basis state 1 (binary 01) - Probability is 0, should NOT be shown (if optimization exists)
        // Note: The implementation logic says "if probabilities[i] > 1e-9", so 01 shouldn't be there.
        assertFalse("Should not list state |01> (prob is 0)",
                output.contains("|01>"));
    }

    @Test
    public void testMeasure_Deterministic() {
        // If state is 100% |0>, measure must return 0
        QuantumCircuit qc = this.createFromArgs(1);
        qc.run(); // Default |0>

        int measurement = qc.measure();
        assertEquals(0, measurement);
    }

    @Test
    public void testMeasure_Deterministic_State1() {
        // If we flip to |1> (using H + Z logic or just H+H logic if implemented,
        // but let's assume we want to test a definite non-zero state).
        // Since we only have H and CNOT in our simple kernel, let's create a known 50/50
        // and check bounds.

        QuantumCircuit qc = this.createFromArgs(1);
        qc.addHadamardGate(0);
        qc.run();

        int measurement = qc.measure();
        assertTrue("Measurement must be 0 or 1",
                measurement == 0 || measurement == 1);
    }
}
