import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Quantum Random Number Generator (RNG) using quantum circuits.
 */
public final class QuantumRNG {

    /**
     * Private constructor to prevent instantiation.
     */
    private QuantumRNG() {
    }

    /**
     * Generates a random byte (0-255) using 8 quantum bits.
     * @return a random integer between 0 and 255.
     */
    private static int generateQuantumByte() {
        // Initialize an 8-qubit quantum circuit
        QuantumCircuit circuit = new QuantumCircuit1L(8);

        // Put every qubit into superposition
        for (int i = 0; i < 8; i++) {
            circuit.addHadamardGate(i);
        }

        circuit.run();
        return circuit.measure();
    }



    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();

        out.print("Key: ");
        for (int i = 0; i < 5; i++) {
            int randomByte = generateQuantumByte();
            // Format as Hexadecimal
            out.print(String.format("%02X ", randomByte));
        }

        out.close();
    }
}
