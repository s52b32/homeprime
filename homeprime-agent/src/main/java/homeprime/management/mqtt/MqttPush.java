package homeprime.management.mqtt;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 *
 * Runnable for management mode of type push.
 *
 * @author Milan Ramljak
 *
 */
public class MqttPush implements Runnable {

    private boolean doStop = false;

    public synchronized void doStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }

    @Override
    public void run() {
        System.out.println("<--Pi4J--> GPIO Listen Example ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13,
                PinPullResistance.PULL_DOWN);

        // provision gpio pin #01 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_12,
                PinPullResistance.PULL_DOWN);

        // set shutdown state for this input pin
        // myButton.setShutdownOptions(true);
        // reconfigure the pin back to an input pin
        // myButton.setMode(PinMode.DIGITAL_INPUT);
        myButton.setDebounce(1000);
        myButton2.setDebounce(2000);

        // create and register gpio pin listener
        myButton2.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                System.out.println(myButton2.getState().getValue());
                if (event.getState().isHigh()) {

                }
            }

        });

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {

            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                System.out.println(myButton.getState().getValue());
            }

        });

        System.out.println(" ... complete the GPIO #01 circuit and see the listener feedback here in the console.");

        // keep program running until user aborts (CTRL-C)
        while (keepRunning()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Sleep interrupted");
            }
        }
    }
}
