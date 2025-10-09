import java.util.ArrayList;
import java.util.List;

/**
 * A proof-of-concept for a Quantum Circuit component in a single Java file.
 * This class simulates the behavior of a quantum circuit, including the
 * principles of superposition and entanglement.
 *
 * @author Pranay Kumar
 */
public class QuantumCircuit {

    /**
     * Represents a complex number, essential for quantum state amplitudes.
     */
    private static class Complex {
        /** The real part of the complex number. */
        private final double real;
        /** The imaginary part of the complex number. */
        private final double imaginary;

        /**
         * Constructs a complex number with real and imaginary parts.
         *
         * @param real
         *            The real part of the complex number
         * @param imaginary
         *            The imaginary part of the complex number
         */
        Complex(double real, double imaginary) {
            this.real = real;
            this.imaginary = imaginary;
        }

        /**
         * Gets the real part of this complex number.
         *
         * @return The real part of the complex number
         */
        public double getReal() {
            return this.real;
        }

        /**
         * Gets the imaginary part of this complex number.
         *
         * @return The imaginary part of the complex number
         */
        public double getImaginary() {
            return this.imaginary;
        }

        /**
         * Adds this complex number with another complex number.
         *
         * @param other
         *            The complex number to add
         * @return A new Complex number representing the sum
         */
        public Complex plus(Complex other) {
            return new Complex(this.real + other.real,
                    this.imaginary + other.imaginary);
        }

        /**
         * Multiplies this complex number with another complex number.
         *
         * @param other
         *            The complex number to multiply with
         * @return A new Complex number representing the product
         */
        public Complex times(Complex other) {
            double newReal = this.real * other.real
                    - this.imaginary * other.imaginary;
            double newImag = this.real * other.imaginary
                    + this.imaginary * other.real;
            return new Complex(newReal, newImag);
        }

        /**
         * Calculates the squared magnitude of this complex number.
         *
         * @return The squared magnitude (real² + imaginary²)
         */
        public double magnitudeSq() {
            return this.real * this.real + this.imaginary * this.imaginary;
        }

        @Override
        public String toString() {
            if (this.imaginary == 0) {
                return String.format("%.3f", this.real);
            }
            if (this.real == 0) {
                return String.format("%.3fi", this.imaginary);
            }
            if (this.imaginary < 0) {
                return String.format("%.3f - %.3fi", this.real,
                        -this.imaginary);
            }
            return String.format("%.3f + %.3fi", this.real, this.imaginary);
        }
    }

    /**
     * Represents a quantum gate operation to be applied to the circuit.
     */
    private static class Gate {
        /** The name of the quantum gate operation. */
        private String name;
        /** The target qubit for the gate operation. */
        private int target;
        /** The control qubit for controlled operations, -1 if none. */
        private int control;

        /**
         * Gets the name of the gate.
         *
         * @return The gate name
         */
        public String getName() {
            return this.name;
        }

        /**
         * Gets the target qubit.
         *
         * @return The target qubit index
         */
        public int getTarget() {
            return this.target;
        }

        /**
         * Gets the control qubit.
         *
         * @return The control qubit index
         */
        public int getControl() {
            return this.control;
        }

        /**
         * Constructs a quantum gate with a name and target qubit.
         *
         * @param name
         *            The name of the gate operation
         * @param target
         *            The target qubit for the gate operation
         */
        Gate(String name, int target) {
            this(name, target, -1);
        }

        /**
         * Constructs a quantum gate with a name, target qubit, and control
         * qubit.
         *
         * @param name
         *            The name of the gate operation
         * @param target
         *            The target qubit for the gate operation
         * @param control
         *            The control qubit for controlled operations
         */
        Gate(String name, int target, int control) {
            this.name = name;
            this.target = target;
            this.control = control;
        }
    }

    /** The number of qubits in the quantum circuit. */
    private final int numQubits;
    /** The state vector representing the quantum state. */
    private Complex[] stateVector;
    /** The list of quantum gates to be applied to the circuit. */
    private final List<Gate> gates;

    /** The constant 1/√2 used in Hadamard gate operations. */
    private static final Complex ONE_OVER_SQRT_2 = new Complex(
            1.0 / Math.sqrt(2), 0);

    /**
     * Initializes a quantum circuit with a given number of qubits. The initial
     * state is |00...0>.
     *
     * @param numQubits
     *            The number of qubits in the circuit
     */
    public QuantumCircuit(int numQubits) {
        if (numQubits <= 0) {
            throw new IllegalArgumentException(
                    "Number of qubits must be positive.");
        }
        this.numQubits = numQubits;
        this.gates = new ArrayList<>();

        // The state vector size is 2^n
        int stateVectorSize = (int) Math.pow(2, numQubits);
        this.stateVector = new Complex[stateVectorSize];

        // Initialize to |0...0> state, which means the first amplitude is 1
        // and all others are 0. i.e. represents the state |00...0>
        for (int i = 1; i < stateVectorSize; i++) {
            this.stateVector[i] = new Complex(0, 0);
        }
        this.stateVector[0] = new Complex(1, 0);
    }

    /**
     * Adds a Hadamard gate to the circuit, which creates superposition.
     *
     * @param targetQubit
     *            The qubit to apply the gate to (0-indexed).
     */
    public void addHadamardGate(int targetQubit) {
        this.gates.add(new Gate("H", targetQubit));
    }

    /**
     * Simulates the circuit by applying all added gates to the state vector.
     */
    public void run() {
        for (Gate gate : this.gates) {
            this.applyGate(gate);
        }
    }

    /**
     * Applies the specified quantum gate operation to the circuit's state
     * vector.
     *
     * @param gate
     *            The quantum gate to be applied
     */
    private void applyGate(Gate gate) {
        if (gate.name.equals("H")) {
            this.applyHadamard(gate.target);
        }
    }

    /**
     * Applies a Hadamard gate to the specified target qubit.
     *
     * @param target
     *            The index of the target qubit
     */
    private void applyHadamard(int target) {
        int stride = (int) Math.pow(2, target);
        for (int i = 0; i < this.stateVector.length; i += 2 * stride) {
            for (int j = 0; j < stride; j++) {
                int index0 = i + j;
                int index1 = i + j + stride;

                Complex amp0 = this.stateVector[index0];
                Complex amp1 = this.stateVector[index1];

                this.stateVector[index0] = (amp0.plus(amp1))
                        .times(ONE_OVER_SQRT_2);
                this.stateVector[index1] = (amp0
                        .plus(amp1.times(new Complex(-1, 0))))
                                .times(ONE_OVER_SQRT_2);
            }
        }

    }

    /**
     * Prints the measurement probabilities of the quantum state in the circuit.
     * Only states with non-negligible probabilities are displayed.
     */
    public void printMeasurementProbabilities() {
        System.out.println("Measurement Probabilities:");
        for (int i = 0; i < this.stateVector.length; i++) {
            double probability = this.stateVector[i].magnitudeSq();
            System.out.println(probability * 100);
        }
    }

    /**
     * Main method to demonstrate quantum circuit operations.
     *
     * @param args
     *            Command line arguments
     */
    public static void main(String[] args) {
        //Applyng Hadamard gate to a single qubit
        QuantumCircuit superposition = new QuantumCircuit(1);
        superposition.addHadamardGate(0);
        superposition.run();
        superposition.printMeasurementProbabilities();
        System.out.println(
                "This shows an equal 50% chance of measuring |0> or |1>, "
                        + "a classic sign of entanglement.");

    }

}
