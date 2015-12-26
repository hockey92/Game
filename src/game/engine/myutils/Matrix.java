package game.engine.myutils;

class MatrixException extends RuntimeException {

    public MatrixException() {
    }

    public MatrixException(String err) {
        super(err);
    }
}

public class Matrix implements Cloneable, IVector {
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
    }

    public Matrix(Matrix matrix) {
        rowCount = matrix.rowCount;
        columnCount = matrix.columnCount;
        transposed = matrix.transposed;
        values = new float[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            System.arraycopy(matrix.values[i], 0, values[i], 0, columnCount);
        }
    }

    @Override
    public Matrix clone() throws CloneNotSupportedException {
        Matrix matrix = (Matrix) super.clone();
        matrix.values = values.clone();
        return matrix;
    }

    public Matrix transpose() {
        Matrix transposedMatrix = new Matrix(columnCount, rowCount);

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                transposedMatrix.values[j][i] = values[i][j];
            }
        }
        return transposedMatrix;
    }

    public int rowCount() {
        return rowCount;
    }

    public int columnCount() {
        return columnCount;
    }

    public float get(int row, int column) throws MatrixException {
        if (row < 0 || column < 0 || row >= rowCount || column >= columnCount) {
            throw new MatrixException("Element not found");
        }
        return values[row][column];
    }

    public void set(int row, int column, float value) throws MatrixException {
        if (row < 0 || column < 0 || row >= rowCount || column >= columnCount) {
            throw new MatrixException("Wrong index");
        }
        values[row][column] = value;
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
        if (a.columnCount != b.rowCount) {
            throw new MatrixException("Incorrect matrix sizes");
        }

        Matrix mulMatrix = new Matrix(a.rowCount, b.columnCount);
        for (int i = 0; i < mulMatrix.rowCount; i++) {
            for (int j = 0; j < mulMatrix.columnCount; j++) {
                float value = 0.0f;
                for (int k = 0; k < a.columnCount; k++) {
                    value += a.values[i][k] * b.values[k][j];
                }
                mulMatrix.values[i][j] = value;
            }
        }
        return mulMatrix;
    }

    public Matrix mulEq(float c) {
        Matrix multMatrix = new Matrix(rowCount, columnCount);
        for (int i = 0; i < multMatrix.rowCount; i++) {
            for (int j = 0; j < multMatrix.columnCount; j++) {
                multMatrix.values[i][j] = values[i][j] * c;
            }
        }
        return multMatrix;
    }

    //use mulEguals(float) instead
    @Deprecated
    public static Matrix createMul(Matrix a, float c) {
        Matrix multMatrix = new Matrix(a.rowCount, a.columnCount);
        for (int i = 0; i < multMatrix.rowCount; i++) {
            for (int j = 0; j < multMatrix.columnCount; j++) {
                multMatrix.values[i][j] = a.values[i][j] * c;
            }
        }
        return multMatrix;
    }

    public Matrix mul(float c) {
        for (int i = 0; i < this.rowCount; i++) {
            for (int j = 0; j < this.columnCount; j++) {
                values[i][j] *= c;
            }
        }
        return this;
    }

    public Matrix plus(Matrix m) {
        return applyLinComb(m, 1f, 1f);
    }

    public Matrix plusEq(Matrix m) {
        return getLinComb(this, m, 1f, 1f);
    }

    public Matrix minus(Matrix m) {
        return applyLinComb(m, 1f, -1f);
    }

    public Matrix minusEq(Matrix m) {
        return getLinComb(this, m, 1f, -1f);
    }

    public Matrix mulLikeSum(Matrix m) {
        if (rowCount != m.rowCount || columnCount != m.columnCount) {
            throw new MatrixException("Incorrect matrix size");
        }
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                values[i][j] *= m.values[i][j];
            }
        }
        return this;
    }

    public static Matrix getLinComb(Matrix a, Matrix b, float c1, float c2) throws MatrixException {
        if (a.rowCount != b.rowCount || a.columnCount != b.columnCount) {
            throw new MatrixException("Incorrect matrix sizes");
        }
        Matrix linCombMatrix = new Matrix(a.rowCount, a.columnCount);
        for (int i = 0; i < linCombMatrix.rowCount; i++) {
            for (int j = 0; j < linCombMatrix.columnCount; j++) {
                linCombMatrix.values[i][j] = a.values[i][j] * c1 + b.values[i][j] * c2;
            }
        }
        return linCombMatrix;
    }

    public Matrix applyLinComb(Matrix b, float c1, float c2) throws MatrixException {
        if (rowCount != b.rowCount || columnCount != b.columnCount) {
            throw new MatrixException("Incorrect matrix sizes");
        }
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                values[i][j] = values[i][j] * c1 + b.values[i][j] * c2;
            }
        }
        return this;
    }

    @Override
    public float get(int index) {
        if (!isVector()) {
            throw new MatrixException("It isn't a vector");
        } else if (maxOfSizes() <= index) {
            throw new MatrixException("Wrong index");
        }

        if (rowCount >= columnCount) {
            return values[index][0];
        } else {
            return values[0][index];
        }
    }

    public Matrix set(int index, float value) {
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

        return this;
    }

    private boolean isVector() {
        return rowCount == 1 || columnCount == 1;
    }

    private boolean isCoords() {
        return isVector() && maxOfSizes() == 2;
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

    public static Matrix createIdentityMatrix(int size) {
        Matrix identityMatrix = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            identityMatrix.values[i][i] = 1.0f;
        }
        return identityMatrix;
    }

    public Matrix getSubMatrix(int x1, int y1, int x2, int y2) {
        return null;
    }

