import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Use Case 1: Bell State Demonstration. * This program demonstrates the
 * creation of quantum entanglement (Bell State |Phi+>). It shows how
 * manipulating one qubit affects another, a fundamental property unique to
 * quantum mechanics.
 */
public final class BellStateDemo {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BellStateDemo() {
    }

    /**
     * Main method.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();

        out.println("This program creates an entangled pair of qubits "
                + "which is a Bell State (|Phi+>)");

        QuantumCircuit circuit = new QuantumCircuit1L(2);

        // Create Superposition on Qubit 0
        // State becomes: (|00> + |10>) / sqrt(2)
        circuit.addHadamardGate(0);

        // Entangle Qubit 0 and Qubit 1 using CNOT
        // State becomes: (|00> + |11>) / sqrt(2) i.e. Bell State: |Phi+>
        circuit.addCNotGate(0, 1);

        // Run the circuit simulation
        circuit.run();

        // Check probabilities
        out.println("\nTheoretical Probabilities:");
        circuit.printMeasurementProbabilities(0.01);

        // \nulate measurements
        out.println("\nRunning 10 simulated measurements...");
        // We have to reset the circuit for each measurement because measurement
        // collapses the wave function.
        for (int i = 0; i < 10; i++) {
            circuit.clear();
            circuit.addHadamardGate(0);
            circuit.addCNotGate(0, 1);
            circuit.run();

            int result = circuit.measure();
            String binary = String.format("%2s", Integer.toBinaryString(result))
                    .replace(' ', '0');
            out.println("Measurement " + (i + 1) + ": |" + binary + ">");
        }


        in.close();
        out.close();
    }
}
