import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;


public class gui {
    //frame
    private JFrame mainFrame;
    //layout
    private GridBagLayout layout;
    //label
    private JLabel if_label;
    private JLabel pwd_label;
    private JLabel msg_label;
    //button
    private JButton select_file_button;
    private JButton encrypt_button;
    private JButton decrypt_button;
    //textField
    private JTextField file_path_field;
    private JTextField pwd_field;

    public gui(){
        // create window
        mainFrame = new JFrame("FileEncrypter");
        mainFrame.setSize(280, 100);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create panel
        JPanel panel = new JPanel(layout);

        //create layout and add to panel
        layout = new GridBagLayout();
        panel.setLayout(layout);

        //create label
        if_label = new JLabel("Input file:");
        pwd_label = new JLabel("Password:");
        msg_label = new JLabel(" ",JLabel.CENTER);

        //create button
        select_file_button = new JButton("Select");
        encrypt_button = new JButton("Encrypt");
        decrypt_button = new JButton("Decrypt");

        //create textfield
        file_path_field = new JTextField(12);
        pwd_field = new JPasswordField(12);

        //file chooser dialog
        JFileChooser fc = new JFileChooser();

        //set button action listener
        select_file_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int rv = fc.showOpenDialog(mainFrame);
                        if(rv == JFileChooser.APPROVE_OPTION){
                            file_path_field.setText(fc.getSelectedFile().toString());
                        }
                    }
                }
        );

        encrypt_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        long starTime = System.currentTimeMillis();

                        String inFile = file_path_field.getText();
                        String ouFile = inFile.concat(".lld");
                        String pwd = pwd_field.getText();

                        if(inFile.isBlank()){
                            msg_label.setText("Input File Must Not Empty Or Blank.");
                            return;
                        }

                        if(pwd.isBlank()){
                            msg_label.setText("PassWord Must Not Empty Or Blank.");
                            return;
                        }

                        File _if = new File(inFile);
                        if(! _if.canRead()){
                            msg_label.setText("Input File Can Not Read.");
                            return;
                        }
                        try {
                            FileEncrypter.encrypt(inFile,ouFile,pwd);
                        } catch (IOException ioException) {
                            msg_label.setText(ioException.toString());
                            return;
                        }
                        catch (RuntimeException runtimeException){
                            msg_label.setText(runtimeException.getMessage());
                            return;
                        }

                        long endTime = System.currentTimeMillis();

                        float totalTime = endTime - starTime;
                        totalTime /= 1000;

                        msg_label.setText("File Encrypted Success. Using " + Float.toString(totalTime) + " Seconds");
                    }
                }
        );

        decrypt_button.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        long starTime = System.currentTimeMillis();

                        String inFile = file_path_field.getText();
                        File _if = new File(inFile);
                        String ouFile = _if.getParent() + "\\dec_" + _if.getName();
                        ouFile = ouFile.substring(0, ouFile.lastIndexOf("."));
                        
                        String pwd = pwd_field.getText();

                        if(inFile.isBlank()){
                            msg_label.setText("Input File Must Not Empty Or Blank.");
                            return;
                        }

                        if(pwd.isBlank()){
                            msg_label.setText("PassWord Must Not Empty Or Blank.");
                            return;
                        }

                        if(! _if.canRead()){
                            msg_label.setText("Input File Can Not Read.");
                            return;
                        }

                        try {
                            FileEncrypter.decrypt(inFile,ouFile,pwd);
                        } catch (IOException ioException) {
                            msg_label.setText(ioException.toString());
                            return;
                        } catch (RuntimeException runtimeException){
                            msg_label.setText(runtimeException.getMessage());
                            return;
                        }

                        long endTime = System.currentTimeMillis();

                        float totalTime = endTime - starTime;
                        totalTime /= 1000;

                        msg_label.setText("File Decrypted Success. Using " + Float.toString(totalTime) + " Seconds");
                    }
                }
        );

        //add to panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3,3,3,3);

        c.ipady = 3;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(if_label,c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(pwd_label,c);

        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(file_path_field,c);

        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(pwd_field,c);

        c.gridwidth = 1;
        c.gridx = 3;
        c.gridy = 0;
        panel.add(select_file_button,c);

        c.ipady = 60;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(msg_label,c);

        c.gridwidth = 1;
        c.ipady = 0;
        c.gridx = 1;
        c.gridy = 3;
        panel.add(encrypt_button,c);

        c.gridx = 2;
        c.gridy = 3;
        panel.add(decrypt_button,c);

        //add panel to frame
        mainFrame.add(panel);
        // show window
        mainFrame.pack();
        mainFrame.setVisible(true);
    }


    public static void main(String[] args){
        gui g = new gui();
    }
}
