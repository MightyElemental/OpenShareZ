package mightyelemental.opensharez;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

public class OSZAppFrame extends JFrame {

	private static final long serialVersionUID = -6387774227145242530L;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public OSZAppFrame() {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setBounds( 100, 100, 737, 404 );
		setTitle( "OpenShareZ - Screen Share Program" );
		this.setLocationRelativeTo( null );
		contentPane = new JPanel();
		contentPane.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setContentPane( contentPane );
		contentPane.setLayout( null );

		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout( new GridLayout( 0, 1 ) );
		menuBar.setBounds( 0, 0, 191, 377 );
		contentPane.add( menuBar );

		JMenu mnCapture = new JMenu( "Capture" );
		mnCapture.setForeground( Color.WHITE );
		mnCapture.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/capture/camera.png" ) ) );
		mnCapture.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnCapture );

		JMenuItem mntmFullscreen = new JMenuItem( "Fullscreen" );
		mntmFullscreen.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				BufferedImage img = CaptureOperations.captureAllDisplays();
				OpenShareZ.CAPTURE.play();
				try {
					Utils.saveImage( img, "fullscreen" );
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				OpenShareZ.TASK_COMPLETE.play();
			}
		} );
		mntmFullscreen.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/capture/layer.png" ) ) );
		mntmFullscreen.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mntmFullscreen );

		JMenu mnWindow = new JMenu( "Window" );
		mnWindow.setEnabled( false );
		mnWindow.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mnWindow );

		JMenu mnMonitor = new JMenu( "Monitor" );
		mnMonitor.setEnabled( false );
		mnMonitor.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/capture/monitor.png" ) ) );
		mnMonitor.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mnMonitor );

		JMenuItem mntmRegion = new JMenuItem( "Region" );
		mntmRegion.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor( 1 );
				exec.schedule( () -> {
					BufferedImage img = CaptureOperations.captureRegion();
					try {
						Utils.saveImage( img, "regionselect" );
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}, 1, TimeUnit.MILLISECONDS );
			}
		} );
		mntmRegion.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/capture/layer-shape.png" ) ) );
		mntmRegion.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		mnCapture.add( mntmRegion );

		JMenu mnUpload = new JMenu( "Upload" );
		mnUpload.setForeground( Color.WHITE );
		mnUpload.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/upload/arrow-090.png" ) ) );
		mnUpload.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnUpload );

		JMenu mnWorkflows = new JMenu( "Workflows" );
		mnWorkflows.setForeground( Color.WHITE );
		mnWorkflows.setIcon( new ImageIcon(
				OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/categories.png" ) ) );
		mnWorkflows.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnWorkflows );

		JMenu mnTools = new JMenu( "Tools" );
		mnTools.setForeground( Color.WHITE );
		mnTools.setIcon( new ImageIcon(
				OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/toolbox.png" ) ) );
		mnTools.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnTools );

		JSeparator separator = new JSeparator();
		menuBar.add( separator );

		JMenu mnAfterCaptureTasks = new JMenu( "After capture tasks" );
		mnAfterCaptureTasks.setForeground( Color.WHITE );
		mnAfterCaptureTasks.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/upload/image-export.png" ) ) );
		mnAfterCaptureTasks.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnAfterCaptureTasks );

		JMenu mnAfterUploadTasks = new JMenu( "After upload tasks" );
		mnAfterUploadTasks.setForeground( Color.WHITE );
		mnAfterUploadTasks.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/upload/upload-cloud.png" ) ) );
		mnAfterUploadTasks.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnAfterUploadTasks );

		JMenu mnDestinations = new JMenu( "Destinations" );
		mnDestinations.setForeground( Color.WHITE );
		mnDestinations.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/upload/drive-globe.png" ) ) );
		mnDestinations.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnDestinations );

		JMenu mnApplicationSettings = new JMenu( "Application settings..." );
		mnApplicationSettings.setForeground( Color.WHITE );
		mnApplicationSettings.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/wrench-screwdriver.png" ) ) );
		mnApplicationSettings.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnApplicationSettings );

		JMenu mnTaskSettings = new JMenu( "Task settings..." );
		mnTaskSettings.setForeground( Color.WHITE );
		mnTaskSettings.setIcon( new ImageIcon(
				OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/gear.png" ) ) );
		mnTaskSettings.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnTaskSettings );

		JMenu mnHotkeySettings = new JMenu( "Hotkey settings..." );
		mnHotkeySettings.setForeground( Color.WHITE );
		mnHotkeySettings.setIcon( new ImageIcon(
				OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/keyboard.png" ) ) );
		mnHotkeySettings.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnHotkeySettings );

		JSeparator separator_1 = new JSeparator();
		menuBar.add( separator_1 );

		JMenuItem mnScreenshotsFolder = new JMenuItem( "Screenshots folder..." );
		mnScreenshotsFolder.setForeground( Color.WHITE );
		mnScreenshotsFolder.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/folder-open-image.png" ) ) );
		mnScreenshotsFolder.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnScreenshotsFolder );

		JMenuItem mnHistory = new JMenuItem( "History..." );
		mnHistory.setForeground( Color.WHITE );
		mnHistory.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/application-blog.png" ) ) );
		mnHistory.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mnHistory );

		JMenuItem mntmImageHistory = new JMenuItem( "Image history..." );
		mntmImageHistory.setForeground( Color.WHITE );
		mntmImageHistory.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/application-icon-large.png" ) ) );
		mntmImageHistory.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mntmImageHistory );

		JMenuItem mntmDebug = new JMenuItem( "Debug" );
		mntmDebug.setForeground( Color.WHITE );
		mntmDebug.setIcon( new ImageIcon( OSZAppFrame.class
				.getResource( "/mightyelemental/opensharez/icons/traffic-cone.png" ) ) );
		mntmDebug.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mntmDebug );

		JMenuItem mntmDonate = new JMenuItem( "Donate..." );
		mntmDonate.setForeground( Color.WHITE );
		mntmDonate.setIcon( new ImageIcon(
				OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/heart.png" ) ) );
		mntmDonate.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mntmDonate );

		JMenuItem mntmAbout = new JMenuItem( "About..." );
		mntmAbout.setForeground( Color.WHITE );
		mntmAbout.setIcon( new ImageIcon(
				OSZAppFrame.class.getResource( "/mightyelemental/opensharez/icons/crown.png" ) ) );
		mntmAbout.setFont( new Font( "Source Code Pro Medium", Font.PLAIN, 12 ) );
		menuBar.add( mntmAbout );

		JList<String> list = new JList<String>();
		list.setBounds( 203, 0, 213, 377 );
		contentPane.add( list );
	}
}
