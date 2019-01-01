/**
 * 
 */
package le2lejosev3.robots.colorsorter;

import java.util.ArrayList;
import java.util.logging.Logger;

import le2lejosev3.logging.Setup;
import le2lejosev3.pblocks.ColorSensor;
import le2lejosev3.pblocks.Display;
import le2lejosev3.pblocks.LargeMotor;
import le2lejosev3.pblocks.MediumMotor;
import le2lejosev3.pblocks.Sound;
import le2lejosev3.pblocks.TouchSensor;
import le2lejosev3.pblocks.Wait;
import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Color Sorter
 * 
 * @author Roland Blochberger
 */
public class ColorSorter {

	private static Class<?> clazz = ColorSorter.class;
	private static final Logger log = Logger.getLogger(clazz.getName());

	// the robot configuration
	static final Port motorPortA = MotorPort.A; // medium motor
	static final Port motorPortC = MotorPort.C; // large motor
	static final Port touchSensorPort = SensorPort.S1; // touch sensor
	static final Port colorSensorPort = SensorPort.S3; // color sensor

	// the motors:
	private static final MediumMotor motorA = new MediumMotor(motorPortA);
	private static final LargeMotor motorD = new LargeMotor(motorPortC);
	// the sensors:
	private static final TouchSensor touchSens = new TouchSensor(touchSensorPort);
	private static final ColorSensor colorSens = new ColorSensor(colorSensorPort);

	// the variables:
	// numeric array A
	private static ArrayList<Integer> a = new ArrayList<Integer>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// setup logging to file
		Setup.log2File(clazz);
		log.fine("Starting ...");

		// M Loop
		while (Button.ESCAPE.isUp()) {

			// Initialize
			initialize();

			// SCN Loop: scan colored bricks to be sorted
			scnLoop();

			// Play sound file "Ready" with volume 100 and wait until done (0)
			Sound.playFile("Ready", 100, Sound.WAIT);
			// Display image "EV3" on LCD at 0, 0 and clear screen before
			Display.image("EV3", true, 0, 0);

			// SRT Loop: sort the colored bricks
			srtLoop();
		}

