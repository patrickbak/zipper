/**
 * This is class that contains Main method. It runs this application.
 */

public class Zipper {

    public static void main(String[] args)
    {
        ZipperModel zipperModel = new ZipperModel();
        ZipperControlView zipperControlView = new ZipperControlView(zipperModel);

        zipperControlView.setVisible(true);
    }
}
