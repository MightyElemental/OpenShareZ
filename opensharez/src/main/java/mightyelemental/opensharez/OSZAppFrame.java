package mightyelemental.opensharez;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

public class OSZAppFrame extends JFrame {

	private static final long serialVersionUID = -6387774227145242530L;

	private JPanel contentPane;
	private JTable table;

	public void fullScreenCapture() {
		BufferedImage img = CaptureOperations.captureAllDisplays();
		OpenShareZ.CAPTURE.play();
		try {
			AfterCaptureOperations.runAfterCaptureOps( img, "fullscreen" );
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void regionCapture() {
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor( 1 );
		exec.schedule( () -> {
			BufferedImage img = CaptureOperations.captureRegion();
			if (img != null) {
				OpenShareZ.CAPTURE.play();
				try {
					AfterCaptureOperations.runAfterCaptureOps( img, "regionselect" );
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}, 1, TimeUnit.MILLISECONDS );
	}

	public static void captureRecording() {
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor( 1 );
		exec.schedule( () -> { CaptureOperations.startScreenRecord(); }, 1, TimeUnit.MILLISECONDS );
	}

	/**
	 * Create the frame.
	 */
	public OSZAppFrame() {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 843, 515 );
		// setBackground(new Color(0, 0, 0, 0));
		setTitle( OpenShareZ.TITLE );
		this.setLocationRelativeTo( null );
		contentPane = new JPanel();
		contentPane.setBackground( Color.DARK_GRAY );
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setContentPane( contentPane );
		contentPane.setLayout( null );

		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout( new GridLayout( 0, 1 ) );
		menuBar.setBounds( 0, 0, 191, 488 );
		contentPane.add( menuBar );

		addCaptureMenu( menuBar );

		JMenu mnUpload = new JMenu( "Upload" );
		mnUpload.setForeground( Color.WHITE );
		mnUpload.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/upload/arrow-090.png" ) ) );
		mnUpload.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnUpload );

		JMenu mnWorkflows = new JMenu( "Workflows" );
		mnWorkflows.setForeground( Color.WHITE );
		mnWorkflows.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/categories.png" ) ) );
		mnWorkflows.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnWorkflows );

		JMenu mnTools = new JMenu( "Tools" );
		mnTools.setForeground( Color.WHITE );
		mnTools.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/toolbox.png" ) ) );
		mnTools.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnTools );

		JSeparator separator = new JSeparator();
		menuBar.add( separator );

		addAfterCaptureMenu( menuBar );

		JMenu mnAfterUploadTasks = new JMenu( "After upload tasks" );
		mnAfterUploadTasks.setForeground( Color.WHITE );
		mnAfterUploadTasks.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/upload/upload-cloud.png" ) ) );
		mnAfterUploadTasks.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnAfterUploadTasks );

		JMenu mnDestinations = new JMenu( "Destinations" );
		mnDestinations.setForeground( Color.WHITE );
		mnDestinations.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/upload/drive-globe.png" ) ) );
		mnDestinations.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnDestinations );

		JMenu mnApplicationSettings = new JMenu( "Application settings..." );
		mnApplicationSettings.setForeground( Color.WHITE );
		mnApplicationSettings.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/wrench-screwdriver.png" ) ) );
		mnApplicationSettings.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnApplicationSettings );

		JMenu mnTaskSettings = new JMenu( "Task settings..." );
		mnTaskSettings.setForeground( Color.WHITE );
		mnTaskSettings.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/gear.png" ) ) );
		mnTaskSettings.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnTaskSettings );

		JMenu mnHotkeySettings = new JMenu( "Hotkey settings..." );
		mnHotkeySettings.setForeground( Color.WHITE );
		mnHotkeySettings
				.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/keyboard.png" ) ) );
		mnHotkeySettings.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnHotkeySettings );

		JSeparator separator_1 = new JSeparator();
		menuBar.add( separator_1 );

		JMenuItem mnScreenshotsFolder = new JMenuItem( "Screenshots folder..." );
		mnScreenshotsFolder.setForeground( Color.WHITE );
		mnScreenshotsFolder.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/folder-open-image.png" ) ) );
		mnScreenshotsFolder.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnScreenshotsFolder );

		JMenuItem mnHistory = new JMenuItem( "History..." );
		mnHistory.setForeground( Color.WHITE );
		mnHistory.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/application-blog.png" ) ) );
		mnHistory.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnHistory );

		JMenuItem mntmImageHistory = new JMenuItem( "Image history..." );
		mntmImageHistory.setForeground( Color.WHITE );
		mntmImageHistory.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/application-icon-large.png" ) ) );
		mntmImageHistory.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mntmImageHistory );

		JMenuItem mntmDebug = new JMenuItem( "Debug" );
		mntmDebug.setForeground( Color.WHITE );
		mntmDebug.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/traffic-cone.png" ) ) );
		mntmDebug.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mntmDebug );

		JMenuItem mntmDonate = new JMenuItem( "Donate..." );
		mntmDonate.setForeground( Color.WHITE );
		mntmDonate.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/heart.png" ) ) );
		mntmDonate.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mntmDonate );

		JMenuItem mntmAbout = new JMenuItem( "About..." );
		mntmAbout.setForeground( Color.WHITE );
		mntmAbout.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/crown.png" ) ) );
		mntmAbout.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mntmAbout );

		addHistoryList( contentPane );
