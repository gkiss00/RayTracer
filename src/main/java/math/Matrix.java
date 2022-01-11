package math;

public class Matrix {
    private double[][] vectors;

    public Matrix(int cols, int rows) throws Exception{
        if (cols <= 0 || rows <= 0)
            throw new Exception("Can not create a matrix with 0 cols and 0 rows");
        this.vectors = new double[cols][rows];
    }

    public Matrix(Matrix matrix) {
        copyMatrix(matrix);
    }

    private void copyMatrix(Matrix matrix) {
        this.vectors = new double[matrix.getNbCols()][matrix.getNbRows()];
        for (int i = 0; i < getNbCols(); ++i) {
            for (int j = 0; j < getNbRows(); ++j) {
                vectors[i][j] = matrix.vectors[i][j];
            }
        }
    }

    public int getNbCols() {
        return vectors.length;
    }

    public int getNbRows() {
        return vectors[0].length;
    }

    private boolean isSquare() {
        return getNbCols() == getNbRows();
    }

    public double get(int col, int row) throws Exception{
        if (col < 0 || col >= getNbCols() || row < 0 || row >= getNbRows())
                throw new Exception("Index out of bound: can not get the element of the matrix");
        return vectors[col][row];
    }

    public void set(int col, int row, double value) throws Exception{
        if (col < 0 || col >= getNbCols() || row < 0 || row >= getNbRows())
            throw new Exception("Index out of bound: can not set the element of the matrix");
        this.vectors[col][row] = value;
    }

    public double getDeterminant() throws Exception{
        if (isSquare() == false)
            throw new Exception("Matrix must be squared to get the determinant");

        int n = getNbCols();
        if(n == 0)
            return 0;
        else if (n == 1)
            return vectors[0][0];
        else if (n == 2) {
            return (vectors[0][0] * vectors[1][1]) - (vectors[0][1] * vectors[1][0]);
        } else {
            double determinant = 0;
            for (int i = 0; i < n; ++ i) {
                double sign = (i % 2 == 0 ? (1) : (-1));
                Matrix subMatrix = getSubMatrixRowed(i);
                determinant += sign * subMatrix.getDeterminant() * vectors[0][i];
            }
            return determinant;
        }
    }

    private Matrix getSubMatrixRowed(int n) throws Exception{
        Matrix m = new Matrix(getNbCols() - 1, getNbRows() - 1);

        for (int i = 1; i < getNbCols(); ++i) {
            for (int j = 0; j < getNbRows(); ++j) {
                if (j < n)
                    m.vectors[i - 1][j] = vectors[i][j];
                else if (j == n) {

                } else {
                    m.vectors[i - 1][j - 1] = vectors[i][j];
                }
            }
        }
        return m;
    }

    private Matrix getSubMatrix(int c, int r) throws Exception{
        Matrix m = new Matrix(getNbCols() - 1, getNbRows() - 1);

        int c_offset = 0;
        int r_offset = 0;
        for (int i = 0; i < getNbCols(); ++i) {
            for (int j = 0; j < getNbRows(); ++j) {
                c_offset = i < c ? (0) : (-1);
                r_offset = j < r ? (0) : (-1);
                if (i != c && j != r)
                    m.vectors[i + c_offset][j + r_offset] = vectors[i][j];
            }
        }
        return m;
    }

    public void unit() throws Exception{
        if (getNbCols() != getNbRows())
            throw new Exception("Matrix can not be unitary");
        for (int i = 0; i < vectors.length; ++i) {
            for (int j = 0; j < vectors.length; ++j) {
                vectors[i][j] = i == j ? (1) : (0);
            }
        }
    }

    public void transpose() throws Exception{
        Matrix m = new Matrix(getNbRows(), getNbCols());

        for (int i = 0; i < getNbCols(); ++i) {
            for (int j = 0; j < getNbRows(); ++j) {
                m.vectors[j][i] = vectors[i][j];
            }
        }
        copyMatrix(m);
    }

