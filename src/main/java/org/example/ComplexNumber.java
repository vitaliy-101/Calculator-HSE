package org.example;

public class ComplexNumber {
    private double actualPart;
    private double imaginaryPart;

    public ComplexNumber() {
        actualPart = 0;
        imaginaryPart = 0;
    }

    public ComplexNumber(double actualPart, double imaginaryPart) {
        this.actualPart = actualPart;
        this.imaginaryPart = imaginaryPart;
    }

    public double getActualPart() {
        return actualPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }

    public void setActualPart(double actualPart) {
        this.actualPart = actualPart;
    }

    public void setImaginaryPart(double imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    public ComplexNumber exponentiation(Integer degree) {
        if (degree == 0) {
            return new ComplexNumber(1, 0);
        }
        if (degree > 0) {
            return stepDegree(degree);
        }
        var newNumber = new ComplexNumber(1, 0);
        newNumber.divide(stepDegree(degree * -1));
        return newNumber;
    }

    private ComplexNumber stepDegree(Integer degree) {
        var leftComplexNumber = new ComplexNumber(actualPart, imaginaryPart);
        var rightComplexNumber = new ComplexNumber(actualPart, imaginaryPart);
        for (int i = 0; i < degree - 1; i ++) {
            leftComplexNumber.multiply(rightComplexNumber);
        }
        return leftComplexNumber;
    }

    public void add(ComplexNumber otherNumber) {
        actualPart += otherNumber.getActualPart();
        imaginaryPart += otherNumber.getImaginaryPart();
    }
    public void subtract(ComplexNumber otherNumber) {
        actualPart -= otherNumber.getActualPart();
        imaginaryPart -= otherNumber.getImaginaryPart();
    }

    public void multiply(ComplexNumber otherNumber) {
        double newActualPart = (actualPart * otherNumber.getActualPart()) - (imaginaryPart * otherNumber.getImaginaryPart());
        double newImaginaryPart = (imaginaryPart * otherNumber.getActualPart()) + (actualPart * otherNumber.getImaginaryPart());
        actualPart = newActualPart;
        imaginaryPart = newImaginaryPart;
    }

    public void divide(ComplexNumber otherNumber) {
        double newActualPart = ((actualPart * otherNumber.getActualPart()) + (imaginaryPart * otherNumber.getImaginaryPart())) / ((otherNumber.getActualPart() * otherNumber.getActualPart()) + (otherNumber.getImaginaryPart() * otherNumber.getImaginaryPart()));
        double newImaginaryPart = ((imaginaryPart * otherNumber.getActualPart()) - (actualPart * otherNumber.getImaginaryPart())) / ((otherNumber.getActualPart() * otherNumber.getActualPart()) + (otherNumber.getImaginaryPart() * otherNumber.getImaginaryPart()));
        actualPart = newActualPart;
        imaginaryPart = newImaginaryPart;
    }

    @Override
    public String toString() {
        if (imaginaryPart == 0 && actualPart == 0) {
            return "0";
        }
        else if (imaginaryPart == 0) {
            return String.valueOf(actualPart);
        }
        else if (actualPart == 0) {
            return String.valueOf(imaginaryPart);
        }
        else {
            return "(" + actualPart + "+" + imaginaryPart + "i)";
        }
    }
}
