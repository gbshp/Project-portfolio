import java.util.ArrayList;
import java.util.List;

/**
 * Kernel implementation of the QuantumCircuit component.
 *
 * @author Pranay Kumar
 */
public class QuantumCircuit1L extends QuantumCircuitSecondary {

    /**
     * Represents a complex number with real and imaginary parts.
     */
    private static class Complex {
        double re;
        double im;

        Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }

        // Returns the magnitude of the complex number
        double magnitude() {
            return Math.sqrt((this.re * this.re) + (this.im * this.im));
        }
    }

    /**
     * Represents the number of qubits in the circuit.
     */
    private int numQubits;

    /**
     * The state vector representing the quantum state.
     */
    private Complex[] stateVector;

    /*
     * A list to hold gates before run() is called on the circuit
     */
    private List<Gate> gates;

    /*
     * Constructors
     */

    /**
     * Constructor with number of qubits.
     *
     * @param numQubits
     *            number of qubits in the circuit
     */
    public QuantumCircuit1L(int numQubits) {
        this.createNewRep(numQubits);
    }

    private void createNewRep(int n) {
        this.numQubits = n;
        this.gates = new ArrayList<>();
        int size = 1 << n; // 2^n
        this.stateVector = new Complex[size];

        // Initialize state vector to |0...0> (Index 0 is 1.0, rest are 0.0)
        this.stateVector[0] = new Complex(1.0, 0.0);
        for (int i = 1; i < size; i++) {
            this.stateVector[i] = new Complex(0.0, 0.0);
        }
    }

    /**
     * Interface for a quantum gate logic.
     */
    private interface Gate {
        void apply(Complex[] stateVector, int numQubits);
    }

    private static class HadamardGate implements Gate {

        private int targetQubit;

        HadamardGate(int targetQubit) {
            this.targetQubit = targetQubit;
        }

        @Override
        public void apply(Complex[] state, int n) {
            double invSqrt2 = 1.0 / Math.sqrt(2.0);
            int size = state.length;
            for (int i = 0; i < size; i++) {
                // Check if the bit at 'target' position is 0
                if ((i & (1 << this.target)) == 0) {
                    int j = i | (1 << this.target); // Flip the target bit to get index j

                    double realI = state[i].re;
                    double imagI = state[i].im;
                    double realJ = state[j].re;
                    double imagJ = state[j].im;

                    state[i].re = (realI + realJ) * invSqrt2;
                    state[i].im = (imagI + imagJ) * invSqrt2;

                    state[j].re = (realI - realJ) * invSqrt2;
                    state[j].im = (imagI - imagJ) * invSqrt2;
                }
            }
        }
    }

    /**
     * Concrete CNOT logic.
     */
    private static class CNotGate implements Gate {
        private int control;
        private int target;

        CNotGate(int control, int target) {
            this.control = control;
            this.target = target;
        }

        @Override
        public void apply(Complex[] state, int n) {
            int size = state.length;
            for (int i = 0; i < size; i++) {
                // Check if control bit is 1
                if ((i & (1 << this.control)) != 0) {
                    // We only swap if control is 1.
                    if ((i & (1 << this.target)) == 0) {
                        int j = i | (1 << this.target);

                        // Swap amplitudes of state[i] and state[j]
                        double tempRe = state[i].re;
                        double tempIm = state[i].im;

                        state[i].re = state[j].re;
                        state[i].im = state[j].im;

                        state[j].re = tempRe;
                        state[j].im = tempIm;
                    }
                }
            }
        }
    }

    /*
     * Kernel Methods
     */

    @Override
    public final void addHadamardGate(int targetQubit) {
        this.gates.add(new HadamardGate(targetQubit));
    }

    @Override
    public final void addCNotGate(int controlQubit, int targetQubit) {
        this.gates.add(new CNotGate(controlQubit, targetQubit));
    }

    @Override
    public final void run() {
        // Apply all gates in the queue to the state vector
        for (Gate gate : this.gates) {
            gate.apply(this.stateVector, this.numQubits);
        }
        // Clear the gate queue as per contract
        this.gates.clear();
    }

    @Override
    public final double[] getProbabilities() {
        double[] probs = new double[this.stateVector.length];
        for (int i = 0; i < this.stateVector.length; i++) {
            probs[i] = this.stateVector[i].magSq();
        }
        return probs;
    }

    @Override
    public final int numQubits() {
        return this.numQubits;
    }

    /*
     * Standard Methods
     */

    @Override
    public final QuantumCircuit newInstance() {
        return new QuantumCircuit1L(this.numQubits);
    }

    @Override
    public final void clear() {
        this.createNewRep(this.numQubits);
    }

    @Override
    public final void transferFrom(QuantumCircuit source) {
        QuantumCircuit1L localSource = (QuantumCircuit1L) source;
        this.numQubits = localSource.numQubits;
        this.stateVector = localSource.stateVector;
        this.gates = localSource.gates;

        localSource.createNewRep(this.numQubits);
    }
}