//		JList<String> list = new JList<String>();// TODO:THIS REALLY NEEDS SORTING
//		list.setBackground( new Color( 34, 45, 50 ) );
//		DefaultListModel<String> strList = new DefaultListModel<String>();
//		strList.addElement( "/home/james/Pictures/test.png" );
//		strList.addElement( "/home/james/Pictures/test.png" );
//		strList.addElement( "/home/james/Pictures/test.png" );
//		strList.addElement( "/home/james/Pictures/test.png" );
//		strList.addElement( "/home/james/Pictures/test.png" );
//		list.setModel( strList );
//		list.setBounds( 192, 0, 213, 488 );
//		contentPane.add( list );

		registerHotkeys();
	}

	private void addHistoryList(JPanel contentPane) {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds( 193, 0, 436, 488 );
		// scrollPane.setBackground( new Color( 34, 45, 50 ) );
		contentPane.add( scrollPane );

		table = new JTable();
		// table.setBackground( new Color( 34, 45, 50 ) );
		scrollPane.setViewportView( table );
		table.setModel( new DefaultTableModel( new Object[][] {}, new String[] {
				"Filename", "Status", "Progress", "URL"
		} ) {

			private static final long serialVersionUID = 7247434836403935167L;
			@SuppressWarnings("rawtypes")
			Class[]                   columnTypes      = new Class[] {
					String.class, String.class, String.class, String.class
			};

			@SuppressWarnings({
					"rawtypes", "unchecked"
			})
			public Class getColumnClass(int columnIndex) { return columnTypes[columnIndex]; }

			public boolean isCellEditable(int row, int column) { return false; }
		} );
	}

	private void addAfterCaptureMenu(JMenuBar menuBar) {
		JMenu mnAfterCaptureTasks = new JMenu( "After capture tasks" );
		mnAfterCaptureTasks.setForeground( Color.WHITE );
		mnAfterCaptureTasks.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/upload/image-export.png" ) ) );
		mnAfterCaptureTasks.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnAfterCaptureTasks );

		JCheckBoxMenuItem mntmCopyImageToClipboard = new JCheckBoxMenuItem( "Copy image to clipboard" );
		mntmCopyImageToClipboard.setSelected( true );
		mntmCopyImageToClipboard.setIcon( new ImageIcon(
				OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/capture/clipboard-paste-image.png" ) ) );
		mntmCopyImageToClipboard.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnAfterCaptureTasks.add( mntmCopyImageToClipboard );

		JCheckBoxMenuItem mntmSaveImageToFile = new JCheckBoxMenuItem( "Save image to file" );
		mntmSaveImageToFile.setSelected( true );
		mntmSaveImageToFile
				.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/capture/disk.png" ) ) );
		mntmSaveImageToFile.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnAfterCaptureTasks.add( mntmSaveImageToFile );
	}

	private void addCaptureMenu(JMenuBar menuBar) {
		JMenu mnCapture = new JMenu( "Capture" );
		mnCapture.setForeground( Color.WHITE );
		mnCapture
				.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/capture/camera.png" ) ) );
		mnCapture.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnCapture );

		JMenuItem mntmFullscreen = new JMenuItem( "Fullscreen" );
		mntmFullscreen.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) { fullScreenCapture(); }
		} );
		mntmFullscreen
				.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/capture/layer.png" ) ) );
		mntmFullscreen.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mntmFullscreen );

		JMenu mnWindow = new JMenu( "Window" );
		mnWindow.setEnabled( false );
		mnWindow.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mnWindow );

		JMenu mnMonitor = new JMenu( "Monitor" );
		mnMonitor
				.setIcon( new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/capture/monitor.png" ) ) );
		mnMonitor.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mnMonitor );

		registerMonitors( mnMonitor );

		JMenuItem mntmRegion = new JMenuItem( "Region" );
		mntmRegion.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) { regionCapture(); }
		} );
		mntmRegion.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/capture/layer-shape.png" ) ) );
		mntmRegion.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mntmRegion );

		JMenuItem mntmScreenRecord = new JMenuItem( "Screen recording" );
		mntmScreenRecord.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) { captureRecording(); }
		} );
		mntmScreenRecord.setIcon(
				new ImageIcon( OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/capture/camcorder-image.png" ) ) );
		mntmScreenRecord.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mntmScreenRecord );
	}

	public void registerMonitors(JMenu mnMonitor) {
		for (int i = 0; i < CaptureOperations.screens.length; i++) {
			final int monNum = i;
			DisplayMode dm = CaptureOperations.screens[i].getDisplayMode();
			String name = String.format( "%d. %dx%d", i + 1, dm.getWidth(), dm.getHeight() );
			JMenuItem mntmFullscreen = new JMenuItem( name );
			mntmFullscreen.addActionListener( new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					BufferedImage img = CaptureOperations.captureScreen( monNum );
					OpenShareZ.CAPTURE.play();

					try {
						AfterCaptureOperations.runAfterCaptureOps( img, "monitor" + monNum );
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} );
			mntmFullscreen.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
			mnMonitor.add( mntmFullscreen );
		}
	}

	public void registerHotkeys() {
		Provider provider = Provider.getCurrentProvider( true );
		provider.register( KeyStroke.getKeyStroke( "PRINTSCREEN" ), new HotKeyListener() {

			@Override
			public void onHotKey(HotKey hotKey) { System.out.println( "hotkey fullscreen screenshot" ); fullScreenCapture(); }

		} );
		provider.register( KeyStroke.getKeyStroke( "control PRINTSCREEN" ), new HotKeyListener() {

			@Override
			public void onHotKey(HotKey hotKey) { System.out.println( "hotkey region screenshot" ); regionCapture(); }

		} );
		provider.register( KeyStroke.getKeyStroke( "shift PRINTSCREEN" ), new HotKeyListener() {

			@Override
			public void onHotKey(HotKey hotKey) { System.out.println( "hotkey record screen" ); captureRecording(); }

		} );
	}

}
