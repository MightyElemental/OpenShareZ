package mightyelemental.opensharez;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class RecordHUDFrame extends JFrame {

	private static final long serialVersionUID = -2347524198137977138L;

	private Point locked;

	public RecordHUDFrame(Rectangle rect, Process ffmpeg) {
		super( "Recording region..." );
		removeNotify();
		this.setUndecorated( true );
		setLayout( null );
		addNotify();
		addComponentListener( new ComponentAdapter() {

			public void componentMoved(ComponentEvent e) {
				if (locked != null) RecordHUDFrame.this.setLocation( locked );
			}
		} );
		this.addWindowListener( new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				ffmpeg.destroy();
				System.out.println( "stopped recording" );
			}

			@Override
			public void windowClosed(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}

		} );
		this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setBackground( new Color( 0, 0, 0, 0 ) );
		setAlwaysOnTop( true );
		setResizable( false );

		pack();
		setLocation( rect.x - 5, rect.y - 32 );
		locked = getLocation();// TODO: allow mouse events to pass through - even if that means using 4 jframes as a box
		this.getContentPane().setPreferredSize( new Dimension( rect.width, rect.height + 5 ) );
		this.pack();
		// this.setOpacity( 0.8f );

		JButton btnStop = new JButton( "Stop" );
		btnStop.setBounds( 0, 0, 191, 377 );
	}

}
