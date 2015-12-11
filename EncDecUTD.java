
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import Decode.DecodeFile;
import Encode.EncodeFile;

public class EncDecUTD {

	private JFrame frame;
	private JTextField sourceFile;
	private JPasswordField passwordField;
	private JTextField destinationFile;
	private JTextField encodedFilePath;
	private JTextField decodedFilePath;
	private JPasswordField passwordField_1;
	private JTextField ivFilePath;
	private JTextField saltFilePath;
	private JTextField ACCESS_TOKEN;
	private JTextField hashFiledestiantion;
	private JTextField hashFileSource;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EncDecUTD window = new EncDecUTD();
					// set the window frame visible
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EncDecUTD() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1153, 736);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new FormLayout(
				new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, }));

		// Label for Dropbox token
		JLabel lblDropboxToken = new JLabel("Dropbox Token");
		frame.getContentPane().add(lblDropboxToken, "2, 2, right, default");

		// Text box for Dropbox token
		ACCESS_TOKEN = new JTextField();
		frame.getContentPane().add(ACCESS_TOKEN, "4, 2, fill, default");
		ACCESS_TOKEN.setColumns(10);

		// Label for Specifying the source path of the file to be encrypted
		JLabel lblSourceFilePath = new JLabel("Source File Path");
		frame.getContentPane().add(lblSourceFilePath, "2, 4, right, default");

		// Text box for Specifying the source path of the file to be encrypted
		sourceFile = new JTextField();
		frame.getContentPane().add(sourceFile, "4, 4, fill, default");
		sourceFile.setColumns(10);

		// Label for Password
		JLabel lblPassword = new JLabel("Password");
		frame.getContentPane().add(lblPassword, "2, 8, right, default");

		// Text box for Password
		passwordField = new JPasswordField();
		frame.getContentPane().add(passwordField, "4, 8, fill, default");

		// Label for the whole path where the encrypted file to be stored in
		// your dropbox folder including the name of the file.
		JLabel lblDestinationFilePath = new JLabel("Destination File Path");
		frame.getContentPane().add(lblDestinationFilePath, "2, 12, right, default");

		// Text box for the whole path where the encrypted file to be stored in
		// your dropbox folder including the name of the file.
		destinationFile = new JTextField();
		frame.getContentPane().add(destinationFile, "4, 12, fill, default");
		destinationFile.setColumns(10);

		// Button to perform the call to the encryption process -- call to
		// Encode.enc method to encrypt and upload the data to Dropbox.
		JButton btnEncryptUpload = new JButton("Encrypt & Upload");
		btnEncryptUpload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				EncodeFile encFile = new EncodeFile();
				try {
					encFile.enc(sourceFile.getText(), destinationFile.getText(), passwordField.getText(),
							ACCESS_TOKEN.getText(), hashFiledestiantion.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		JLabel lblDestinationHashfilePath = new JLabel("Destination HashFile Path");
		frame.getContentPane().add(lblDestinationHashfilePath, "2, 14, right, default");

		hashFiledestiantion = new JTextField();
		frame.getContentPane().add(hashFiledestiantion, "4, 14, fill, default");
		hashFiledestiantion.setColumns(10);
		frame.getContentPane().add(btnEncryptUpload, "2, 16");

		JSeparator separator_1 = new JSeparator();
		frame.getContentPane().add(separator_1, "2, 18");

		JSeparator separator = new JSeparator();
		frame.getContentPane().add(separator, "4, 18");

		JLabel lblEncodedFilePath = new JLabel("Encoded File Path");
		frame.getContentPane().add(lblEncodedFilePath, "2, 22, right, default");

		encodedFilePath = new JTextField();
		frame.getContentPane().add(encodedFilePath, "4, 22, fill, default");
		encodedFilePath.setColumns(10);

		JLabel lblDestinationFilePath_1 = new JLabel("Destination File Path");
		frame.getContentPane().add(lblDestinationFilePath_1, "2, 26, right, default");

		decodedFilePath = new JTextField();
		frame.getContentPane().add(decodedFilePath, "4, 26, fill, default");
		decodedFilePath.setColumns(10);

		JLabel lblHashFilePath = new JLabel("Hash File Path");
		frame.getContentPane().add(lblHashFilePath, "2, 28, right, default");

		hashFileSource = new JTextField();
		frame.getContentPane().add(hashFileSource, "4, 28, fill, default");
		hashFileSource.setColumns(10);

		JLabel lblPassword_1 = new JLabel("Password");
		frame.getContentPane().add(lblPassword_1, "2, 30, right, default");

		passwordField_1 = new JPasswordField();
		frame.getContentPane().add(passwordField_1, "4, 30, fill, default");

		JLabel lblIvFilePath = new JLabel("IV file Path");
		frame.getContentPane().add(lblIvFilePath, "2, 34, right, default");

		ivFilePath = new JTextField();
		frame.getContentPane().add(ivFilePath, "4, 34, fill, default");
		ivFilePath.setColumns(10);

		JLabel lblSaltFilePath = new JLabel("Salt file Path");
		frame.getContentPane().add(lblSaltFilePath, "2, 38, right, default");

		saltFilePath = new JTextField();
		frame.getContentPane().add(saltFilePath, "4, 38, fill, default");
		saltFilePath.setColumns(10);

		JButton btnNewButton = new JButton("Download & Decrypt");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					DecodeFile.dec(encodedFilePath.getText(), decodedFilePath.getText(), passwordField_1.getText(),
							ivFilePath.getText(), saltFilePath.getText(), ACCESS_TOKEN.getText(),
							hashFileSource.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(btnNewButton, "2, 42");
	}

}
