package mightyelemental.opensharez;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class RecordHUDFrame extends FixedJFrame {

	private static final long serialVersionUID = -2347524198137977138L;

	FixedJFrame left   = new FixedJFrame();
	FixedJFrame right  = new FixedJFrame();
	FixedJFrame bottom = new FixedJFrame();

	public RecordHUDFrame(Rectangle rect, Process ffmpeg) {
		super( "Recording region..." );

		this.addWindowListener( new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
				Utils.stopRecording( ffmpeg );
				ffmpeg.destroy();
				left.dispose();
				right.dispose();
				bottom.dispose();
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
		setAlwaysOnTop( true );
		setResizable( false );
		setLocation( rect.x - 5, rect.y - 32 );
		this.getContentPane().setPreferredSize( new Dimension( rect.width, 0 ) );
		this.pack();
		this.setLocked( true );
		// this.setOpacity( 0.8f );

		left.setBounds( rect.x - 2, rect.y, 2, rect.height );
		left.setType( Type.POPUP );
		left.setVisible( true );
		left.setLocked( true );
		left.setResizable( false );
		left.setAlwaysOnTop( true );

		right.setBounds( rect.x + rect.width, rect.y, 2, rect.height );
		right.setType( Type.POPUP );
		right.setVisible( true );
		right.setLocked( true );
		right.setResizable( false );
		right.setAlwaysOnTop( true );

		bottom.setBounds( rect.x - 2, rect.y + rect.height, rect.width + 4, 2 );
		bottom.setType( Type.POPUP );
		bottom.setVisible( true );
		bottom.setLocked( true );
		bottom.setResizable( false );
		bottom.setAlwaysOnTop( true );
	}

	public void dispose() { left.dispose(); right.dispose(); bottom.dispose(); super.dispose(); }

}
