import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * This is Model class.
 * All application logic is coded here.
 */

public class ZipperModel extends JFrame
{
    private DefaultListModel listModel = new DefaultListModel(){
        @Override
        public Object get(int index) {
            return list.get(index);
        }
        @Override
        public void addElement(Object obj) {
            list.add(obj);
            if (obj.getClass() == ZipEntry.class)
                super.addElement(((ZipEntry)obj).getName());
            else
                super.addElement(((File)obj).getName());
        }
        @Override
        public Object remove(int index) {
            list.remove(index);
            return super.remove(index);
        }

        ArrayList list = new ArrayList();
    };

    public void addEntriesToArchive()
    {
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jFileChooser.setMultiSelectionEnabled(true);

        int tmp = jFileChooser.showDialog(getParent(), "Add to archive");

        if (tmp == JFileChooser.APPROVE_OPTION)
        {
            File[] paths = jFileChooser.getSelectedFiles();

            for (int i = 0; i < paths.length; i++)
            {
                if (!isEntryRepeated(paths[i].getPath()))
                    listModel.addElement(paths[i]);
            }
        }
    }

    public boolean isEntryRepeated(String testedEntry)
    {
        for (int i = 0; i < listModel.getSize(); i++)
        {
            if (listModel.get(i).getClass() == File.class)
                if (((File) listModel.get(i)).getPath().equals(testedEntry))
                    return true;
            if (listModel.get(i).getClass() == ZipEntry.class)
                if (((ZipEntry) listModel.get(i)).getName().equals(testedEntry))
                    return true;
        }
        return false;
    }

    public void deletingEntriesFromList()
    {
        int[] tmp = list.getSelectedIndices();

        for (int i = 0; i < tmp.length; i++)
            listModel.remove(tmp[i] - i);
    }

    public void createZipArchive() {
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jFileChooser.setSelectedFile(zipFile);
        tmp = jFileChooser.showDialog(getParent(), "Compress");
        jFileChooser.cancelSelection();

        if (tmp == JFileChooser.APPROVE_OPTION)
        {
            byte[] tmpData = new byte[BUFFOR];
            try
            {
                ZipOutputStream zOutS = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(jFileChooser.getSelectedFile()), BUFFOR));

                for (int i = 0; i < listModel.getSize(); i++)
                {
                    if (!((File) listModel.get(i)).isDirectory())
                        createZip(zOutS, (File) listModel.get(i), tmpData, ((File) listModel.get(i)).getPath());
                    else
                    {
                        writePaths((File) listModel.get(i), false);

                        for (int j = 0; j < listOfPaths.size(); j++)
                            createZip(zOutS, (File) listOfPaths.get(j), tmpData, ((File) listModel.get(i)).getPath());
                    }

                    listOfPaths.removeAll(listOfPaths);
                }
                zOutS.close();
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    private void createZip(ZipOutputStream zOutS, File filePath, byte[] tmpData, String baseDirectory) throws IOException
    {
        BufferedInputStream inS = new BufferedInputStream(new FileInputStream(filePath), BUFFOR);

        zOutS.putNextEntry(new ZipEntry(filePath.getPath().substring(baseDirectory.lastIndexOf(File.separator) + 1)));

        int counter;

        while ((counter = inS.read(tmpData, 0, BUFFOR)) != -1)
            zOutS.write(tmpData, 0, counter);

        zOutS.closeEntry();

        inS.close();
    }

    public static final int BUFFOR = 1024;

    private void writePaths(File pathName, boolean zipOnly)
    {
        String[] filesAndDirectoryNames = pathName.list();

        if (zipOnly)
            jFileChooser.setFileFilter(zipFilter);
        else
            jFileChooser.setFileFilter(defaultFilter);

        for (int i = 0; i < filesAndDirectoryNames.length; i++)
        {
            File p = new File(pathName.getPath(), filesAndDirectoryNames[i]);

            if (p.isFile())
                listOfPaths.add(p);

            if (p.isDirectory())
                writePaths(new File(p.getPath()), zipOnly);
        }
    }

    public void openZip()
    {
        jFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jFileChooser.setMultiSelectionEnabled(false);
        jFileChooser.setFileFilter(zipFilter);

        tmp = jFileChooser.showOpenDialog(getParent());
        jFileChooser.cancelSelection();
        File path = jFileChooser.getSelectedFile();

        if (tmp == JFileChooser.APPROVE_OPTION)
        {
            listModel.removeAllElements();
            try {
                ZipFile zipFile = new ZipFile(path);
                Enumeration entries;
                for (entries = zipFile.entries(); entries.hasMoreElements();) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    if (!isEntryRepeated(entry.getName()))
                        listModel.addElement(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int tmp;
    private final JFileChooser jFileChooser = new JFileChooser();
    private JList list = new JList(listModel);
    private ArrayList listOfPaths = new ArrayList();
    private File zipFile = new File(System.getProperty("user.dir") + File.separator + "myname.zip");

    private FileFilter zipFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".zip");
        }

        @Override
        public String getDescription() {
            return "Zip Archive";
        }
    };
    private FileFilter defaultFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return false;
        }

        @Override
        public String getDescription() {
            return "All files";
        }
    };

    public JList getList() {
        return list;
    }
}
