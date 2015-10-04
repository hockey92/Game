package game.engine.myutils;

class MatrixException extends RuntimeException {

    public MatrixException() {
    }

    public MatrixException(String err) {
        super(err);
    }
}

public class Matrix implements Cloneable {
    private float[][] values;
    private int rowCount, columnCount;
    private boolean transposed;

    public Matrix(int row, int column) {
        if (row < 1 || column < 1) {
            throw new MatrixException("Initial matrix sizes is wrong");
        }

        this.rowCount = row;
        this.columnCount = column;
        transposed = false;
        values = new float[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                values[i][j] = 0;
            }
        }
    }

    public Matrix(Matrix matrix) {
        rowCount = matrix.rowCount;
        columnCount = matrix.columnCount;
        transposed = matrix.transposed;
        values = new float[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                values[i][j] = matrix.values[i][j];
            }
        }
    }

    @Override
    public Matrix clone() throws CloneNotSupportedException {
        Matrix matrix = (Matrix) super.clone();
        matrix.values = values.clone();
        return matrix;
    }

    public Matrix transpose() {
        transposed = !transposed;
        return this;
    }

    public int getRowCount() {
        if (transposed) {
            return columnCount;
        } else {
            return rowCount;
        }
    }

    public int getColumnCount() {
        if (transposed) {
            return rowCount;
        } else {
            return columnCount;
        }
    }

    public float get(int row, int column) throws MatrixException {
        if (row < 0 || column < 0 || row >= getRowCount() || column >= getColumnCount()) {
            throw new MatrixException("Element not found");
        }
        if (transposed) {
            return values[column][row];
        } else {
            return values[row][column];
        }
    }

    public void set(int row, int column, float value) throws MatrixException {
        if (row < 0 || column < 0 || row >= getRowCount() || column >= getColumnCount()) {
            throw new MatrixException("Wrong index");
        }
        if (transposed) {
            values[column][row] = value;
        } else {
            values[row][column] = value;
        }
    }

    public static Matrix getRotateMatrix(float angle) {
        Matrix rotateMatrix = new Matrix(2, 2);
        rotateMatrix.set(0, 0, (float) Math.cos(angle));
        rotateMatrix.set(0, 1, (float) -Math.sin(angle));
        rotateMatrix.set(1, 0, (float) Math.sin(angle));
        rotateMatrix.set(1, 1, (float) Math.cos(angle));
        return rotateMatrix;
    }

    public static Matrix mul(Matrix a, Matrix b) throws MatrixException {
        if (a.getColumnCount() != b.getRowCount()) {
            throw new MatrixException("Incorrect matrix sizes");
        }

        Matrix mulMatrix = new Matrix(a.getRowCount(), b.getColumnCount());
        for (int i = 0; i < mulMatrix.getRowCount(); i++) {
            for (int j = 0; j < mulMatrix.getColumnCount(); j++) {
                float value = 0.0f;
                for (int k = 0; k < a.getColumnCount(); k++) {
                    value += a.get(i, k) * b.get(k, j);
                }
                mulMatrix.set(i, j, value);
            }
        }
        return mulMatrix;
    }

    public static Matrix getMul(Matrix a, float c) {
        Matrix multMatrix = new Matrix(a.getRowCount(), a.getColumnCount());
        for (int i = 0; i < multMatrix.getRowCount(); i++) {
            for (int j = 0; j < multMatrix.getColumnCount(); j++) {
                multMatrix.set(i, j, a.get(i, j) * c);
            }
        }
        return multMatrix;
    }

    public Matrix mul(float c) {
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                this.set(i, j, this.get(i, j) * c);
            }
        }
        return this;
    }

    public Matrix mulLikeSum(Matrix m) {
        if (getRowCount() != m.getRowCount() || getColumnCount() != m.getColumnCount()) {
            throw new MatrixException("Incorrect matrix sizes");
        }
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                set(i, j, get(i, j) * m.get(i, j));
            }
        }
        return this;
    }

    public static Matrix getLinearCombination(Matrix a, Matrix b, float c1, float c2) throws MatrixException {
        if (a.getRowCount() != b.getRowCount() || a.getColumnCount() != b.getColumnCount()) {
            throw new MatrixException("Incorrect matrix sizes");
        }
        Matrix linCombMatrix = new Matrix(a.getRowCount(), a.getColumnCount());
        for (int i = 0; i < linCombMatrix.getRowCount(); i++) {
            for (int j = 0; j < linCombMatrix.getColumnCount(); j++) {
                linCombMatrix.set(i, j, a.get(i, j) * c1 + b.get(i, j) * c2);
            }
        }
        return linCombMatrix;
    }

    public Matrix applyLinearCombination(Matrix b, float c1, float c2) throws MatrixException {
        if (this.getRowCount() != b.getRowCount() || this.getColumnCount() != b.getColumnCount()) {
            throw new MatrixException("Incorrect matrix sizes");
        }
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                this.set(i, j, this.get(i, j) * c1 + b.get(i, j) * c2);
            }
        }
        return this;
    }

    public float get(int index) {
        if (!isVector()) {
            throw new MatrixException("It isn't a vector");
        } else if (Math.max(columnCount, rowCount) <= index) {
            throw new MatrixException("Wrong index");
        }

        if (rowCount >= columnCount) {
            return values[index][0];
        } else {
            return values[0][index];
        }
    }

    public void set(int index, float value) {
        if (!isVector()) {
            throw new MatrixException("It isn't a vector");
        } else if (Math.max(columnCount, rowCount) <= index) {
            throw new MatrixException("Wrong index");
        }

        if (rowCount >= columnCount) {
            values[index][0] = value;
        } else {
            values[0][index] = value;
        }
    }

    private boolean isVector() {
        if (getRowCount() == 1 || getColumnCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isCoords() {
        if (isVector() && maxOfSizes() == 2) {
            return true;
        }
        return false;
    }

    public void setCoords(float coord1, float coord2) {
        if (!isCoords()) {
            throw new MatrixException("It isn't coordinates");
        }
        set(0, coord1);
        set(1, coord2);
    }

    public static Matrix createCoords(float coord1, float coord2) {
        Matrix coords = new Matrix(2, 1);
        coords.setCoords(coord1, coord2);
        return coords;
    }

    public int maxOfSizes() {
        return Math.max(columnCount, rowCount);
    }

    public static float getScalarProduct(Matrix a, Matrix b) throws MatrixException {
        if (!a.isVector() || !b.isVector()) {
            throw new MatrixException("One of participants isn't a vector");
        } else if (a.maxOfSizes() != b.maxOfSizes()) {
            throw new MatrixException("Incorrect vector sizes");
        }

        float scalarProduct = 0f;
        for (int i = 0; i < a.maxOfSizes(); i++) {
            scalarProduct += a.get(i) * b.get(i);
        }

        return scalarProduct;
    }

    public static Matrix getIdentityMatrix(int size) {
        Matrix identityMatrix = new Matrix(size, size);
        for (int i = 0; i < identityMatrix.getRowCount(); i++) {
            for (int j = 0; j < identityMatrix.getColumnCount(); j++) {
                if (i == j) {
                    identityMatrix.set(i, j, 1.0f);
                }
            }
        }
        return identityMatrix;
    }

    public Matrix getSubMatrix(int x1, int y1, int x2, int y2) {
        return null;
    }

    @Override
    public String toString() {
        String matrixOut = "";
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                matrixOut += this.get(i, j) + " ";
            }
            matrixOut += "\n";
        }
        return matrixOut;
    }
}