    public void adjugate() throws Exception{
        if (getNbCols() != getNbRows())
            throw new Exception("Matrix can not be adjugate");

        Matrix m = new Matrix(getNbCols(), getNbRows());

        for (int i = 0; i < getNbCols(); ++i) {
            for (int j = 0; j < getNbRows(); ++j) {
                double sign = (i + j) % 2 == 0 ? (1) : (-1);
                Matrix subMatrix = getSubMatrix(i, j);
                m.vectors[i][j] = sign * subMatrix.getDeterminant();
            }
        }
        m.transpose();
        copyMatrix(m);
    }

    public void inverse() throws Exception{
        double determinant = getDeterminant();
        if (determinant == 0)
            throw new Exception("Matrix can not be reverse");

        Matrix m1 = new Matrix(this);
        m1.adjugate();
        for (int i = 0; i < getNbCols(); ++i) {
            for (int j = 0; j < getNbRows(); ++j) {
                this.vectors[i][j] = (1.0 / determinant) * m1.vectors[i][j];
            }
        }
    }

    public static Matrix add(Matrix matrix1, Matrix matrix2) throws Exception {
        if (matrix1.getNbCols() != matrix2.getNbCols() || matrix1.getNbRows() != matrix2.getNbRows())
            throw new Exception("Addition operator can not be apply on 2 different matrices");

        Matrix m = new Matrix(matrix1.getNbCols(), matrix1.getNbRows());
        for (int i = 0; i < matrix1.getNbCols(); ++i) {
            for (int j = 0; j < matrix1.getNbRows(); ++i) {
                m.vectors[i][j] = matrix1.vectors[i][j] + matrix2.vectors[i][j];
            }
        }
        return m;
    }

    public static Matrix sub(Matrix matrix1, Matrix matrix2) throws Exception {
        if (matrix1.getNbCols() != matrix2.getNbCols() || matrix1.getNbRows() != matrix2.getNbRows())
            throw new Exception("Addition operator can not be apply on 2 different matrices");

        Matrix m = new Matrix(matrix1.getNbCols(), matrix1.getNbRows());
        for (int i = 0; i < matrix1.getNbCols(); ++i) {
            for (int j = 0; j < matrix1.getNbRows(); ++i) {
                m.vectors[i][j] = matrix1.vectors[i][j] - matrix2.vectors[i][j];
            }
        }
        return m;
    }

//    public static Matrix mult(Matrix matrix1, double... matrix2) throws Exception{
//        int cols = matrix2.length;
//        int rows = matrix1.getNbRows();
//        Matrix result = new Matrix(cols, rows);
//        for (int i = 0; i < result.getNbCols(); ++i) {
//            for (int j = 0; j < result.getNbRows(); ++j) {
//                for (int x = 0; x < matrix1.getNbCols(); ++x) {
//                    result.vectors[i][j] += matrix1.vectors[x][j] * matrix2[x];
//                }
//            }
//        }
//        return result;
//    }

    public static Matrix mult(Matrix matrix1, Matrix matrix2) throws Exception{
        int cols = matrix2.getNbCols();
        int rows = matrix1.getNbRows();
        Matrix result = new Matrix(cols, rows);
        for (int i = 0; i < result.getNbCols(); ++i) {
            for (int j = 0; j < result.getNbRows(); ++j) {
                for (int x = 0; x < matrix1.getNbCols(); ++x) {
                    result.vectors[i][j] += matrix1.vectors[x][j] * matrix2.vectors[i][x];
                }
            }
        }
        return result;
    }

    public static Matrix multMany(Matrix... matrices) throws Exception{
        Matrix result = new Matrix(matrices[0]);
        for (int i = 1; i < matrices.length; ++i) {
            result = Matrix.mult(result, matrices[i]);
        }
        return result;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < getNbCols(); ++i) {
            for (int j = 0; j < getNbRows(); ++j) {
                str += String.format("|%4f| ", vectors[i][j]);
            }
            str += "\n";
        }
        return str;
    }
}
