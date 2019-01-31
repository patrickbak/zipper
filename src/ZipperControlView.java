import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This is both Controller and View class.
 * View creates components and provides GUI for user.
 * Controller adds listeners to View's components and react to user's actions.
 */

public class ZipperControlView extends JFrame
{
    public ZipperControlView(ZipperModel zipperModel)
    {
        this.zipperModel = zipperModel;
        this.setTitle("Zipper");
        this.setLocationRelativeTo(null);
        this.setJMenuBar(menuBar);

        menuFile.add(adding);
        menuFile.add(deleting);
        menuFile.add(Open);
        menuFile.add(Zip);

        bAdd = new JButton(adding);
        bDelete = new JButton(deleting);
        bZip = new JButton(Zip);
        bOpen = new JButton(Open);

        list = zipperModel.getList();
        JScrollPane scroll = new JScrollPane(list);

        list.setBorder(BorderFactory.createEtchedBorder());

        GroupLayout layout = new GroupLayout(this.getContentPane());

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(scroll, 100, 150, Short.MAX_VALUE)
                        .addContainerGap(0, Short.MAX_VALUE)
                        .addGroup(
                                layout.createParallelGroup().addComponent(bAdd).addComponent(bDelete).addComponent(bOpen).addComponent(bZip)
                        )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addComponent(scroll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(
                                layout.createSequentialGroup().addComponent(bAdd).addComponent(bDelete).addGap(0, 40, Short.MAX_VALUE).addComponent(bOpen).addComponent(bZip)
                        )
        );

        this.getContentPane().setLayout(layout);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.pack();
    }

    private JMenuBar menuBar = new JMenuBar();

    private JMenu menuFile = menuBar.add(new JMenu("File"));
    private JButton bAdd;
    private JButton bDelete;
    private JButton bZip;
    private JButton bOpen;

    private ZipperModel zipperModel;
    private JList list;
    private Action adding = new PerformAction("Add", "Add new entry to archive", "ctrl A", new ImageIcon("green.png"));
    private Action deleting = new PerformAction("Delete", "Delete selected entry from archive", "ctrl D", new ImageIcon("red.png"));
    private Action Zip = new PerformAction("Zip", "Create a ZIP archive", "ctrl Z");
    private Action Open = new PerformAction("Open", "Open a ZIP archive", "ctrl O");

    private class PerformAction extends AbstractAction
    {
        public PerformAction(String name, String description, String keyboardShortcut)
        {
            this.putValue(Action.NAME, name);
            this.putValue(Action.SHORT_DESCRIPTION, description);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(keyboardShortcut));
        }
        public PerformAction(String name, String description, String keyboardShortcut, Icon icon)
        {
            this(name, description, keyboardShortcut);
            this.putValue(Action.SMALL_ICON, icon);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Add"))
            {
                zipperModel.addEntriesToArchive();
            }
            else if (e.getActionCommand().equals("Delete"))
            {
                zipperModel.deletingEntriesFromList();
            }
            else if (e.getActionCommand().equals("Zip"))
            {
                zipperModel.createZipArchive();
            }
            else if (e.getActionCommand().equals("Open"))
            {
                zipperModel.openZip();
            }
        }
    }
}
