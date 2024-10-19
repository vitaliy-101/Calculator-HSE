package org.example;

import org.example.exceptions.MatrixOperationException;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private List<List<ComplexNumber>> matrix;
    private int columnsSize;
    private int rowsSize;

    public Matrix() {
        this.matrix = new ArrayList<>();
        this.columnsSize = 0;
        this.rowsSize = 0;
    }

    public Matrix(int rowsSize, int columnsSize) {
        this.columnsSize = columnsSize;
        this.rowsSize = rowsSize;
        this.matrix = new ArrayList<>();
        initMatrixBySize(rowsSize, columnsSize);
    }

    public Matrix(int rowsSize, int columnsSize, List<List<ComplexNumber>> matrix) {
        this.columnsSize = columnsSize;
        this.rowsSize = rowsSize;
        this.matrix = matrix;
    }

    public ComplexNumber getMatrixElement(int rowsSize, int columnsSize) {
        return matrix.get(rowsSize).get(columnsSize);
    }
    public void addRow(List<ComplexNumber> row) {
        matrix.add(row);
    }

    public int getColumnsSize() {
        return columnsSize;
    }

    public void setColumnsSize(int columnsSize) {
        this.columnsSize = columnsSize;
    }

    public int getRowsSize() {
        return rowsSize;
    }

    public void setRowsSize(int rowsSize) {
        this.rowsSize = rowsSize;
    }

    public void initMatrixBySize(int rowsSize, int columnsSize) {
        for (int i = 0; i < rowsSize; i ++) {
            var row = new ArrayList<ComplexNumber>();
            for (int j = 0; j < columnsSize; j ++) {
                row.add(new ComplexNumber());
            }
            matrix.add(row);
        }
    }

    public void addMatrix(Matrix otherMatrix) throws MatrixOperationException {
        validSizeMatrix(otherMatrix);
        for (int rowIndex = 0; rowIndex < rowsSize; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columnsSize; columnIndex++) {
                getMatrixElement(rowIndex, columnIndex).add(otherMatrix.getMatrixElement(rowIndex, columnIndex));
            }
        }
    }

    public void subtractMatrix(Matrix otherMatrix) throws MatrixOperationException {
        validSizeMatrix(otherMatrix);
        for (int rowIndex = 0; rowIndex < rowsSize; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columnsSize; columnIndex++) {
                getMatrixElement(rowIndex, columnIndex).subtract(otherMatrix.getMatrixElement(rowIndex, columnIndex));
            }
        }
    }

    public Matrix multiplyMatrix(Matrix otherMatrix) throws MatrixOperationException {
        validateMultiplyForMatrix(otherMatrix);
        var newMatrix = new Matrix(rowsSize, otherMatrix.getColumnsSize());
        for (int i = 0; i < newMatrix.getRowsSize(); i++) {
            for (int j = 0; j < newMatrix.getColumnsSize(); j++) {
                for (int k = 0; k < columnsSize; k++) {
                    var newNumber = new ComplexNumber(getMatrixElement(i, k).getActualPart(), getMatrixElement(i, k).getImaginaryPart());
                    newNumber.multiply(otherMatrix.getMatrixElement(k, j));
                    newMatrix.getMatrixElement(i, j).add(newNumber);
                }
            }
        }
        return newMatrix;
    }

    private void validateMultiplyForMatrix(Matrix otherMatrix) throws MatrixOperationException {
        if (columnsSize != otherMatrix.getRowsSize()) {
            throw new MatrixOperationException("The matrix sizes are not equal during the operation!");
        }
    }

    public void addNumber(ComplexNumber complexNumber) {
        for (int rowIndex = 0; rowIndex < rowsSize; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columnsSize; columnIndex++) {
                getMatrixElement(rowIndex, columnIndex).add(complexNumber);
            }
        }
    }

    public void subtractNumber(ComplexNumber complexNumber) {
        for (int rowIndex = 0; rowIndex < rowsSize; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columnsSize; columnIndex++) {
                getMatrixElement(rowIndex, columnIndex).subtract(complexNumber);
            }
        }
    }

    public void multiplyNumber(ComplexNumber complexNumber) {
        for (int rowIndex = 0; rowIndex < rowsSize; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columnsSize; columnIndex++) {
                getMatrixElement(rowIndex, columnIndex).multiply(complexNumber);
            }
        }
    }

    public void divideNumber(ComplexNumber complexNumber) {
        for (int rowIndex = 0; rowIndex < rowsSize; rowIndex++) {
            for (int columnIndex = 0; columnIndex < columnsSize; columnIndex++) {
                getMatrixElement(rowIndex, columnIndex).divide(complexNumber);
            }
        }
    }

    public void exponentiationMatrix(Integer degree) throws MatrixOperationException {
        if (degree <= 1) {
            throw new MatrixOperationException("Degree for matrix can be only more one");
        }
        stepDegreeMatrix(degree);
    }

    private void stepDegreeMatrix(Integer degree) throws MatrixOperationException {
        var initMatrix = new Matrix(rowsSize, columnsSize, matrix);
        for (int i = 0; i < degree - 1; i ++) {
            multiplyMatrix(initMatrix);
        }
    }

    public ComplexNumber getDeterminant() throws MatrixOperationException {
        validateMatrixForDeterminant();
        return calculateDeterminant(this);
    }

    public void validateMatrixForDeterminant() throws MatrixOperationException {
        if (rowsSize != columnsSize) {
            throw new MatrixOperationException("The determinant can only be computed for square matrices.");
        }
    }

    private ComplexNumber calculateDeterminant(Matrix matrix) {
        int rowsSize = matrix.rowsSize;
        if (rowsSize == 1) {
            return matrix.getMatrixElement(0, 0);
        }
        if (rowsSize == 2) {
            matrix.getMatrixElement(0, 0).multiply(matrix.getMatrixElement(1, 1));
            matrix.getMatrixElement(0, 1).multiply(matrix.getMatrixElement(1, 0));
            matrix.getMatrixElement(0, 0).subtract(matrix.getMatrixElement(0, 1));
            return matrix.getMatrixElement(0, 0);
        }

        ComplexNumber det = new ComplexNumber();
        for (int j = 0; j < rowsSize; j++) {
            List<List<ComplexNumber>> newMat = new ArrayList<>();
            for (int k = 1; k < rowsSize; k++) {
                List<ComplexNumber> newRow = new ArrayList<>();
                for (int l = 0; l < rowsSize; l++) {
                    if (l != j) {
                        newRow.add(matrix.getMatrixElement(k, l));
                    }
                }
                newMat.add(newRow);
            }
            var subMatrix = new Matrix(rowsSize - 1, rowsSize - 1, newMat);
            // Учитываем знак из разложения по строке
            var addComplexNumber = new ComplexNumber(j % 2 == 0 ? 1 : -1, 0);
            matrix.getMatrixElement(0, j).multiply(calculateDeterminant(subMatrix));
            addComplexNumber.multiply(matrix.getMatrixElement(0, j));
            det.add(addComplexNumber);
        }
        return det;
    }

    public Matrix transpose() {
        var newMatrix = new Matrix(rowsSize, columnsSize);
        for (int i = 0; i < columnsSize; i++) {
            for (int j = 0; j < rowsSize; j++) {
                newMatrix.getMatrixElement(i, j).add(getMatrixElement(j, i));
            }
        }
        return newMatrix;
    }

    private void validSizeMatrix(Matrix otherMatrix) throws MatrixOperationException {
        if (!(columnsSize == otherMatrix.getColumnsSize() && rowsSize == otherMatrix.getRowsSize())) {
            throw new MatrixOperationException("The matrix sizes are not equal during the operation!");
        }
    }

    @Override
    public String toString() {
        var stringMatrix = new StringBuilder();
        for (int i = 0; i < rowsSize; i++) {
            stringMatrix.append(i).append(": ");
            for (int j = 0; j < columnsSize; j++) {
                stringMatrix.append(getMatrixElement(i, j).toString()).append(" ");
            }
            stringMatrix.append("\n");
        }
        return stringMatrix.toString();
    }
}
