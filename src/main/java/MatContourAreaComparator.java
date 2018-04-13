import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.Comparator;

public class MatContourAreaComparator implements Comparator<Mat> {
    @Override
    public int compare(Mat mat1, Mat mat2) {
        return (int) (Imgproc.contourArea(mat1) - Imgproc.contourArea(mat2));
    }
}