//    public static float getCrossProduct(Matrix v1, Matrix v2) {
//        if (!v1.isCoords() || !v2.isCoords()) {
//            throw new MatrixException("A cross product participant isn't correct");
//        }
//        return v1.get(0) * v2.get(1) - v2.get(0) * v1.get(1);
//    }
//
//    public static Matrix getCrossProduct(Matrix v1, float v2) {
//        if (!v1.isCoords()) {
//            throw new MatrixException("A cross product participance isn't correct");
//        }
//        return Matrix.createCoords(v1.get(1) * v2, v1.get(0) * v2);
//    }
//
//    public static Matrix getCrossProduct(float v1, Matrix v2) {
//        if (!v2.isCoords()) {
//            throw new MatrixException("A cross product participance isn't correct");
//        }
//        return Matrix.createCoords(-v2.get(1) * v1, -v2.get(0) * v1);
//    }

    public static Matrix getCrossProduct(Matrix v1, Matrix v2) {
        Matrix crossProduct = new Matrix(1, 3);
        crossProduct.values[0][0] = v1.get(1) * v2.get(2) - v1.get(2) * v2.get(1);
        crossProduct.values[0][1] = v1.get(0) * v2.get(2) - v1.get(2) * v2.get(0);
        crossProduct.values[0][2] = v1.get(0) * v2.get(1) - v1.get(1) * v2.get(0);
        return crossProduct;
    }

    public float length() {
        if (!isVector()) {
            throw new MatrixException("Matrix isn't vector");
        }
        float answ = 0f;
        for (int i = 0; i < maxOfSizes(); i++) {
            answ += get(i) * get(i);
        }
        return (float) Math.sqrt(answ);
    }

    public static Matrix convert(Matrix init) {
        if (!init.isCoords()) {
            throw new MatrixException("Matrix isn't coords");
        }
        Matrix converted = new Matrix(1, 3);
        for (int i = 0; i < 2; i++) {
            converted.set(i, init.get(i));
        }
        return converted;
    }

    public Matrix setValues(float[][] values) {
        for (int i = 0; i < values.length; i++) {
            if (i > rowCount) {
                break;
            }
            for (int j = 0; j < values[i].length; j++) {
                if (j > columnCount) {
                    break;
                }
                set(i, j, values[i][j]);
            }
        }
        return this;
    }

    public float getVectorLength() {
        if (!isVector()) {
            throw new MatrixException("It isn't a vector");
        }
        float result = 0f;
        for (int i = 0; i < maxOfSizes(); i++) {
            result += get(i);
        }
        return (float) Math.sqrt(result);
    }

    public Matrix setMainDiag(float[] mainDiag) {
        if (columnCount != rowCount || columnCount != mainDiag.length) {
            throw new MatrixException("Incorrect size");
        }
        for (int i = 0; i < mainDiag.length; i++) {
            values[i][i] = mainDiag[i];
        }
        return this;
    }

//    public static Matrix convertPolarCoords(float angle, float r) {
//        return Matrix.createCoords(
//                r * (float) Math.cos(angle),
//                r * (float) Math.sin(angle)
//        );
//    }
//
//    public static Matrix convertPolarCoords(PolarCoords polarCoords) {
//        return convertPolarCoords(polarCoords.getAngle(), polarCoords.getR());
//    }

    public Matrix getInv() {
        if (rowCount != columnCount || rowCount > 2) {
            throw new MatrixException("We can't get an inverse matrix with this dimension.");
        }

        Matrix m = null;

        if (rowCount == 1) {
            m = new Matrix(1, 1);
            m.values[0][0] = 1f / values[0][0];
        } else if (rowCount == 2) {
            float d = values[0][0] * values[1][1] - values[0][1] * values[1][0];
            if (d == 0) {
                throw new MatrixException("A determinant of a 2x2 matrix (" + toString() + ") is zero. We can't find an inverse matrix");
            }
            m = new Matrix(2, 2);
            m.values[0][0] = values[1][1];
            m.values[1][1] = values[0][0];
            m.values[0][1] = -values[0][1];
            m.values[1][0] = -values[1][0];
            m.mul(1f / d);
        }

        return m;
    }

    @Override
    public String toString() {
        String matrixOut = "";
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                matrixOut += values[i][j] + " ";
            }
            matrixOut += "\n";
        }
        return matrixOut;
    }
}