		log.fine("The End");
	}

	/**
	 * initialize
	 */
	private static void initialize() {
		log.fine("");
		// Medium motor on for 2 seconds with power 8 and brake at end
		motorA.motorOnForSeconds(8, 2F, true);
		// log.fine("Motor A rotation: " + motorA.measureDegrees() + "deg.");
		// Medium motor on for 180 degrees with power -30 and brake at end
		motorA.motorOnForDegrees(-30, 180, true);
		// log.fine("Motor A rotation: " + motorA.measureDegrees() + "deg.");

		// Large motor on with power -50
		motorD.motorOn(-50);
		// Wait until touch sensor is pressed (status 1)
		touchSens.waitCompareState(TouchSensor.PRESSED);
		// Large motor off and do not brake at end
		motorD.motorOff(false);

		// Wait 1 second
		Wait.time(1F);
		// Reset Large motor rotation
		motorD.rotationReset();

		// clear LCD screen
		Display.textGrid("", true, 0, 0, false, 2);

		// empty numeric array A
		a.clear();
	}

	/**
	 * SCN Loop (scan the pieces)
	 */
	private static void scnLoop() {
		log.fine("");

		String text = null;
		boolean brickButton2Pressed = false;
		int color = 0;
		boolean validColor = false;
		// SCN Loop
		while (Button.ESCAPE.isUp()) {

			// Display image "Right" on LCD at 0, 0 and clear screen before
			Display.image("Right", true, 0, 0);

			// Merge text and array A length
			text = "# = " + a.size();
			// Display the merged text on LCD at 0, 0 with large font (2) and do not clear
			// screen before
			Display.textGrid(text, false, 0, 0, Display.COLOR_BLACK, Display.FONT_LARGE);

			// BC Loop
			while (Button.ESCAPE.isUp()) {

				// Get compare result of Brick button 2 with status 1 (pressed)
				brickButton2Pressed = Button.ENTER.isDown();

				// check wheather color sensor sees one of the colors 2, 3, 4, or 5 (one of the
				// valid sort colors)
				color = colorSens.measureColor();
				validColor = (color == ColorSensor.COLOR_BLUE) || (color == ColorSensor.COLOR_GREEN)
						|| (color == ColorSensor.COLOR_YELLOW) || (color == ColorSensor.COLOR_RED);
				if (brickButton2Pressed || validColor) {
					// leave loop if button 2 pressed or a valid sort color was detected
					break;
				}
			} // end BC Loop
			log.fine("found color: " + color + " (" + colorText(color) + ")");

			// Compare Brick button 2 with status 0 (released)
			if (Button.ENTER.isUp()) {

				// Play tone 1000Hz for 0.1 seconds with volume 100 once and don't wait for end
				// (1)
				Sound.playTone(1000, 0.1F, 100, Sound.ONCE);

				// Append detected color to array A
				a.add(color);
				log.fine("Color Brick # " + a.size() + ", color: " + color + " (" + colorText(color) + ")");

				// Wait until color sensor sees one of the colors 0 or 1 (color brick was
				// removed)
				while (Button.ESCAPE.isUp()) {
					color = colorSens.measureColor();
					if ((color == ColorSensor.COLOR_NONE) || (color == ColorSensor.COLOR_BLACK)) {
						break;
					}
					Wait.time(0.002F);
				}

				// Play tone 2000Hz for 0.1 seconds with volume 100 once and wait for end
				Sound.playTone(2000, 0.1F, 100, Sound.WAIT);

				// Display image "Backward" on LCD at 0, 0 and clear screen before
				Display.image("Backward", true, 0, 0);

				// Wait 2 seconds
				Wait.time(2F);
			}

			// Check length of array
			if ((a.size() >= 8) || brickButton2Pressed) {
				// leave loop if array 'full' or button 2 pressed
				break;
			}
		} // end SCN Loop
	}

	/**
	 * SRT Loop (sorting)
	 */
	private static void srtLoop() {
		log.fine("");
		int color = 0;
		// SRT Loop
		for (int i = 0; (i < a.size()) && Button.ESCAPE.isUp(); i++) {

			// Wait 1 second
			Wait.time(1F);

			// Large motor on with power -50
			motorD.motorOn(-50);
			// Wait until touch sensor is pressed (state 1)
			touchSens.waitCompareState(TouchSensor.PRESSED);
			// Large motor off and do not brake at end
			motorD.motorOff(false);

			// Read array A element at index i
			color = a.get(i);
			log.fine("Sort brick # " + (i + 1) + ": " + colorText(color));
			// switch
			switch (color) {
			case ColorSensor.COLOR_BLUE:
				// Play sound file "Blue" with volume 100 and wait until done (0)
				Sound.playFile("Blue", 100, Sound.WAIT);
				// Large motor D on for 10 degrees with power 50 and brake at end
				motorD.motorOnForDegrees(50, 10, true);
				break;

			case ColorSensor.COLOR_GREEN:
				// Play sound file "Green" with volume 100 and wait until done (0)
				Sound.playFile("Green", 100, Sound.WAIT);
				// Large motor D on for 132 degrees with power 50 and brake at end
				// (better use 180 degrees)
				motorD.motorOnForDegrees(50, 180, true);
				break;

			case ColorSensor.COLOR_YELLOW:
				// Play sound file "Yellow" with volume 100 and wait until done (0)
				Sound.playFile("Yellow", 100, Sound.WAIT);
				// Large motor D on for 360 degrees with power 50 and brake at end
				motorD.motorOnForDegrees(50, 360, true);
				break;

			case ColorSensor.COLOR_RED:
				// Play sound file "Red" with volume 100 and wait until done (0)
				Sound.playFile("Red", 100, Sound.WAIT);
				// Large motor D on for 530 degrees with power 50 and brake at end
				motorD.motorOnForDegrees(50, 530, true);
				break;
			}

			// push the brick out:
			// Medium motor A on for 90 degrees with power 100 and brake at end
			// log.fine("Motor A rotation: " + motorA.measureDegrees() + "deg.");
			motorA.motorOnForDegrees(100, 90, true);
			// log.fine("Motor A rotation: " + motorA.measureDegrees() + "deg.");
			// Medium motor A on for 90 degrees with power -100 and brake at end
			motorA.motorOnForDegrees(-100, 90, true);
			// log.fine("Motor A rotation: " + motorA.measureDegrees() + "deg.");

		} // end SRT Loop
	}

	/**
	 * Convert the color number to the respective color name text.
	 * 
	 * @param colorId LEGO color number.
	 * @return the color name text.
	 */
	private static String colorText(int colorId) {
		switch (colorId) {
		case ColorSensor.COLOR_BLACK:
			return "BLACK";
		case ColorSensor.COLOR_BLUE:
			return "BLUE";
		case ColorSensor.COLOR_GREEN:
			return "GREEN";
		case ColorSensor.COLOR_YELLOW:
			return "YELLOW";
		case ColorSensor.COLOR_RED:
			return "RED";
		case ColorSensor.COLOR_WHITE:
			return "WHITE";
		case ColorSensor.COLOR_BROWN:
			return "BROWN";
		default:
			return "Unknown";
		}
	}
}
