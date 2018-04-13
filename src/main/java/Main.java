import com.sun.imageio.plugins.common.ImageUtil;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.rmi.CORBA.Util;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Map dictonary = new HashMap<ArrayList<Integer>, Integer>();

        dictonary.put(Arrays.asList(1, 1, 1, 0, 1, 1, 1), 0);
        dictonary.put(Arrays.asList(0, 0, 1, 0, 0, 1, 0), 1);
        dictonary.put(Arrays.asList(1, 0, 1, 1, 1, 1, 0), 2);
        dictonary.put(Arrays.asList(1, 0, 1, 1, 0, 1, 1), 3);
        dictonary.put(Arrays.asList(0, 1, 1, 1, 0, 1, 0), 4);
        dictonary.put(Arrays.asList(1, 1, 0, 1, 0, 1, 1), 5);
        dictonary.put(Arrays.asList(1, 1, 0, 1, 1, 1, 1), 6);
        dictonary.put(Arrays.asList(1, 0, 1, 0, 0, 1, 0), 7);
        dictonary.put(Arrays.asList(1, 1, 1, 1, 1, 1, 1), 8);
        dictonary.put(Arrays.asList(1, 1, 1, 1, 0, 1, 1), 9);

        String filename = (Main.class.getResource("example.jpg").getPath()
                .replaceFirst("/", "")
                .replace("/", "\\\\\\")
        );

        Mat mat = Imgcodecs.imread(filename,  Imgcodecs.IMREAD_COLOR);

        Mat resizedMat = mat.clone();
//        Imgproc.resize(mat, resizedMat, new Size(mat.width() / (mat.height() / 500), 500));

        Mat greyMat = new Mat();
        Imgproc.cvtColor(resizedMat, greyMat, Imgproc.COLOR_BGR2GRAY);

        Mat blurredMat = new Mat();
        Imgproc.GaussianBlur(greyMat, blurredMat, new Size(5, 5), 0);

        Mat edgedMat = new Mat();
        Imgproc.Canny(blurredMat, edgedMat, 50, 255);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edgedMat.clone(), contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        contours.sort(new MatContourAreaComparator());
        Collections.reverse(contours);

        Mat finalMat = new Mat();

        for (MatOfPoint matOfPoint : contours) {
            MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
            matOfPoint.convertTo(matOfPoint2f, CvType.CV_32F);

            double perimeter = Imgproc.arcLength(matOfPoint2f, true);

            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(matOfPoint2f, approx, 0.02 * perimeter, true);

            Mat t = new Mat();
            approx.convertTo(t, CvType.CV_32F);

            if (approx.size().height == 4) {
                finalMat = approx;
                break;
            }
        }

        Mat final2Mat = new Mat();
        Imgproc.warpPerspective(finalMat, final2Mat, finalMat.reshape(4, 2), new Size());

        Imgcodecs.imwrite("C:\\Users\\dknik\\Desktop\\rendered.jpg", final2Mat);
    }

    private static Mat fourPointTransform(Mat image, List<MatOfPoint> points) {
        return new Mat();
    }
}
