public class Complex {
    final private double real;
    final private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    } 

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imaginary + other.imaginary);
    }

    public Complex add(double other) {
        return new Complex(this.real + other, this.imaginary);
    }

    public double abs() {
        return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
    }

    public Complex multiply(Complex other) {
        return new Complex(
            this.real * other.real - this.imaginary * other.imaginary,
            this.real * other.imaginary + this.imaginary * other.real
        );
    }
    
    public Complex power(int exponent) {
        Complex result = new Complex(1, 0);
        for (int i = 0; i < exponent; i++) {
            result = result.multiply(this);
        }        
        return result;
    }
}